package com.triplelands.sd.activitypage;

import java.util.List;

import com.triplelands.sd.sms.Contact;
import com.triplelands.sd.storage.ContactsRepository;

import android.app.Activity;
import android.os.AsyncTask;

public class ContactLoaderTask extends AsyncTask<Void, String, Void> {
	private LoadContactHandler handler;
	private Activity act;
	private List<Contact> contacts;
	
	public ContactLoaderTask(Activity act, LoadContactHandler handler) {
		this.handler = handler;
		this.act = act;
	}
	
	protected Void doInBackground(Void... params) {
		ContactsRepository repo = ContactsRepository.getInstance();
		contacts = repo.getContacts(act, null);
		return null;
	}
	
	protected void onPostExecute(Void result) {
		handler.onQueryResult(contacts);
	}
	
}
