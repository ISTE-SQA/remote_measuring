package io.mosaic.demonstrator.dataprocessing.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
@Document
public class ConvertedData {

	@Id
	private String campaignId; 
	
	private String convertedData;
	
	private int dataSize;

	@Indexed(name="entryDate", expireAfterSeconds=3600)
	private Date entryDate;
	
	
	
    public ConvertedData(String campaignId, String convertedData, int dataSize, Date entryDate) {
		super();
		this.campaignId = campaignId;
		this.convertedData = convertedData;
		this.dataSize = dataSize;
		this.entryDate = entryDate;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public String getConvertedData() {
		return convertedData;
	}
	
	public int getDataSize() {
		return dataSize;
	}

	public Date getEntryDate() {
		return entryDate;
	}


}
