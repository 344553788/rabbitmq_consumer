package cn.demo.config;

import cn.demo.constant.MessageConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	@Bean
	public DirectExchange deadLetterExchange() {
		return new DirectExchange(MessageConstant.ExchangeName.dead_letter_exchange, true, false);
	}

	@Bean
	Queue deadLetterQueue() {
		return QueueBuilder.durable(MessageConstant.QueueName.dead_letter_queue)
				.deadLetterExchange(MessageConstant.ExchangeName.dead_letter_exchange) // DLX
				.deadLetterRoutingKey(MessageConstant.RouteName.mail_queue_fail) // dead letter携带的routing key
				.build();
	}

	@Bean
	public Binding deadLetterBinding() {
		return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(MessageConstant.RouteName.mail_routing_key);
	}


	@Bean
	public TopicExchange messageFailExchange() {
		return new TopicExchange(MessageConstant.ExchangeName.message_dead_letter_exchange, true, false);
	}

	@Bean
	Queue messageFailQueue() {
		return QueueBuilder.durable(MessageConstant.QueueName.message_fail)
				.deadLetterExchange(MessageConstant.ExchangeName.message_dead_letter_exchange) // DLX
				.deadLetterRoutingKey(MessageConstant.RouteName.message_fail) // dead letter携带的routing key
				.build();
	}

	@Bean
	public Binding messageFailBinding() {
		return BindingBuilder.bind(messageFailQueue()).to(messageFailExchange()).with(MessageConstant.RouteName.mail_queue_fail);
	}
}
