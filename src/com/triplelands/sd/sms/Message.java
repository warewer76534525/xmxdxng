package com.triplelands.sd.sms;

import java.util.List;

public class Message {
	private List<Contact> destinations;
	private String content;

	public Message(List<Contact> destinations, String content) {
		this.destinations = destinations;
		this.content = content;
	}

	public String getContent() {
		return content;
	}
	
	public List<Contact> getDestinations() {
		return destinations;
	}
	
	public static Message create(List<Contact> destinations, String message) {
		return new Message(destinations, message);
	}
}
