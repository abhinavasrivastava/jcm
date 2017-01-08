package com.jiocloud.messages.dao;

import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.datastax.driver.core.Session;
import com.google.common.util.concurrent.ListenableFuture;

@Component
public class CassandraConnectionFactory {

	CassandraConnector cassandraConnector;
	
	private Session session;
	ListenableFuture<Session> asyncSession;
	ResourceBundle rb = ResourceBundle.getBundle("jcm-app");
	
	@PostConstruct
	public void initialize(){
		cassandraConnector = new CassandraConnector();
		cassandraConnector.connect(rb.getString("cassandra.node"), 9042, rb.getString("cassandra.keyspace"));
		session = cassandraConnector.getSession();
		asyncSession = cassandraConnector.getAsyncSession();
	}
	
	public Session getSession(){
		return session;
	}
	
	public ListenableFuture<Session> getAsyncSession(){
		return asyncSession;
	}
}
