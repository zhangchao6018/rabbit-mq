package com.zc.rabbit.common.serializer.impl;

import com.zc.rabbit.api.Message;
import com.zc.rabbit.common.serializer.Serializer;
import com.zc.rabbit.common.serializer.SerializerFactory;

public class JacksonSerializerFactory implements SerializerFactory {

	public static final SerializerFactory INSTANCE = new JacksonSerializerFactory();
	
	@Override
	public Serializer create() {
		return JacksonSerializer.createParametricType(Message.class);
	}

}
