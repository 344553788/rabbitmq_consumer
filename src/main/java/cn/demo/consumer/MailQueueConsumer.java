package cn.demo.consumer;

<<<<<<< HEAD:src/main/java/cn/demo/consumer/MessageConsumer.java
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

=======
>>>>>>> 0b4d29d92ffe6b4a675f5b47203a11bcd318e4f2:src/main/java/cn/demo/consumer/MailQueueConsumer.java
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
<<<<<<< HEAD:src/main/java/cn/demo/consumer/MessageConsumer.java
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
			//可以通过给队列（Queue）绑定死信队列，使用nack反馈给mq，会将消息转发到死信队列里面，此种方式需要自己在消费消息的方法内部将异常处理掉
			//消息确认时使用nack，并且requeue参数传false
//			try {
//				channel.basicNack(deliveryTag, false, false);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
		}
	}

=======
		value = @Queue(name = MessageConstant.QueueName.mail_queue, durable = Exchange.TRUE,
				autoDelete = MessageConstant.QueueName.mail_queue,
				arguments = {@Argument(name = "x-dead-letter-exchange", value = MessageConstant.ExchangeName.dead_letter_exchange),
						@Argument(name = "x-dead-letter-routing-key", value = MessageConstant.RouteName.mail_queue_fail)}
		),
		exchange = @Exchange(name = MessageConstant.ExchangeName.mail_exchange,
				type = ExchangeTypes.DIRECT, ignoreDeclarationExceptions = "true"),
		key = {MessageConstant.RouteName.mail_routing_key}))
public class MailQueueConsumer {
	
>>>>>>> 0b4d29d92ffe6b4a675f5b47203a11bcd318e4f2:src/main/java/cn/demo/consumer/MailQueueConsumer.java
	@RabbitHandler
	public void handleMessage(@Payload String data, Message message, Channel channel){
		int dd = 1/0;
		System.out.print(dd);
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
