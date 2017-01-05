package com.jiocloud.messages.thread;

import java.util.concurrent.Callable;

import com.jiocloud.messages.rabbit.RabbitMqConnectionFactory;
import com.rabbitmq.client.Channel;

public class PublishTask implements Callable<String>{
	
	RabbitMqConnectionFactory rabbitMqConnectionFactory;
	//String message;
	byte[]bytes;
	
	public PublishTask(RabbitMqConnectionFactory rabbitMqConnectionFactory, byte[]bytes){
		this.rabbitMqConnectionFactory = rabbitMqConnectionFactory;
		this.bytes = bytes;
	}

	@Override
	public String call() throws Exception {
		Channel channel = rabbitMqConnectionFactory.getChannel();
		//channel.basicPublish("textmessagesexchange", "textmessagekey", MessageProperties.MINIMAL_PERSISTENT_BASIC, message.getBytes());
		channel.basicPublish("textmessagesexchange", "textmessagekey", null, bytes);
		return null;
	}

}


