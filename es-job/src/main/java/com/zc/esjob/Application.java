package com.zc.esjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.zc.rabbit.task.annotation.EnableElasticJob;

@EnableElasticJob
@SpringBootApplication
@ComponentScan(basePackages = {"com.zc.esjob.*","com.zc.esjob.service.*", "com.zc.esjob.annotation.*","com.zc.esjob.task.*"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
