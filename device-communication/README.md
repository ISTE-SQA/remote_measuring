# Device Communication Microservice

Service built using Spring-Boot and Spring AMQP for RabbitMQ

## Steps to run:

Start RabbitMQ server

Start the service 
``` 
./mvnw spring-boot:run
``` 


## API End Points

# Start a Transmitting Telemetry Data Campaign

Starts a communication with the backend. A transmiting campaign is created.

**URL** : `/ingress/online`

**Method** : `GET`

**Data constraints** : `{}`

## Success Responses

**Code** : `200 OK`

**Body** :

```json
{
    "summary": "{campaign_id}"
}
```
**Example Response** : 

```json
{
    "summary": "6009ee8abd7c107d60a41733"
}
```

# Transmit Telemetry Data

Transmit telemetry data for campaign with the given token.

**URL** : `/ingress/{campaign_id}/`

**Method** : `PUT`

**Body** :
 
```json
{
	"content": "some content dummy tea"
}
```

## Success Responses

**Code** : `200 OK`

**Body** :

```json
{
    "summary": "Size of data added to campaign: {size}"
}
```

# End the campaign 

End the campaing of transmiting telemetry data for the given campaign id. 

**URL** : `/ingress/{campaign_id}/offline`

**Method** : `GET`

**Data constraints** : `{}`

## Success Responses

**Code** : `200 OK`







