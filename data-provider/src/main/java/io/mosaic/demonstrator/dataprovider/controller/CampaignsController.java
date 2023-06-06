package io.mosaic.demonstrator.dataprovider.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.mosaic.demonstrator.dataprovider.dto.CampaignEntryData;
import io.mosaic.demonstrator.dataprovider.dto.ConvertedTelemetryData;
import io.mosaic.demonstrator.dataprovider.entity.Campaign;
import io.mosaic.demonstrator.dataprovider.service.CampaignService;


@RestController
@RequestMapping("campaigns")
public class CampaignsController {
	
	@Autowired
	private CampaignService campaignService;

	@GetMapping(produces="application/json")
	@ResponseStatus(HttpStatus.OK)
	public List<CampaignEntryData> getCampaigns() {
		
		List<Campaign> campaigns = campaignService.getAvailableCampaigns();
		List<CampaignEntryData> campaignEntries = campaigns.stream().map(cmp -> new CampaignEntryData(cmp.getId())).collect(Collectors.toList());
		return campaignEntries;
	}
	
	@GetMapping(path = "/{id}/", produces="application/json")
	public ResponseEntity<ConvertedTelemetryData> getTelemetryData(@PathVariable("id") String id) {
		Optional<ConvertedTelemetryData> convertedData = campaignService.getConvertedTelemetryData(id);
		if(convertedData.isPresent()) {
			return new ResponseEntity<ConvertedTelemetryData>(convertedData.get(), HttpStatus.OK);
		} else { 
			return new ResponseEntity<ConvertedTelemetryData>(HttpStatus.NOT_FOUND);
		}
		
	}
}
