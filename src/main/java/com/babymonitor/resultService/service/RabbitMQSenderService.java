package com.babymonitor.resultService.service;

import com.babymonitor.resultService.config.RabbitMQConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnExpression("!'${spring.rabbitmq.host}'.isEmpty()")
public class RabbitMQSenderService {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQSenderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

//    public void sendParticipantAction(ParticipantAction action) {
//        System.out.println("Sending participant action: " + action);
//        rabbitTemplate.convertAndSend(RabbitMQConfig.MATLAB_QUEUE, action);
//    }

    public void sendParticipantAction(/*ParticipantAction action*/) {
        //String routingKey = "lobby.participantAction"; // Ensure this matches the listener's binding
        //System.out.println("Sending participant action: " + action + " with routing key: " + routingKey);

        // Send to the correct exchange name
        //rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE_NAME, routingKey, action);
    }
}
