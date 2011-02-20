package com.triplelands.sd.sms;

import java.util.List;

public class Group {

	private long id;
	private String name;
	private List<Contact> contacts;

	public Group() {
	}
	
	public Group(String name, List<Contact> contacts) {
		this.name = name;
		this.contacts = contacts;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public Group create(String name, List<Contact> contacts) {
		return new Group(name, contacts);
	}
}
