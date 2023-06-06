package io.mosaic.demonstrator.devicecommunication.messaging;


import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;


@Component
public class MessageConsumer {
	
	//@RabbitListener(queues="${io.mosaic.demonstrator.input.queue}")
	@SendTo("replyQueue")
    public String receivedMessage(Message msg) {
        System.out.println("Got Message: " + msg);
        
        double val = 10;
        for (int i = 0; i < 100; i++) {
        	val = Math.atan(Math.sqrt(Math.pow(val , 10)));
		}        
 
       return Double.toString(val);
    }
	
	
}
