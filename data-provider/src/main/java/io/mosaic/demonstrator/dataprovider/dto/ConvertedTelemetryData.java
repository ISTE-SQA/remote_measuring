package io.mosaic.demonstrator.dataprovider.dto;

public class ConvertedTelemetryData {

	private String campaignId; 
	
	private String convertedTelemetry;
	
	public ConvertedTelemetryData(String campaignId, String convertedTelemetry) {
		super();
		this.campaignId = campaignId;
		this.convertedTelemetry = convertedTelemetry;
	}

	public String getConvertedTelemetry() {
		return convertedTelemetry;
	}

	public String getCampaignId() {
		return campaignId;
	}
	
}
