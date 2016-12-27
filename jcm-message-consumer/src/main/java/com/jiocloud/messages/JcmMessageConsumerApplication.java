package com.jiocloud.messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

public class JcmMessageConsumerApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(JcmMessageConsumerApplication.class, args);
		//MessageConsumer consumer = ctx.getBean(MessageConsumer.class);
		//JcmMessageConsumerApplication mainObj = ctx.getBean(JcmMessageConsumerApplication.class);
		//mainObj.init();
		System.out.println("test");
		int numConsumers = 3;
		String groupId = "message-consumer-group";
		List<String> topics = Arrays.asList("test");
		ExecutorService executor = Executors.newFixedThreadPool(numConsumers);
		final List<MessageConsumerLoop> consumers = new ArrayList<>();
		for (int i = 0; i < numConsumers; i++) {
			MessageConsumerLoop consumer = new MessageConsumerLoop(i, groupId, topics);
		    consumers.add(consumer);
		    executor.submit(consumer);
		  }
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    @Override
		    public void run() {
		      for (MessageConsumerLoop consumer : consumers) {
		        consumer.shutdown();
		      } 
		      executor.shutdown();
		      try {
		        executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
		      } catch (InterruptedException e) {
		        e.printStackTrace();
		      }
		    }
		  });
	}
	
	
	public void init(){
		System.out.println("test");
	}
}
	