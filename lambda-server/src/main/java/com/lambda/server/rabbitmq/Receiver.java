package com.lambda.server.rabbitmq;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

@Component
public class Receiver {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private static AtomicLong counter = new AtomicLong(0);
    public static long addOne() {
        return counter.incrementAndGet();
    }

	/**
	 * 接收消息
	 * @param msg
	 * @param channel
	 * @param message
	 * @throws IOException
	 */
	@RabbitHandler
	@RabbitListener(queues = RabbitConfig.LAMBDA_QUEUE)
	public void process(String msg,Channel channel,Message message) throws IOException {
		try {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);//确认消息
		} catch (IOException e) {
			log.error("process|exception|{}",new String(message.getBody()));
			if(addOne() < 2){
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);//否认消息（重入队列）
            }else {
            	counter = new AtomicLong(0);
            	channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);//拒绝消息
            }
		}
		System.err.println(new String(message.getBody()));
		log.info("Receiver|{}",msg);
	}
}