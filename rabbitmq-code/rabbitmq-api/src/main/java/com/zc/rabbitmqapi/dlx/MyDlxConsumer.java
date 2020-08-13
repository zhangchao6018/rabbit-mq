package com.zc.rabbitmqapi.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class MyDlxConsumer extends DefaultConsumer {


	public MyDlxConsumer(Channel channel) {
		super(channel);
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
		System.err.println("-----------dlx msg ----------");
//		System.err.println("consumerTag: " + consumerTag);
//		System.err.println("envelope: " + envelope);
//		System.err.println("properties: " + properties);
//		System.err.println("body: " + new String(body));

		//todo 收到死信消息  进行下一步处理
		System.out.println("收到死信消息  进行下一步处理...");

	}


}
