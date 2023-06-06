package io.mosaic.demonstrator.devicecommunication.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Campaign {

	@Id
	private String id;

	private String description;
	private Instant online;
	private Instant offline;
	
	private List<Telemetry> dataPoints = new ArrayList<>();

	public List<Telemetry> getDataPoints() {
		return dataPoints;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Instant getOnline() {
		return online;
	}

	public void setOnline(Instant online) {
		this.online = online;
	}

	public Instant getOffline() {
		return offline;
	}

	public void setOffline(Instant offline) {
		this.offline = offline;
	}

}