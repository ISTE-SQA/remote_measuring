package io.mosaic.demonstrator.devicecommunication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.mosaic.demonstrator.misc.service.ProtocomService;

@SpringBootApplication
public class DeviceCommunicationApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeviceCommunicationApplication.class, args);
	}
	
	@Bean(initMethod = "init")
	public ProtocomService protocomService() {
		return new ProtocomService();
	}


}
