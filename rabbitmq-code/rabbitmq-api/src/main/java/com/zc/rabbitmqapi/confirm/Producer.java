package com.zc.rabbitmqapi.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * 描述:
 *
 * @Author: zhangchao
 * @Date: 8/13/20 8:52 上午
 **/
public class Producer {

    public static void main(String[] args) throws Exception{
        //1.创建ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.1.112");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2.获取连接
        Connection connection = connectionFactory.newConnection();

        //3.通过连接创建channel
        Channel channel = connection.createChannel();

        //4.指定消息投递模式
        channel.confirmSelect();

        String exchangeName = "test_confirm_exchange";
        String routingKey = "confirm.save";

        //5.发送一条消息
        String msg = "Hello RabbitMQ Send confirm message!";
        channel.basicPublish(exchangeName,routingKey,null,msg.getBytes());

        channel.addConfirmListener(new ConfirmListener() {
            //成功
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.err.println("------- ack success-----------");
            }

            //失败
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.err.println("------- ack fail-----------");
            }
        });
    }
}
