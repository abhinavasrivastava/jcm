package com.jiocloud.messages.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiocloud.messages.dao.MessageDaoImpl;
import com.jiocloud.messages.model.MessageUploadRequest;

@Service
public class MessageUploadServiceImpl {

	@Autowired
	MessageDaoImpl messageDaoImpl;
	
	public void saveMessages(MessageUploadRequest messageUploadRequest){
		//messageDaoImpl.saveMessages2L(messageUploadRequest);
		Date start = new Date();
		messageDaoImpl.saveMessages2R(messageUploadRequest);
		Date end = new Date();
		System.out.println("Sync query time - " + (end.getTime() - start.getTime()));
		
		start = new Date();
		messageDaoImpl.saveAsyncMessages2R(messageUploadRequest);
		end = new Date();
		System.out.println("Sync query time - " + (end.getTime() - start.getTime()));
	}
}
