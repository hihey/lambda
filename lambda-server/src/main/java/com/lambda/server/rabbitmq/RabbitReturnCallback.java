package com.lambda.server.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitReturnCallback implements RabbitTemplate.ReturnCallback {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
     * 发送到队列失败后回调
     * 消息可以发送到相应交换机，但是没有相应路由键和队列绑定
     * @param message
     * @param i
     * @param s
     * @param s1
     * @param s2
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
    	log.info("sender return success" + message.toString() + "===" + replyCode + "===" + replyText + "===" + exchange);
    }
}