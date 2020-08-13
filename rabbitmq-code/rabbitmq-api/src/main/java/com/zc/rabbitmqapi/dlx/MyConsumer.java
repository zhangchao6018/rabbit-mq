package com.zc.rabbitmqapi.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class MyConsumer extends DefaultConsumer {

	private Channel channel ;
	public MyConsumer(Channel channel) {
		super(channel);
		this.channel = channel;
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//		try { TimeUnit.SECONDS.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
		System.err.println("-----------consume message----------");
		System.err.println("consumerTag: " + consumerTag);
		System.err.println("envelope: " + envelope);
		System.err.println("properties: " + properties);
		System.err.println("body: " + new String(body));
		//拒绝消息->将会转发到配置好的死信队列中
//		channel.basicNack(envelope.getDeliveryTag(),false,false);
		System.out.println("消费端决绝了消息");
		channel.basicReject(envelope.getDeliveryTag(), false);
	}


}
