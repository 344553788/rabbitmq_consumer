package cn.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.demo.constant.MessageConstant;

@Configuration
public class RabbitConfig {

	/********************* 测试交换机 *****************/

	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange(MessageConstant.ExchangeName.DIRECT_EXCHANGE, true, false);
	}

//	@Bean
//	public FanoutExchange fanoutExchange() {
//		return new FanoutExchange(MessageConstant.ExchangeName.FANOUT_EXCHANGE, true, false);
//	}
//
//	@Bean
//	public TopicExchange topicExchange() {
//		return new TopicExchange(MessageConstant.ExchangeName.TOPIC_EXCHANGE, true, false);
//	}

	/**
	 * 死信队列跟交换机类型没有关系 不一定为directExchange 不影响该类型交换机的特性.
	 */
	@Bean
	public DirectExchange deadLetterExchange() {
		return new DirectExchange(MessageConstant.ExchangeName.DELAY_EXCHANGE, true, false);
	}

	/********************* 测试队列 *****************/

//	@Bean
//	public Queue fanoutQueue() {
//		Queue fanoutQueue = QueueBuilder.durable(MessageConstant.QueueName.FANOUT_QUEUE).build();
//		return fanoutQueue;
//	}
//
//	@Bean
//	public Queue topicQueue() {
//		Queue topicQueue = QueueBuilder.durable(MessageConstant.QueueName.TOPIC_QUEUE).build();
//		return topicQueue;
//	}

	/**
	 * 声明一个死信队列. x-dead-letter-exchange 对应 死信交换机 x-dead-letter-routing-key 对应死信路由建
	 */
	@Bean
	Queue deadLetterQueue() {
		return QueueBuilder.durable(MessageConstant.QueueName.DELAY_QUEUE)
				.deadLetterExchange(MessageConstant.ExchangeName.DELAY_EXCHANGE) // DLX
				.deadLetterRoutingKey(MessageConstant.RouteName.DELAY_ROUTE) // dead letter携带的routing key
				.build();
	}

	/**
	 * 定义死信队列转发队列.
	 */
	@Bean
	public Queue directQueue() {
		// 消费失败的信息加入死信
		Queue directQueue = QueueBuilder.durable(MessageConstant.QueueName.DIRECT_QUEUE)
				.deadLetterExchange(MessageConstant.ExchangeName.DELAY_EXCHANGE)
				.deadLetterRoutingKey(MessageConstant.RouteName.DELAY_ROUTE)
				.build();
		return directQueue;
	}

	// 广播跟route-key 无关
//	@Bean
//	public Binding fanoutBinding() {
//		return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange());
//	}
//
//	@Bean
//	public Binding topicBinding() {
//		return BindingBuilder.bind(topicQueue()).to(topicExchange()).with(MessageConstant.RouteName.TOPIC_ROUTE);
//	}

	@Bean
	public Binding directBinding() {
		return BindingBuilder.bind(directQueue()).to(directExchange()).with(MessageConstant.RouteName.DIRECT_ROUTE);
	}

	/**
	 * 死信路由通过 DELAY_ROUTE 绑定键绑定到死信队列上.
	 */
	@Bean
	Binding dlxBinding() {
		return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange())
				.with(MessageConstant.RouteName.DELAY_ROUTE);
	}
}
