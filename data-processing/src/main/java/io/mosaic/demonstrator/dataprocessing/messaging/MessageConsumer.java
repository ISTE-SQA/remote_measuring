package io.mosaic.demonstrator.dataprocessing.messaging;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.mosaic.demonstrator.dataprocessing.entity.ConvertedData;
import io.mosaic.demonstrator.dataprocessing.entity.Telemetry;

import io.mosaic.demonstrator.dataprocessing.repository.CampaignRepository;
import io.mosaic.demonstrator.dataprocessing.repository.ConvertedDataCacheRepository;
import io.mosaic.demonstrator.misc.service.ProtocomService;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.bson.internal.Base64;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

@Component
public class MessageConsumer {

	private static final int CACHE_TIME_WINDOW_PER_CAMPAIGN_IN_SECONDS = 5;

	private static final int CONVERSION_CPU_PER_POINT = 1;

	// 30-40 milliseconds on average based on the data
	private static final long AVERAGE_CONVERSION_STATIC = 30; 
	
    private static final Logger LOGGER = Logger.getLogger(MessageConsumer.class);
	
	@Autowired
	CampaignRepository campaignRepo;

	@Autowired
	ConvertedDataCacheRepository cacheRepo;

	@Autowired
	ProtocomService protocomService;
	
	@Autowired
	MeterRegistry meterRegistry;
	
	private Timer queueDelayTimerDataMsg;
	private Timer queueDelayTimerConversionRequestMsg;
	
	@PostConstruct
	public void initTimers() {
	
		queueDelayTimerDataMsg = meterRegistry.timer("messsaging.queuDelayDataMessage", "entity","rabbit","layer","messaging","cmd","receiveData");
		queueDelayTimerConversionRequestMsg = meterRegistry.timer("messsaging.queuDelayConversionRequest", "entity","rabbit","layer","messaging","cmd","receiveConversionRequest");
		
	}
	
	@RabbitListener(queues = "${io.mosaic.demonstrator.data.queue}", messageConverter = "jackson2JsonMessageConverter")
	public void receivedDataMessage(Message<String> msg) {
		LOGGER.info("Got Message: " + msg + " T:" + Thread.currentThread().getName() + " " + Thread.currentThread().getId());
		recordQueueDelay(msg, queueDelayTimerDataMsg);
		String campaignId = msg.getPayload();
		var currentTime = Date.from(Instant.now());
		
		// 1. get data from DB
		var campaign = campaignRepo.findById(campaignId);
		if (campaign.isPresent()) {
			
			int cachedEntrySize = 0;
			// 2. query cache -> go to 5. if cache is current
			var cacheEntry = cacheRepo.findById(campaignId);
			var dataPoints = campaign.get().getDataPoints();
			
			if (cacheEntry.isPresent()) {
				var cacheData = cacheEntry.get();
				cachedEntrySize = cacheData.getDataSize();
		
				//only trigger data conversion whenever time window exceeded
				long timeDifferenceOfCacheFromNow = ChronoUnit.SECONDS.between(cacheData.getEntryDate().toInstant(), currentTime.toInstant());
				
				if (timeDifferenceOfCacheFromNow < CACHE_TIME_WINDOW_PER_CAMPAIGN_IN_SECONDS)
					return;
				
				// delete old Entry
				cacheRepo.delete(cacheData);				
			}
			
			String convertedData = convertData(cachedEntrySize, dataPoints);

			// 4. put to cache
			var cacheInsert = new ConvertedData(campaignId, convertedData, dataPoints.size(), currentTime);
			LOGGER.info("Inserted data to cache");

			cacheRepo.insert(cacheInsert);

		}
	}

	private void recordQueueDelay(Message<String> msg, Timer timer) {
		Instant timeEnteringQueue =	Instant.ofEpochMilli(Long.parseLong(msg.getHeaders().get("timestamp_in_ms").toString()));
		LOGGER.debug("Time entered queue is: "+timeEnteringQueue);
		Duration queueDuration = Duration.between(Instant.now(),timeEnteringQueue).abs();
		LOGGER.debug("Final duration is: "+queueDuration.getSeconds() + "seconds and " +queueDuration.getNano()+" nanoseconds");
		timer.record(queueDuration);	
	}


	@RabbitListener(queues = "${io.mosaic.demonstrator.conversion.queue}", messageConverter = "jackson2JsonMessageConverter")
	public String receivedConversionRequestMessage	(Message<String> msg) {
		LOGGER.debug("Got Message: " + msg);
		recordQueueDelay(msg, queueDelayTimerConversionRequestMsg);
		
		String campaignId = msg.getPayload();
		String convertedData = "";

		// 1. query cache -> go to 5. if exists
		var cacheEntry = cacheRepo.findById(campaignId);
		if (cacheEntry.isPresent()) {
			LOGGER.debug("cache found for campaignId: " + campaignId);
			convertedData = cacheEntry.get().getConvertedData();
		} else {
			// 2. get data from DB
			var campaign = campaignRepo.findById(campaignId);

			if(campaign.isPresent()) {
				LOGGER.debug("No cache for campaignId: " + campaignId);
				
				int cachedEntrySize = 0;
				var dataPoints = campaign.get().getDataPoints();
				
				convertedData = convertData(cachedEntrySize, dataPoints);
				
				// 4. put to cache
				var cacheInsert = new ConvertedData(campaignId, convertedData, dataPoints.size(), Date.from(Instant.now()));
				LOGGER.debug("Inserted data to cache");
				
				cacheRepo.insert(cacheInsert);
			}
		}

		return convertedData;
	}

	private String convertData(int cachedEntrySize, List<Telemetry> dataPoints) {
		// 3. convert data
		// Conversion CPU load
		LOGGER.info("Converting data");
		
		// emulate conversion
		int numberOfNewDataPoints = dataPoints.size() - cachedEntrySize;
//		protocomService.cpuDemand(numberOfNewDataPoints * CONVERSION_CPU_PER_POINT);
		protocomService.cpuDemand(AVERAGE_CONVERSION_STATIC);
		String emulatedConvertedData = dataPoints.stream().map(Telemetry::getData).map(Base64::encode)
				.collect(Collectors.joining("', '"));
		
		// create json representation 
		String convertedData = "";
		convertedData += "{'points': ['";
		convertedData += emulatedConvertedData;
		convertedData += "']}";
		return convertedData;
	}
}
