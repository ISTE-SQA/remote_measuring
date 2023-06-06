package io.mosaic.demonstrator.dataprocessing;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.mosaic.demonstrator.misc.service.ProtocomService;

@SpringBootApplication
public class DataProcessingApplication {

	@Value("${io.mosaic.demonstrator.conversion.queue:conversion-request}")
	private String inputQueue;
	
	@Value("${io.mosaic.demonstrator.data.queue:data-available}")
	private String dataAvailableQueue;
	
	public static void main(String[] args) {
		SpringApplication.run(DataProcessingApplication.class, args);
	}
	
	@Bean(initMethod = "init")
	public ProtocomService protocomService() {
		return new ProtocomService();
	}

	@Bean
	public Queue inputQueueConversionRequest() {
	    return new Queue(inputQueue, false);
	}
	
	@Bean
	public Queue inputQueueDataAvailable() {
		return new Queue(dataAvailableQueue, false);
	}
	
	@Bean
	public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}

