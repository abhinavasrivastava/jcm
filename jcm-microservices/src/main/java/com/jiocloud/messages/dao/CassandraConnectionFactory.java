package com.jiocloud.messages.dao;

import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.datastax.driver.core.Session;
import com.google.common.util.concurrent.ListenableFuture;

@Component
public class CassandraConnectionFactory {

	CassandraConnector cassandraConnector;
	CassandraConnector cassandraConnector2;
	
	private Session session;
	ListenableFuture<Session> asyncSession;
	
	private Session session2;
	ListenableFuture<Session> asyncSession2;
	ResourceBundle rb = ResourceBundle.getBundle("jcm-app");
	
	@PostConstruct
	public void initialize(){
		cassandraConnector = new CassandraConnector();
		cassandraConnector.connect(rb.getString("cassandra.node"), 9042, rb.getString("cassandra.keyspace"));
		session = cassandraConnector.getSession();
		asyncSession = cassandraConnector.getAsyncSession();
		
		
		cassandraConnector2 = new CassandraConnector();
		cassandraConnector2.connect(rb.getString("cassandra.node"), 9042, rb.getString("cassandra.keyspace"));
		session2 = cassandraConnector.getSession();
		asyncSession2 = cassandraConnector.getAsyncSession();
	}
	
	public Session getSession(){
		return session;
	}
	
	public ListenableFuture<Session> getAsyncSession(){
		return asyncSession;
	}
	
	public Object[] getSessions(){
		if(Math.random() > 0.5){
			return new Object[]{session2,asyncSession2};
		}else{
			return new Object[]{session,asyncSession};
		}
		
	}
}
