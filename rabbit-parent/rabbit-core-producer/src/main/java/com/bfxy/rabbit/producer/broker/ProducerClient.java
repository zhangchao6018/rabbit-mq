package com.bfxy.rabbit.producer.broker;

import com.bfxy.rabbit.api.Message;
import com.bfxy.rabbit.api.MessageProducer;
import com.bfxy.rabbit.api.MessageType;
import com.bfxy.rabbit.api.SendCallback;
import com.bfxy.rabbit.api.exception.MessageRunTimeException;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 	$ProducerClient 发送消息的实际实现类
 * @author Alienware
 *
 */
@Component
public class ProducerClient implements MessageProducer {

	@Autowired
	private RabbitBroker rabbitBroker;
	
	@Override
	public void send(Message message) throws MessageRunTimeException {
		Preconditions.checkNotNull(message.getTopic());
		String messageType = message.getMessageType();
		switch (messageType) {
			case MessageType.RAPID:
				//直接发送,不确认
				rabbitBroker.rapidSend(message);
				break;
			case MessageType.CONFIRM:
				//确认消息
				rabbitBroker.confirmSend(message);
				break;
			case MessageType.RELIANT:
				//可靠性消息,消息记录持久化
				rabbitBroker.reliantSend(message);
				break;
		default:
			break;
		}
	}

	/**
	 * 	$send Messagetype
	 */
	@Override
	public void send(List<Message> messages) throws MessageRunTimeException {
		messages.forEach( message -> {
			message.setMessageType(MessageType.RAPID);
			MessageHolder.add(message);
		});
		rabbitBroker.sendMessages();
	}
	
	@Override
	public void send(Message message, SendCallback sendCallback) throws MessageRunTimeException {
		// TODO Auto-generated method stub
		
	}

}
