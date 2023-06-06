package io.mosaic.demonstrator.devicecommunication.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import io.micrometer.core.annotation.Timed;
import io.mosaic.demonstrator.devicecommunication.entity.Campaign;

@Timed
public interface CampaignRepository extends MongoRepository<Campaign, String>{

}
