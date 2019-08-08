package com.lambda.server.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
	
	public static final String LAMBDA_EXCHANGE = "COM.LAMBDA.EXCHANGE";
	public static final String LAMBDA_QUEUE = "COM.LAMBDA.QUEUE";
	
	/**
	 * 设置持久化交换机
	 * durable: 
	 * autoDelete:
	 * @return
	 */
	@Bean
	public TopicExchange getExchange() {
		return new TopicExchange(LAMBDA_EXCHANGE, true, false);
	}

	/**
	 * 设置持久化topic模式队列 
	 * durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
	 * exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
	 * autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
	 * @return
	 */
	@Bean
	public Queue getQueue() {
		return new Queue(LAMBDA_QUEUE, true, false, false);
	}
	
	/**
	 * 绑定exchange queue routekey.
	 * @param queueMessage 队列
	 * @param exchange     交换机
	 * @param routekey     路由
	 * @return
	 */
	public Binding bindingExchange(Queue queueMessage, TopicExchange exchange, String routekey) {
		return BindingBuilder.bind(queueMessage).to(exchange).with(routekey);
	}

	/**
	 * 绑定队列与exchange.
	 * @return
	 */
	@Bean
	public Binding agencyChangeBinding() {
		return bindingExchange(getQueue(),getExchange(),RabbitConfig.LAMBDA_QUEUE);
	}
}