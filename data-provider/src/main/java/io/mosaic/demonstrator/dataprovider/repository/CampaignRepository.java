package io.mosaic.demonstrator.dataprovider.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import io.mosaic.demonstrator.dataprovider.entity.Campaign;

public interface CampaignRepository extends MongoRepository<Campaign, String>{

}
