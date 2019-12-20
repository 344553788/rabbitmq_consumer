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
 *
 * @author User
 *
 */
@Slf4j
@Component
@RabbitListener(bindings = @QueueBinding(
		value = @Queue(name = MessageConstant.QueueName.message_queue, durable = Exchange.TRUE,
				autoDelete = MessageConstant.QueueName.message_queue,
				arguments = {@Argument(name = "x-dead-letter-exchange", value = MessageConstant.ExchangeName.message_dead_letter_exchange),
						@Argument(name = "x-dead-letter-routing-key", value = MessageConstant.RouteName.message_fail)}
		),
		exchange = @Exchange(name = MessageConstant.ExchangeName.message_exchange,
				type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"),
		key = {MessageConstant.RouteName.message_route}))
public class TopicMailQueueConsumer {



	@RabbitHandler
	public void handleMessage(String data, Message message, Channel channel) {
		Long deliveryTag = message.getMessageProperties().getDeliveryTag();
		try{
			MessageTemplate template = MessageTemplate.builder().content(data).build();
			log.info("MessageConstant.QueueName.message_queue  handleMessage:::data={}, properties={}", template, message.getMessageProperties());
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
