package com.zc.rabbitmqapi.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * 描述:
 * 生产端代码
 * @Author: zhangchao
 * @Date: 8/12/20 7:38 下午
 **/
public class Procuder {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //1 创建一个ConnectionFactory, 并进行配置
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.112");
        factory.setPort(5672);
        //默认
        factory.setVirtualHost("/");

        //2 通过连接工厂创建连接
        Connection connection = factory.newConnection();

        //3 通过connection创建一个Channel
        Channel channel = connection.createChannel();

        //4 通过Channel发送数据
        for (int i = 0; i < 5; i++) {
            String msg = "Hello RabbitMQ!";
            //1 exchange   2 routingKey  未指定exchange,会拿routingKey匹配queue名称
            channel.basicPublish("", "test001", null, msg.getBytes());
        }

        //5 记得要关闭相关的连接
        channel.close();
        connection.close();
    }
}
