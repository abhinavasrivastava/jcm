package com.jiocloud.messages.thread;

import java.util.concurrent.Callable;

import com.datastax.driver.core.ResultSet;
import com.jiocloud.messages.dao.MessageDaoImpl;
import com.jiocloud.messages.model.MessageUploadRequest;

public class AsyncSaveMessageTask implements Callable<ResultSet>{

	MessageDaoImpl messageDaoImpl;
	MessageUploadRequest messageUploadRequest;
	
	
	public AsyncSaveMessageTask(MessageDaoImpl messageDaoImpl,MessageUploadRequest messageUploadRequest){
		this.messageDaoImpl = messageDaoImpl;
		this.messageUploadRequest = messageUploadRequest;
	}
	
	@Override
	public ResultSet call() throws Exception {
		return  messageDaoImpl.saveAsyncMessages2R(messageUploadRequest);
		//return "see u.";
	}

}
