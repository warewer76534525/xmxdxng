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
import android.widget.TextView;
import android.widget.Toast;

import com.triplelands.sd.sms.Contact;
import com.triplelands.sd.view.ContactAdapter;

public class ContactListActivity extends Activity implements LoadContactHandler{
	private ContactAdapter _adapter;
	private Button _btnPick;
	private ListView _lvContact;
	private List<Contact> _listAllContacts;
	private EditText _txtFilter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);
		setTitle("Pilih Contact");
				
		_lvContact = (ListView) findViewById(R.id.listViewContact);
		_btnPick = (Button) findViewById(R.id.btnPickContact);
		_btnPick.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						pickContact();
					}
				});
		
		_txtFilter = (EditText)findViewById(R.id.txtSearch);
		_txtFilter.addTextChangedListener(filterTextWatcher);
		_adapter = new ContactAdapter(this, _btnPick);
		
		ContactLoaderTask loader = new ContactLoaderTask(this, this);
		loader.execute();
	}
	
	public void onQueryResult(List<Contact> listContact) {
		_listAllContacts = listContact;
		populateList(_listAllContacts, null);
		((TextView)findViewById(R.id.txtLoading)).setVisibility(View.GONE);
		_txtFilter.setVisibility(View.VISIBLE);
		_txtFilter.requestFocus();
	}
	
	private TextWatcher filterTextWatcher = new TextWatcher() {
		
		public void afterTextChanged(Editable arg0) {			
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			populateList(filterContact(_listAllContacts, s.toString()), _adapter.getSelectedContacts());	
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
	
	private List<Contact> filterContact(List<Contact> source, String filter){
		List<Contact> filtered = new ArrayList<Contact>();
		for(int i = 0; i < source.size(); i++){
			if(source.get(i).getName().toLowerCase().contains(filter.toLowerCase())){
				filtered.add(source.get(i));
			}
		}
		return filtered;
	}
}
