package com.zc.esjob.config;

import org.springframework.context.annotation.Bean;

import com.zc.esjob.annotation.JobTraceInterceptor;

//@Configuration
public class TraceJobConfiguration {

	@Bean
	public JobTraceInterceptor jobTraceInterceptor() {
		System.err.println("init --------------->");
		return new JobTraceInterceptor();
	}
	
}
