package com.jiocloud.messages.controller;

import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

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

import com.datastax.driver.core.ResultSet;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.jiocloud.messages.dao.MessageDaoImpl;
import com.jiocloud.messages.model.MessageUploadRequest;
import com.jiocloud.messages.rabbit.RabbitMqConnectionFactory;
import com.jiocloud.messages.service.MessageUploadServiceImpl;
import com.jiocloud.messages.thread.AsyncSaveMessageTask;
import com.jiocloud.messages.thread.JCMExecutorService;
import com.jiocloud.messages.thread.SaveMessageTask;
import com.rabbitmq.client.AMQP.BasicProperties;


@RestController
@RequestMapping("/messages")
public class MessageController {
	
	Logger logger = LoggerFactory.getLogger(MessageController.class);
	Gson gson = new Gson();
	ResourceBundle rb = ResourceBundle.getBundle("jcm-app");
	
	@Autowired
	RabbitMqConnectionFactory rabbitMqConnectionFactory;
	
	@Autowired
	JCMExecutorService jCMExecutorService;
	
	@Autowired
	MessageUploadServiceImpl messageUploadServiceImpl;
	
	@Autowired
	MessageDaoImpl messageDaoImpl;
	
	private Producer<String, String> producer;
    private String topic;
    BasicProperties rProps;
    
    
	
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
	
	
//	@RequestMapping(value="/upload2r", method = RequestMethod.POST)
//	public DeferredResult<String> uploadMesaages2r(@RequestBody MessageUploadRequest req) throws InterruptedException, ExecutionException, IOException{
//		final DeferredResult<String>deferredResult = new DeferredResult<String>();
//		Channel channel = rabbitMqConnectionFactory.getChannel();
//        String message = gson.toJson(req);
//        channel.basicPublish("textmessagesexchange", "", null, message.getBytes());
//		deferredResult.setResult("message queued.");
//		return deferredResult;
//	}
	
	@RequestMapping(value="/upload2r", method = RequestMethod.POST)
	public String uploadMesaages2r(/* @RequestBody MessageUploadRequest req, */ HttpServletRequest request) throws Exception{
//		Channel channel = rabbitMqConnectionFactory.getChannel();
		//String message = org.apache.commons.io.IOUtils.toString( request.getInputStream());
		byte[] bytes = org.apache.commons.io.IOUtils.toByteArray( request.getInputStream());
          //String message = gson.toJson(req);
//        channel.basicPublish("textmessagesexchange", "textmessagekey", MessageProperties.MINIMAL_PERSISTENT_BASIC, message.getBytes());
          //jCMExecutorService.submit(new PublishTask(rabbitMqConnectionFactory, bytes));
		return "message queued.";
	}
	
	
	@RequestMapping(value="/syncupload2c", method = RequestMethod.POST)
	public ResultSet syncupload2c(@RequestBody MessageUploadRequest req) throws Exception{
		return messageUploadServiceImpl.saveMessages(req);
	
		//return "message queued";
	}
	
	@RequestMapping(value="/asyncupload2c", method = RequestMethod.POST)
	public String asyncupload2c(@RequestBody MessageUploadRequest req) throws Exception{
		messageUploadServiceImpl.saveAsyncMessages(req);
	
		return "message queued";
	}
	
	@RequestMapping(value="/asyncsyncupload2c", method = RequestMethod.POST)
	public String asyncsyncupload2c(@RequestBody MessageUploadRequest req) throws Exception{
		ListenableFuture future = jCMExecutorService.submit(new SaveMessageTask(messageDaoImpl, req));
		
		Futures.addCallback(future, new FutureCallback<ResultSet>() {
		    @Override
		    public void onSuccess(ResultSet contents) {
		        //...process resultset
		    }

		    @Override
		    public void onFailure(Throwable throwable) {
		        
		    }
		});
	
		return "message queued";
	}
	
	@RequestMapping(value="/asyncasyncupload2c", method = RequestMethod.POST)
	public String asyncasyncupload2c(@RequestBody MessageUploadRequest req) throws Exception{
		ListenableFuture future = jCMExecutorService.submit(new AsyncSaveMessageTask(messageDaoImpl, req));
		
		Futures.addCallback(future, new FutureCallback<ResultSet>() {
		    @Override
		    public void onSuccess(ResultSet contents) {
		        //...process resultset
		    }

		    @Override
		    public void onFailure(Throwable throwable) {
		        
		    }
		});
	
		return "message queued";
	}

}
