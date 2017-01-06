package com.jiocloud.messages.dao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import com.jiocloud.messages.model.Message;
import com.jiocloud.messages.model.MessageUploadRequest;


@Repository
public class MessageDaoImpl {

	@Autowired
	CassandraConnectionFactory cassandraConnectionFactory;

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
		Session session = cassandraConnectionFactory.getSession();
		String sql = "insert into textmessages (userid,"
				+ "id,"
				+ "address,"
				+ "msgbody,"
				+ "msgdate,"
				+ "msgid,"
				+ "msgtype) "
				+ "values(?,?,?,?,?,?,?)";
		PreparedStatement prepared = session.prepare(sql);
		BoundStatement boundStatement = new BoundStatement(prepared);
		BatchStatement batchStmt = new BatchStatement();
		for(Message message:messageUploadRequest.getMessages()){
			batchStmt.add(boundStatement.bind(/*messageUploadRequest.getJioId(),*/
					"123",
					UUIDs.random(),
					message.getAddress(),
					message.getBody(),
					message.getDate(),
					message.get_id(),
					message.getType()
					));
		}
		session.executeAsync(batchStmt);
		//session.execute(batchStmt);
		
//		for(Message message:messageUploadRequest.getMessages()){
//			session.execute(prepared.bind(/*messageUploadRequest.getJioId(),*/
//					"123",
//					UUIDs.random(),
//					message.getAddress(),
//					message.getBody(),
//					message.getDate(),
//					message.get_id(),
//					message.getType()
//					));
//		}
	}

}
