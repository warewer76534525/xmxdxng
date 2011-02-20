package com.triplelands.sd.activitypage;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.triplelands.sd.sms.Contact;
import com.triplelands.sd.storage.ContactsRepository;
import com.triplelands.sd.view.ContactAdapter;

public class ContactListActivity extends Activity {
	private ContactAdapter _adapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);
		setTitle("Pilih Contact");
		
		ContactsRepository contactRepository = ContactsRepository.getInstance();		
		
		populateList(contactRepository.getContacts(this));
		
		((Button) findViewById(R.id.btnPickContact))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						pickContact();
					}
				});
	}

	private void pickContact() {
		if(_adapter.getSelectedContacts().size() == 0){
			Toast.makeText(this, "Silakan pilih/centang contact.", Toast.LENGTH_SHORT).show();
		}else{
			Intent resultIntent = new Intent();
			resultIntent.putExtra("contacts",
					(ArrayList<Contact>) _adapter.getSelectedContacts());
			setResult(RESULT_OK, resultIntent);
			finish();
		}
	}

	private void populateList(List<Contact> contacts) {
		ListView lvContact = (ListView) findViewById(R.id.listViewContact);
		_adapter = new ContactAdapter(this, contacts);
		lvContact.setAdapter(_adapter);
	}
}
