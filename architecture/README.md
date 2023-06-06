# about

Collection of architecture documentation and models

# draft architecture

![planned architecture](IoT-Tracker.png?raw=true "Architecture")

# technical architecture

## Broker
* RabbitMQ (amqp 0.9.1), v3.8 

## Services 
* Spring Boot
** **RabbitTemplate** (https://spring.io/guides/gs/messaging-rabbitmq/)
** JmsListener 

* Two rest interfaces: 
** Ingress
** Data Provider

* Options for REST
  - REST service 
  - **Spring MVC (the initial choice)**
  - Spring Webflux (more reactive, if you want to have back pressure and other. )
  - Spring WS (more for SOAP-based services)
  <br>
* Underlying technology
  - netty 
  - jetty 
  - tomcat (slow)
  - **undertow**
  <br>
* OpenAPI specification? 
  - springfox 
  <br>
* Persistance service
  - MongoDB 
  - spring data mongodb
  <br>
* Define DTOs? 
  - good idea at least for the messages
  <br>
* what technology?
  - jaxb 
  - **json-b (jackson**, johnzon, gson)
  - json-p 
  <br>
* mapper for DTOs (maven support, best integrated with spring)
  - mapstruct (last release sept 2020)
  - **modelmapper** 
  - dozer (last release 2014)
  <br>
    
## Gitlab image registry
We use a k8s secret to configure the private gitlab image registry. See also the tutorial 
[gitlab-registry](https://chris-vermeulen.com/using-gitlab-registry-with-kubernetes/).