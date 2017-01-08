package com.jiocloud.messages.thread;

import java.util.concurrent.Callable;

import com.jiocloud.messages.dao.MessageDaoImpl;
import com.jiocloud.messages.model.MessageUploadRequest;

public class SaveMessageTask implements Callable<String>{

	MessageDaoImpl messageDaoImpl;
	MessageUploadRequest messageUploadRequest;
	
	
	public SaveMessageTask(MessageDaoImpl messageDaoImpl,MessageUploadRequest messageUploadRequest){
		this.messageDaoImpl = messageDaoImpl;
		this.messageUploadRequest = messageUploadRequest;
	}
	
	@Override
	public String call() throws Exception {
		messageDaoImpl.saveAsyncMessages2R(messageUploadRequest);
		
		return "see u.";
	}

}
