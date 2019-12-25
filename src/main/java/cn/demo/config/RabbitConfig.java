package cn.demo.config;

import cn.demo.constant.MessageConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
	
	
	/********************* 测试队列 *****************/

	/**
	 * 定义转发到死信队列的队列
	 */
//	@Bean
//	Queue directQueue() {
//		// 消费失败的信息加入死信
//		Queue directQueue = QueueBuilder.durable(MessageConstant.Queue.DIRECT_QUEUE)
//				.deadLetterExchange(MessageConstant.Exchange.DLX_EXCHANGE)
//				.deadLetterRoutingKey(MessageConstant.Route.DLX_ROUTE)
//				.build();
//		return directQueue;
//	}
	
//	@Bean
//	public Queue fanoutQueue() {
//		Queue fanoutQueue = QueueBuilder.durable(MessageConstant.Queue.FANOUT_QUEUE).build();
//		return fanoutQueue;
//	}
//
//	@Bean
//	public Queue topicQueue() {
//		Queue topicQueue = QueueBuilder.durable(MessageConstant.Queue.TOPIC_QUEUE).build();
//		return topicQueue;
//	}


	
	
	/********************* 测试交换机 *****************/
	
//	@Bean
//	DirectExchange directExchange() {
//		return new DirectExchange(MessageConstant.Exchange.DIRECT_EXCHANGE, true, false);
//	}

//	@Bean
//	public FanoutExchange fanoutExchange() {
//		return new FanoutExchange(MessageConstant.Exchange.FANOUT_EXCHANGE, true, false);
//	}
//
//	@Bean
//	public TopicExchange topicExchange() {
//		return new TopicExchange(MessageConstant.Exchange.TOPIC_EXCHANGE, true, false);
//	}



//	@Bean
//	Binding directBinding() {
//		return BindingBuilder.bind(directQueue()).to(directExchange()).with(MessageConstant.Route.DIRECT_ROUTE);
//	}
	
	
	/**
	 * 声明一个死信队列
	 */
	@Bean
	Queue deadLetterQueue() {
		return QueueBuilder.durable(MessageConstant.Queue.DLX_QUEUE)
				.deadLetterExchange(MessageConstant.Exchange.DLX_EXCHANGE)
				.deadLetterRoutingKey(MessageConstant.Route.DLX_ROUTE)
				.build();
	}
	
	/**
	 * 死信队列跟交换机类型没有关系 不一定为directExchange 不影响该类型交换机的特性.
	 */
	@Bean
	DirectExchange deadLetterExchange() {
		return new DirectExchange(MessageConstant.Exchange.DLX_EXCHANGE, true, false);
	}


	@Bean
	Binding dlxBinding() {
		return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange())
				.with(MessageConstant.Route.DLX_ROUTE);
	}
	
	
	/**
	 * 可以配置MessageRecoverer对异常消息进行处理，此处理会在listener.retry次数尝试完并还是抛出异常的情况下才会调用，默认有两个实现：
	 * RepublishMessageRecoverer：将消息重新发送到指定队列，需手动配置，如：
	 * RejectAndDontRequeueRecoverer：如果不手动配置MessageRecoverer，会默认使用这个，实现仅仅是将异常打印抛出
	 * @param rabbitTemplate
	 * @return
	 */
//	@Bean
//	public MessageRecoverer messageRecoverer(@Qualifier("rabbitTemplate") RabbitTemplate rabbitTemplate){
//	    return new RepublishMessageRecoverer(rabbitTemplate,MessageConstant.Exchange.DLX_EXCHANGE , MessageConstant.Route.DLX_ROUTE);
//	}
//	
//	@Bean
//	public  RejectAndDontRequeueRecoverer rejectRecover()  {
//		return new RejectAndDontRequeueRecoverer();
//	};
}
