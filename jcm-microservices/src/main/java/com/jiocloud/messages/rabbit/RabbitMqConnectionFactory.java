package com.jiocloud.messages.rabbit;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Component;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@Component
public class RabbitMqConnectionFactory {
	
	ConnectionFactory factory;
	Connection connection;
	Channel channel;
	ResourceBundle rb = ResourceBundle.getBundle("jcm-app");
	
	public RabbitMqConnectionFactory() throws IOException, TimeoutException{
		factory = new ConnectionFactory();
        //factory.setHost("localhost");
		String[] hosts = rb.getString("rabbitmq.servers").split(",");
		Address[] addresses = {new Address(hosts[0], 5672), new
				Address(hosts[1], 5672)};
		connection = factory.newConnection(addresses);
	    channel = connection.createChannel();
        channel.exchangeDeclare("textmessagesexchange", "direct");
        channel.queueBind("textmessages", "textmessagesexchange", "textmessagekey");
	}
	
	public Connection getConnection(){
		return connection;
	}

	public Channel getChannel(){
		return channel;
	}
}
