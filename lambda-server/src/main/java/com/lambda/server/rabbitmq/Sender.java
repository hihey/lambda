package com.lambda.server.rabbitmq;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender{

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	/**
     * 定制rabbitMQ模板
     * ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收消息成功回调
     * ReturnCallback接口用于实现消息发送到RabbitMQ交换器，但无相应队列与交换器绑定时的回调  即消息发送不到任何一个队列中回调
     * @return
     */
    @PostConstruct
    public void initRabbitTemplate(){
        rabbitTemplate.setConfirmCallback(new RabbitConfirmCallback());
        rabbitTemplate.setReturnCallback(new RabbitReturnCallback());
    }

    public void send(String msg) {
		rabbitTemplate.convertAndSend(RabbitConfig.LAMBDA_EXCHANGE, RabbitConfig.LAMBDA_QUEUE, msg);
    }
}