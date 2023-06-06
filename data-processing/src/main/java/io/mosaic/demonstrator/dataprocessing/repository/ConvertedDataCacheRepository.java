package io.mosaic.demonstrator.dataprocessing.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import io.mosaic.demonstrator.dataprocessing.entity.ConvertedData;

public interface ConvertedDataCacheRepository extends MongoRepository<ConvertedData, String>{

	
	
}
