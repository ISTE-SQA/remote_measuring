package io.mosaic.demonstrator.dataprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.mosaic.demonstrator.misc.service.ProtocomService;

@SpringBootApplication
public class DataproviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataproviderApplication.class, args);
	}
	
	@Bean(initMethod = "init")
	public ProtocomService protocomService() {
		return new ProtocomService();
	}

}

