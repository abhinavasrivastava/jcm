package com.jiocloud.messages.controller;

import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.google.gson.Gson;
import com.jiocloud.messages.model.MessageUploadRequest;
import com.jiocloud.messages.rabbit.RabbitMqConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;


@RestController
@RequestMapping("/messages")
public class MessageController {
	
	Logger logger = LoggerFactory.getLogger(MessageController.class);
	Gson gson = new Gson();
	ResourceBundle rb = ResourceBundle.getBundle("jcm-app");
	
	@Autowired
	RabbitMqConnectionFactory rabbitMqConnectionFactory;
	
	private Producer<String, String> producer;
    private String topic;
	
    @PostConstruct
	public void initialize() {
		Properties props = new Properties();
		//props.put("bootstrap.servers", "172.24.36:2181,172.24.1.189:2181");
		props.put("bootstrap.servers", rb.getString("bootstrap.servers"));
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("acks", "all");
        producer = new KafkaProducer<String,String>(props);
        topic = rb.getString("jcm.message.topic");
  }
	
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public DeferredResult<String> uploadMesaages(@RequestBody MessageUploadRequest req) throws InterruptedException, ExecutionException{
		final DeferredResult<String>deferredResult = new DeferredResult<String>();
		Future<RecordMetadata> m = producer.send(new ProducerRecord(topic, gson.toJson(req)));
//		System.out.println("Message produced, offset: " + m.get().offset());
//		System.out.println("Message produced, partition : " + m.get().partition());
//		System.out.println("Message produced, topic: " + m.get().topic());
		deferredResult.setResult("message queued.");
		return deferredResult;
	}
	
	
	@RequestMapping(value="/upload2r", method = RequestMethod.POST)
	public DeferredResult<String> uploadMesaages2r(@RequestBody MessageUploadRequest req) throws InterruptedException, ExecutionException, IOException{
		final DeferredResult<String>deferredResult = new DeferredResult<String>();
		Channel channel = rabbitMqConnectionFactory.getChannel();
        String message = gson.toJson(req);
        channel.basicPublish("textmessagesexchange", "", null, message.getBytes());
		deferredResult.setResult("message queued.");
		return deferredResult;
	}

}
