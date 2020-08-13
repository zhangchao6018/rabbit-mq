package com.zc.rabbitmqapi.limit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MyConsumer extends DefaultConsumer {


	private Channel channel ;
	
	public MyConsumer(Channel channel) {
		super(channel);
		this.channel = channel;
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
		System.err.println("-----------consume message----------");
		System.err.println("consumerTag: " + consumerTag);
		System.err.println("envelope: " + envelope);
		System.err.println("properties: " + properties);
		System.err.println("body: " + new String(body));

		try { TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}

		//手动签收
		//multiple 跟consumer设置的channel.basicQos(0, 1, false);  第二个参数相互配合生效
		channel.basicAck(envelope.getDeliveryTag(), false);
//		channel.basicNack();
	}


}
