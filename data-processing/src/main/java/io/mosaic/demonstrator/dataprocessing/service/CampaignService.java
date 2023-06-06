package io.mosaic.demonstrator.dataprocessing.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.palladiosimulator.protocom.resourcestrategies.activeresource.cpu.FibonacciDemand;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import io.mosaic.demonstrator.dataprocessing.dto.ConvertedTelemetryData;
import io.mosaic.demonstrator.dataprocessing.entity.Campaign;
import io.mosaic.demonstrator.dataprocessing.entity.Telemetry;
import io.mosaic.demonstrator.dataprocessing.repository.CampaignRepository;
import io.mosaic.demonstrator.misc.service.ProtocomService;

@Component
public class CampaignService {
	
	@Autowired
	CampaignRepository campaignRepo;

	@Autowired
	ProtocomService protocomService;
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	
	
	public List<Campaign> getAvailableCampaigns(){
		return campaignRepo.findAll();
	}

	public Optional<ConvertedTelemetryData> getConvertedTelemetryData(String campaignId){
		
		var response = rabbitTemplate.convertSendAndReceiveAsType("conversion-request", campaignId, new ParameterizedTypeReference<ConvertedTelemetryData>() {});
		
		return Optional.ofNullable(response);
	}
}
