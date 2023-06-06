# Data Provider

Service built using Spring-Boot and Spring AMQP for RabbitMQ

## Steps to run:

Start RabbitMQ server

Start the service 
``` 
./mvnw spring-boot:run
``` 


## API End Points


## Design Decisions Caching Behavior

* The converted cache data for a campaign lives in the mongo repository for 1 hour set via expireAfterSeconds in the ConvertedData Document. 
* The converted cache is updated with a frequency once every 5 seconds defined in the CACHE_TIME_WINDOW_PER_CAMPAIGN_IN_SECONDS.

