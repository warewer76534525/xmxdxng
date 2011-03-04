package com.triplelands.sd.activitypage;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.triplelands.sd.sms.Contact;
import com.triplelands.sd.storage.ContactsRepository;
import com.triplelands.sd.view.ContactAdapter;

public class ContactListActivity extends Activity {
	private ContactAdapter _adapter;
	private Button _btnPick;
	private ListView _lvContact;
	private ContactsRepository _contactRepository;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);
		setTitle("Pilih Contact");
		
		_contactRepository = ContactsRepository.getInstance();		
		
		_lvContact = (ListView) findViewById(R.id.listViewContact);
		_btnPick = (Button) findViewById(R.id.btnPickContact);
		_btnPick.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						pickContact();
					}
				});
		
		((EditText)findViewById(R.id.txtSearch)).addTextChangedListener(filterTextWatcher);
		_adapter = new ContactAdapter(this, _btnPick);
		populateList(_contactRepository.getContacts(this, null), null);
	}
	
	private TextWatcher filterTextWatcher = new TextWatcher() {
		
		public void afterTextChanged(Editable arg0) {			
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {			
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			populateList(_contactRepository.getContacts(ContactListActivity.this, s.toString()), _adapter.getSelectedContacts());
		}
	};

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

	private void populateList(List<Contact> displayContacts, List<Contact> selectedContacts) {
		_adapter.setListContact(displayContacts);
			
		if(selectedContacts != null) 
			_adapter.setSelectedContacts(selectedContacts);
		
		_lvContact.setAdapter(_adapter);
	}
}
