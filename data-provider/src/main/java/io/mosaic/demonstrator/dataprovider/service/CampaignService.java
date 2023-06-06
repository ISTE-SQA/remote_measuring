package io.mosaic.demonstrator.dataprovider.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.palladiosimulator.protocom.resourcestrategies.activeresource.cpu.FibonacciDemand;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import io.micrometer.core.annotation.Timed;
import io.mosaic.demonstrator.dataprovider.dto.ConvertedTelemetryData;
import io.mosaic.demonstrator.dataprovider.entity.Campaign;
import io.mosaic.demonstrator.dataprovider.entity.Telemetry;
import io.mosaic.demonstrator.dataprovider.repository.CampaignRepository;
import io.mosaic.demonstrator.misc.service.ProtocomService;

@Component
public class CampaignService {
	
	@Autowired
	CampaignRepository campaignRepo;

	@Autowired
	ProtocomService protocomService;
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Value("${io.mosaic.demonstrator.conversion.queue:conversion-request}")
	private String conversionQueueName;
	
	@Timed(value="service.getAvailableCampaigns",histogram = true)
	public List<Campaign> getAvailableCampaigns(){
		
//		Page<Campaign> onePageOfCampaigns = campaignRepo.findAll(PageRequest.of(page, size, sort));
	
        var query = new Query();
        List<Campaign> campaigns = mongoTemplate.find(query.limit(100), Campaign.class);
		return campaigns;
	}

	public Optional<ConvertedTelemetryData> getConvertedTelemetryData(String campaignId){
		
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		var response = rabbitTemplate.convertSendAndReceiveAsType(conversionQueueName, campaignId, new ParameterizedTypeReference<String>() {});
		ConvertedTelemetryData convertedData = new ConvertedTelemetryData(campaignId, response);
		
		return Optional.ofNullable(convertedData);
	}
}
