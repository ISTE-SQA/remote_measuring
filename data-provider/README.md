# Data Provider

Service built using Spring-Boot and Spring AMQP for RabbitMQ

## Steps to run:

Start RabbitMQ server

Start the service 
``` 
./mvnw spring-boot:run
``` 


## API End Points

**URL** : `/campaigns`

**Method** : `GET`

## Success Responses

**Code** : `200 OK`

**Body** :

```json
[
  {
    "campaignId": "{campaign_id}"
  }
]
```
**Example Response** :

```json
[
  {
    "campaignId": "602d1deb4d00987b73e200c9"
  },
  {
    "campaignId": "60408a684d00987b73e200ca"
  }
]
```
