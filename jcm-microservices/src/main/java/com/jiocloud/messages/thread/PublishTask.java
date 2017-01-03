package com.jiocloud.messages.thread;

import java.util.concurrent.Callable;

import com.jiocloud.messages.rabbit.RabbitMqConnectionFactory;
import com.rabbitmq.client.Channel;

public class PublishTask implements Callable<String>{
	
	RabbitMqConnectionFactory rabbitMqConnectionFactory;
	String message;
	
	public PublishTask(RabbitMqConnectionFactory rabbitMqConnectionFactory, String message){
		this.rabbitMqConnectionFactory = rabbitMqConnectionFactory;
		this.message = message;
	}

	@Override
	public String call() throws Exception {
		//Channel channel = rabbitMqConnectionFactory.getChannel();
		Channel channel = rabbitMqConnectionFactory.getNewChannel();
		
		//channel.basicPublish("textmessagesexchange", "textmessagekey", MessageProperties.MINIMAL_PERSISTENT_BASIC, message.getBytes());
		channel.basicPublish("textmessagesexchange", "textmessagekey", null, message.getBytes());
		return null;
	}

}
