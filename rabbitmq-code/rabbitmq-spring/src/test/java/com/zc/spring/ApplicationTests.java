package com.zc.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zc.spring.entity.Order;
import com.zc.spring.entity.Packaged;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import static java.util.concurrent.TimeUnit.SECONDS;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Test
	public void contextLoads() {
	}
	
	@Autowired
	private RabbitAdmin rabbitAdmin;
	
	@Test
	public void testAdmin() throws Exception {
		rabbitAdmin.declareExchange(new DirectExchange("test.direct", false, false));
		
		rabbitAdmin.declareExchange(new TopicExchange("test.topic", false, false));
		
		rabbitAdmin.declareExchange(new FanoutExchange("test.fanout", false, false));
		
		rabbitAdmin.declareQueue(new Queue("test.direct.queue", false));
		
		rabbitAdmin.declareQueue(new Queue("test.topic.queue", false));
		
		rabbitAdmin.declareQueue(new Queue("test.fanout.queue", false));
		
		rabbitAdmin.declareBinding(new Binding("test.direct.queue",
				Binding.DestinationType.QUEUE,
				"test.direct", "direct", new HashMap<>()));
		
		rabbitAdmin.declareBinding(
				BindingBuilder
				.bind(new Queue("test.topic.queue", false))		//直接创建队列
				.to(new TopicExchange("test.topic", false, false))	//直接创建交换机 建立关联关系
				.with("user.#"));	//指定路由Key
		
		
		rabbitAdmin.declareBinding(
				BindingBuilder
				.bind(new Queue("test.fanout.queue", false))		
				.to(new FanoutExchange("test.fanout", false, false)));
		
		//清空队列数据
		rabbitAdmin.purgeQueue("test.topic.queue", false);
	}

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Test
	public void testSendMsg() throws Exception {
		//1.创建消息
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.getHeaders().put("orderId",Math.random()*10000);
		messageProperties.getHeaders().put("time",new Date());
		Message message = new Message(("order" + (new Random().nextInt(1000))).getBytes(), messageProperties);
		rabbitTemplate.convertAndSend("topic001", "spring.order", message, new MessagePostProcessor() {
			//类似于拦截器,作用于真正发送前,对消息进行再次处理
			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				System.out.println("MessagePostProcessor 额外配置");
				message.getMessageProperties().getHeaders().put("sessionId","sys");
				return message;
			}
		});
	}

	@Test
	public void testSendMsg2() throws Exception {
		//1 创建消息
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentType("text/plain");
		Message message = new Message("mq 消息1234".getBytes(), messageProperties);

		rabbitTemplate.send("topic001", "spring.abc", message);

		rabbitTemplate.convertAndSend("topic001", "spring.amqp", "hello object message send!");
		rabbitTemplate.convertAndSend("topic002", "rabbit.abc", "hello object message send!");
	}

	@Test









	public void testSendMessage4Text() throws Exception {
		//1 创建消息
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentType("text/plain");
		Message message = new Message("mq 消息1234".getBytes(), messageProperties);

		//以下注释的两行对应自定义 队列-方法配置为->
//		queueOrTagToMethodName.put("queue001", "method1");
//		queueOrTagToMethodName.put("queue003", "method2");
//		rabbitTemplate.send("topic001", "spring.abc", message);
//		rabbitTemplate.send("topic001", "mq.abc", message);

		rabbitTemplate.send("topic001", "spring.abc", message);
		rabbitTemplate.send("topic002", "rabbit.abc", message);
		try { SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}

	}

	//测试MessageConverter

	@Test
	public void testSendJsonMessage() throws Exception {

		Order order = new Order();
		order.setId("001");
		order.setName("消息订单");
		order.setContent("描述信息");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(order);
		System.err.println("order 4 json: " + json);

		MessageProperties messageProperties = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties.setContentType("application/json");
		Message message = new Message(json.getBytes(), messageProperties);

		rabbitTemplate.send("topic001", "spring.order", message);
		try { SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
	}

	@Test
	public void testSendJavaMessage() throws Exception {

		Order order = new Order();
		order.setId("001");
		order.setName("订单消息");
		order.setContent("订单描述信息");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(order);
		System.err.println("order 4 json: " + json);

		MessageProperties messageProperties = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties.setContentType("application/json");
		//key是固定写法
		messageProperties.getHeaders().put("__TypeId__", "com.zc.spring.entity.Order");
		Message message = new Message(json.getBytes(), messageProperties);

		rabbitTemplate.send("topic001", "spring.order", message);
		try { SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
	}

	@Test
	public void testSendMappingMessage() throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		Order order = new Order();
		order.setId("001");
		order.setName("订单消息");
		order.setContent("订单描述信息");

		String json1 = mapper.writeValueAsString(order);
		System.err.println("order 4 json: " + json1);

		MessageProperties messageProperties1 = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties1.setContentType("application/json");
		messageProperties1.getHeaders().put("__TypeId__", "order");
		Message message1 = new Message(json1.getBytes(), messageProperties1);
		rabbitTemplate.send("topic001", "spring.order", message1);

		Packaged pack = new Packaged();
		pack.setId("002");
		pack.setName("包裹消息");
		pack.setDescription("包裹描述信息");

		String json2 = mapper.writeValueAsString(pack);
		System.err.println("pack 4 json: " + json2);

		MessageProperties messageProperties2 = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties2.setContentType("application/json");
		messageProperties2.getHeaders().put("__TypeId__", "packaged");
		Message message2 = new Message(json2.getBytes(), messageProperties2);
		rabbitTemplate.send("topic001", "spring.pack", message2);

		try { SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}

	}

	@Test
	public void testSendExtConverterMessage() throws Exception {
			byte[] body = Files.readAllBytes(Paths.get("/Users/zhangchao/Desktop/temp", "159722198597.png"));
			MessageProperties messageProperties = new MessageProperties();
			messageProperties.setContentType("image/png");
			messageProperties.getHeaders().put("extName", "png");
			Message message = new Message(body, messageProperties);
			rabbitTemplate.send("", "image_queue", message);

//		byte[] body = Files.readAllBytes(Paths.get("d:/002_books", "mysql.pdf"));
//		MessageProperties messageProperties = new MessageProperties();
//		messageProperties.setContentType("application/pdf");
//		Message message = new Message(body, messageProperties);
//		rabbitTemplate.send("", "pdf_queue", message);
	}

}
