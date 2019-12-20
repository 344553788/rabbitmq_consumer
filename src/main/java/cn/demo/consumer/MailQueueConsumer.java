package cn.demo.consumer;

import cn.demo.constant.MessageConstant;
import cn.demo.entity.MessageTemplate;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
@RabbitListener(bindings = @QueueBinding(
		value = @Queue(name = MessageConstant.QueueName.mail_queue, durable = Exchange.TRUE,
				autoDelete = MessageConstant.QueueName.mail_queue,
				arguments = {@Argument(name = "x-dead-letter-exchange", value = MessageConstant.ExchangeName.dead_letter_exchange),
						@Argument(name = "x-dead-letter-routing-key", value = MessageConstant.RouteName.mail_queue_fail)}
		),
		exchange = @Exchange(name = MessageConstant.ExchangeName.mail_exchange,
				type = ExchangeTypes.DIRECT, ignoreDeclarationExceptions = "true"),
		key = {MessageConstant.RouteName.mail_routing_key}))
public class MailQueueConsumer {
	
	@RabbitHandler
	public void handleMessage(String data, Message message, Channel channel) {
		Long deliveryTag = message.getMessageProperties().getDeliveryTag();
		try{
			MessageTemplate template = MessageTemplate.builder().content(data).build();
			log.info("MessageConstant.QueueName.mail_queue  handleMessage:::data={}, properties={}", template, message.getMessageProperties());
			channel.basicAck(deliveryTag, false);
		}catch (Exception e){
			try {
				channel.basicReject(deliveryTag,false);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
