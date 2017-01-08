package com.jiocloud.messages.dao;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jiocloud.messages.model.Message;
import com.jiocloud.messages.model.MessageUploadRequest;


@Repository
public class MessageDaoImpl {

	@Autowired
	CassandraConnectionFactory cassandraConnectionFactory;
	String sql;
	Session session;
	ListenableFuture<Session> asyncSession;
	PreparedStatement prepared;
	//BoundStatement boundStatement;
	ListenableFuture<PreparedStatement> preparedAsync;
	
	@PostConstruct
    public void initialize(){
		Object[]sessions = cassandraConnectionFactory.getSessions();
//	    session = cassandraConnectionFactory.getSession();
//	    asyncSession = cassandraConnectionFactory.getAsyncSession();
		session = (Session)sessions[0];
	    asyncSession = (ListenableFuture<Session>)sessions[1];
		String sql = "insert into textmessages (userid,"
				+ "id,"
				+ "address,"
				+ "msgbody,"
				+ "msgdate,"
				+ "msgid,"
				+ "msgtype) "
				+ "values(?,?,?,?,?,?,?)";
		prepared = session.prepare(sql);
		preparedAsync = session.prepareAsync(sql);
		//boundStatement = new BoundStatement(prepared);
		
	}



	public void saveMessages2L(MessageUploadRequest messageUploadRequest){
		Session session = cassandraConnectionFactory.getSession();
		String sql = "insert into textmessages (userid,"
				+ "id,"
				+ "address,"
				+ "msgbody,"
				+ "msgdate,"
				+ "msgid,"
				+ "msgtype,"
				+ "hash,"
				+ "last_updated_tx_stamp) "
				+ "values(?,?,?,?,?,?,?,?,?)";
		PreparedStatement prepared = session.prepare(sql);
		BatchStatement batchStmt = new BatchStatement();		
		for(Message message:messageUploadRequest.getMessages()){
			batchStmt.add(prepared.bind(/*messageUploadRequest.getJioId(),*/
					"changeme",
					UUIDs.random(),
					message.getAddress(),
					message.getBody(),
					new Date(),
					message.get_id(),
					message.getType(),
					"very-bad-hash",
					new Date()
					));
		}
		session.execute(batchStmt);



		//				for(Message message:messageUploadRequest.getMessages()){
		//					session.execute(prepared.bind(/*messageUploadRequest.getJioId(),*/
		//							"changeme",
		//							UUIDs.random(),
		//							message.getAddress(),
		//							message.getBody(),
		//							message.getDate(),
		//							message.get_id(),
		//							message.getType(),
		//							"very-bad-hash",
		//							new Date()
		//							));
		//				}
	}

	public void saveMessages2R(MessageUploadRequest messageUploadRequest){
		BatchStatement batchStmt = new BatchStatement();
		for(Message message:messageUploadRequest.getMessages()){
			batchStmt.add(prepared.bind(/*messageUploadRequest.getJioId(),*/
					"123",
					UUIDs.random(),
					message.getAddress(),
					message.getBody(),
					message.getDate(),
					message.get_id(),
					message.getType()
					));
		}
		session.execute(batchStmt);
	}
	
	
	public void saveAsyncMessages2R(MessageUploadRequest messageUploadRequest){
		BatchStatement batchStmt = new BatchStatement();
		for(Message message:messageUploadRequest.getMessages()){
			batchStmt.add(prepared.bind(/*messageUploadRequest.getJioId(),*/
					"123",
					UUIDs.random(),
					message.getAddress(),
					message.getBody(),
					message.getDate(),
					message.get_id(),
					message.getType()
					));
		}
	//	ResultSetFuture f = session.executeAsync(batchStmt);
		ListenableFuture<ResultSet> resultSet = Futures.transform(asyncSession,
			    new AsyncFunction<Session, ResultSet>() {
			        public ListenableFuture<ResultSet> apply(Session session) throws Exception {
			            return session.executeAsync(batchStmt);
			        }
			    });
		
		Futures.addCallback(resultSet, new FutureCallback<ResultSet>() {
		    public void onSuccess(ResultSet resultSet) {

		    }

		    public void onFailure(Throwable t) {
		        System.out.printf("Failed to retrieve the version: %s%n",
		            t.getMessage());
		    }
		});
		//return;
	}

}
