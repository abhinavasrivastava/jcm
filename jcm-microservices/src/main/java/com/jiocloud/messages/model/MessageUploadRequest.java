package com.jiocloud.messages.model;

import java.util.List;

public class MessageUploadRequest {

	private String jioId;
	private String deviceId;
	private List<Message>messages;
	
	public String getJioId() {
		return jioId;
	}
	public void setJioId(String jioId) {
		this.jioId = jioId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public List<Message> getMessages() {
		return messages;
	}
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
}
