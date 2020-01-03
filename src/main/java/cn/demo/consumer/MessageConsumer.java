package cn.demo.consumer;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import cn.demo.constant.MessageConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author User
 *
 */
@Slf4j
@Component
@RabbitListener(bindings = @QueueBinding(
		exchange = @Exchange(value = MessageConstant.Exchange.DIRECT_EXCHANGE, type = ExchangeTypes.DIRECT, durable = Exchange.TRUE, autoDelete = Exchange.FALSE),
value = @Queue(value = MessageConstant.Queue.DIRECT_QUEUE, durable = "true", autoDelete = "false", arguments = {
		@Argument(name = "x-dead-letter-exchange", value = MessageConstant.Exchange.DLX_EXCHANGE),
		@Argument(name = "x-dead-letter-routing-key", value = MessageConstant.Route.DLX_ROUTE) }), 
key = MessageConstant.Route.DIRECT_ROUTE))
public class MessageConsumer {

	@RabbitHandler
	public void handleMessage(@Payload byte[] payload, Message message, Channel channel) {
		Long deliveryTag = message.getMessageProperties().getDeliveryTag();
		String data = new String(payload);
		log.info("handleMessage:::payload={}, properties={}", data, message.getMessageProperties());
		try {
			channel.basicAck(deliveryTag, false);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
