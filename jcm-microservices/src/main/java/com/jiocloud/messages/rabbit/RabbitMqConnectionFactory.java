package com.jiocloud.messages.rabbit;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@Component
public class RabbitMqConnectionFactory {
	
	ConnectionFactory factory;
	Connection connection;
	Channel channel;
	ResourceBundle rb = ResourceBundle.getBundle("jcm-app");
	
	public RabbitMqConnectionFactory() throws IOException, TimeoutException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException{
		factory = new ConnectionFactory();
		//factory.useNio();
		//factory.setNioParams(new NioParams().setNbIoThreads(4));
		factory.setUri("amqp://test:test@172.24.1.36");
		//factory.setHost("localhost");
		//String[] hosts = rb.getString("rabbitmq.servers").split(",");
//		Address[] addresses = {new Address(hosts[0], 5672), new
//				Address(hosts[1], 5672)};
//		connection = factory.newConnection(addresses);
		connection = factory.newConnection();
	    channel = connection.createChannel();
        channel.exchangeDeclare("textmessagesexchange", "direct", true);
        channel.queueDeclare("textmessages", true, false, false, null);
        channel.queueBind("textmessages", "textmessagesexchange", "textmessagekey");
	}
	
	public Connection getConnection(){
		return connection;
	}

	public Channel getChannel(){
		return channel;
	}
	
//	public Channel getNewChannel() throws IOException{
//		Channel channel = connection.createChannel();
//        channel.exchangeDeclare("textmessagesexchange", "direct", true);
//        channel.queueBind("textmessages", "textmessagesexchange", "textmessagekey");
//		return channel;
//	}
}
