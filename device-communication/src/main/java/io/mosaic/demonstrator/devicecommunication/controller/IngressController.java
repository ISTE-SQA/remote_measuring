package io.mosaic.demonstrator.devicecommunication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import io.mosaic.demonstrator.devicecommunication.dto.DisplayData;
import io.mosaic.demonstrator.devicecommunication.dto.TelemetryData;
import io.mosaic.demonstrator.devicecommunication.entity.Campaign;
import io.mosaic.demonstrator.devicecommunication.entity.Telemetry;
import io.mosaic.demonstrator.devicecommunication.repository.CampaignRepository;
import io.mosaic.demonstrator.devicecommunication.service.CampaignService;

@RestController
@RequestMapping("ingress")
public class IngressController {

	
	@Autowired
	private CampaignService campaignService;
	
	@GetMapping(path = "/online", produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public DisplayData online() {
		String id = campaignService.createCampaign();
		DisplayData displayData = new DisplayData(id);
		return displayData;
	}

	@GetMapping(path = "/{id}/offline", produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public DisplayData offline(@PathVariable String id) {

		Optional<Campaign> campaign = campaignService.closeCampaign(id);

		DisplayData displayData = new DisplayData("sample");

		return displayData;
	}

	@PutMapping(path = "/{id}/", produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public DisplayData sendTelemetryData(@PathVariable String id, @RequestBody TelemetryData blob) {

		var sizeOfCampaigns = campaignService.addData(blob.getContent(), id);
	
		DisplayData displayData = new DisplayData("Size of data added to campaign: " + sizeOfCampaigns);

		return displayData;
	}

}
