package cn.demo.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import cn.demo.constant.MessageConstant;
import cn.demo.entity.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * bindings = @QueueBinding(
        value = @Queue(name = MessageConstant.QueueName.DIRECT_QUEUE, durable = Exchange.TRUE,
        autoDelete = MessageConstant.QueueName.DIRECT_QUEUE,
        		arguments = {@Argument(name = "x-dead-letter-exchange", value = MessageConstant.ExchangeName.DELAY_EXCHANGE),
        			@Argument(name = "x-dead-letter-routing-key", value = MessageConstant.RouteName.DELAY_ROUTE) }
        ),
        exchange = @Exchange(name = MessageConstant.ExchangeName.DIRECT_EXCHANGE,
        type = ExchangeTypes.DIRECT,ignoreDeclarationExceptions = "true"),
        key = {MessageConstant.RouteName.DIRECT_ROUTE})
 * @author User
 *
 */
@Slf4j
@Component
@RabbitListener(queues = MessageConstant.QueueName.DIRECT_QUEUE)
public class MessageConsumer {
	
	@RabbitHandler
	public void handleMessage(byte[] payload, Message message, Channel channel) {
		Long deliveryTag = message.getMessageProperties().getDeliveryTag();
		String data = new String(payload);
		log.info("handleMessage:::payload={}, properties={}", data, message.getMessageProperties());
		try {
			channel.basicAck(deliveryTag, false);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	@RabbitHandler
	public void handleMessage(String data, Message message, Channel channel) {
		Long deliveryTag = message.getMessageProperties().getDeliveryTag();
		MessageTemplate template = MessageTemplate.builder().content(data).build();
		log.info("handleMessage:::data={}, properties={}", template, message.getMessageProperties());
		try {
			channel.basicAck(deliveryTag, false);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
