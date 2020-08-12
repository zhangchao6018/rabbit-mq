package com.zc.rabbit.producer.autoconfigure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.zc.rabbit.task.annotation.EnableElasticJob;

/**
 * 	$RabbitProducerAutoConfiguration 自动装配 
 *
 */
@EnableElasticJob
@Configuration
@ComponentScan({"com.zc.rabbit.producer.*"})
public class RabbitProducerAutoConfiguration {


}
