package io.mosaic.demonstrator.devicecommunication.service;

import java.time.Instant;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.palladiosimulator.protocom.resourcestrategies.activeresource.cpu.FibonacciDemand;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.mosaic.demonstrator.devicecommunication.entity.Campaign;
import io.mosaic.demonstrator.devicecommunication.entity.Telemetry;
import io.mosaic.demonstrator.devicecommunication.repository.CampaignRepository;
import io.mosaic.demonstrator.misc.service.ProtocomService;

@Component
@Timed
public class CampaignService {
	
	@Autowired
	CampaignRepository campaignRepo;

	@Autowired
	ProtocomService protocomService;
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	MeterRegistry meterRegistry;

	private Timer saveCampaignTimer;

	private Timer findCampaignTimer;

	private Timer messagingSendDataTimer;

	private Timer protocomTimer;
	
	@PostConstruct
	public void initTimers() {
	
		saveCampaignTimer = meterRegistry.timer("data.saveCampaign", "entity","mongo","layer","data","cmd","save");
		findCampaignTimer = meterRegistry.timer("data.findCampaign", "entity","mongo","layer","data","cmd","find");
		messagingSendDataTimer = meterRegistry.timer("messaging.sendData", "entity","rabbit","layer","messaging","cmd","send");		
	
		protocomTimer = meterRegistry.timer("protocom.cpuDemand", "entity","protocom","layer","service");	
		
	}

	@Timed(value="service.createCampaign",histogram = true)
	public String createCampaign() {
		Campaign c = new Campaign();
		c.setOnline(Instant.now());
		var inserted = campaignRepo.insert(c);
		return inserted.getId();
	}
	
	@Timed(value="service.addData", histogram = true)
	public Integer addData(String content, String id) {
		Optional<Campaign> c = findCampaignById(id);
		
		protocomTimer.record(()-> {
			protocomService.cpuDemand(50);
		});
		
		
		if (c.isPresent()) {
			var campaign = c.get();
			var datapoint = new Telemetry();
			datapoint.setData(content.getBytes());
			campaign.getDataPoints().add(datapoint);
			saveCampaign(campaign);

			sendDataToBroker(id);
						
			return campaign.getDataPoints().size();
		}
		
		
		return -1;
	}

	public void sendDataToBroker(String id) {
		messagingSendDataTimer.record(()-> {
			rabbitTemplate.convertAndSend("data-available",id);
		});
		
	}

	public void saveCampaign(Campaign campaign) {
		saveCampaignTimer.record(()->{
			campaignRepo.save(campaign);
		});
	}

	public Optional<Campaign> findCampaignById(String id) {
		return findCampaignTimer.record(()->{
			return campaignRepo.findById(id);
		}); 
	}

	@Timed(value="service.closeCampaign",histogram = true)
	public Optional<Campaign> closeCampaign(String id) {
		Optional<Campaign> c = findCampaignById(id);

		if (c.isPresent()) {
			var campaign = c.get();
			campaign.setOffline(Instant.now());
			saveCampaign(campaign);
		}
		
		return c;
	}
}
