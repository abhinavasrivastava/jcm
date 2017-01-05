package com.jiocloud.messages.dao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
		for(Message message:messageUploadRequest.getMessages()){
			session.execute(prepared.bind(/*messageUploadRequest.getJioId(),*/
					"changeme",
					UUIDs.random(),
					message.getAddress(),
					message.getBody(),
					message.getDate(),
					message.get_id(),
					message.getType(),
					"very-bad-hash",
					new Date()
					));
		}
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
		for(Message message:messageUploadRequest.getMessages()){
			session.execute(prepared.bind(/*messageUploadRequest.getJioId(),*/
					"changeme",
					UUIDs.random(),
					message.getAddress(),
					message.getBody(),
					message.getDate(),
					message.get_id(),
					message.getType()
					));
		}
	}

}
