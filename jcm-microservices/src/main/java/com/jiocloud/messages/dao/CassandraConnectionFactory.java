package com.jiocloud.messages.dao;

import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.datastax.driver.core.Session;

@Component
public class CassandraConnectionFactory {

	CassandraConnector cassandraConnector;
	
	private Session session;
	ResourceBundle rb = ResourceBundle.getBundle("jcm-app");
	
	@PostConstruct
	public void initialize(){
		cassandraConnector = new CassandraConnector();
		cassandraConnector.connect(rb.getString("cassandra.node"), 9042);
		session = cassandraConnector.getSession();
	}
	
	public Session getSession(){
		return session;
	}
}
