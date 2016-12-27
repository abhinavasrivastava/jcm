package com.jiocloud.messages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumerLoop implements Runnable{
	private KafkaConsumer<String, String> consumer;
	private List<String> topics;
	private int id;
	ResourceBundle rb = ResourceBundle.getBundle("jcm-app");

	public MessageConsumerLoop(int id,
			String groupId, 
			List<String> topics) {
		this.id = id;
		this.topics = topics;
		Properties props = new Properties();
		props.put("bootstrap.servers", rb.getString("bootstrap.servers"));
		props.put("group.id", groupId);
		props.put("key.deserializer", StringDeserializer.class.getName());
		props.put("value.deserializer", StringDeserializer.class.getName());
		this.consumer = new KafkaConsumer<String, String>(props);
	}

	@Override
	public void run() {
		try {
			consumer.subscribe(topics);

			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
				for (ConsumerRecord<String, String> record : records) {
					Map<String, Object> data = new HashMap<>();
					data.put("partition", record.partition());
					data.put("offset", record.offset());
					data.put("value", record.value());
					System.out.println(this.id + ": " + data);
				}
			}
		} catch (WakeupException e) {
			// ignore for shutdown 
		} finally {
			consumer.close();
		}
	}

	public void shutdown() {
		consumer.wakeup();
	}
}

