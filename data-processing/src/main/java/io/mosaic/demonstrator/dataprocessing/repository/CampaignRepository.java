package io.mosaic.demonstrator.dataprocessing.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import io.mosaic.demonstrator.dataprocessing.entity.Campaign;

public interface CampaignRepository extends MongoRepository<Campaign, String>{

}
