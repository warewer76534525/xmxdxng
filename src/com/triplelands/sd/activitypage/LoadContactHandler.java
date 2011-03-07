package com.triplelands.sd.activitypage;

import java.util.List;

import com.triplelands.sd.sms.Contact;

public interface LoadContactHandler {
	public void onQueryResult(List<Contact> listContacts);
}
