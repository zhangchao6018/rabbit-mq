package com.zc.esjob.service;

import com.zc.esjob.annotation.JobTrace;
import org.springframework.stereotype.Service;

@Service
public class IndexService {

	@JobTrace
	public void tester(String name) {
		System.err.println("name: " + name);
	}
}
