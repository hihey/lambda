package com.lambda.server.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitConfirmCallback implements RabbitTemplate.ConfirmCallback{
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
    /**
     * 发送到交换机上失败回调
     * 消息发送回调(判断是否发送到相应的交换机上)
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("消息发送到exchange成功");
        } else {
            log.info("消息发送到exchange失败");
        }
    }
}