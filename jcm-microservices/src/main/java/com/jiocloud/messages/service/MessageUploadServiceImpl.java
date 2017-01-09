package com.jiocloud.messages.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiocloud.messages.dao.MessageDaoImpl;
import com.jiocloud.messages.model.MessageUploadRequest;

@Service
public class MessageUploadServiceImpl {

	@Autowired
	MessageDaoImpl messageDaoImpl;
	
	public void saveAsyncMessages(MessageUploadRequest messageUploadRequest){
		messageDaoImpl.saveMessages2R(messageUploadRequest);
	}
	
	public void saveMessages(MessageUploadRequest messageUploadRequest){
		messageDaoImpl.saveAsyncMessages2R(messageUploadRequest);
	}
}
