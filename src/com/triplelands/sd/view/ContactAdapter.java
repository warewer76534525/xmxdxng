package com.triplelands.sd.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.triplelands.sd.sms.Contact;

public class ContactAdapter extends BaseAdapter {

	private List<Contact> listContact;
	private List<Contact> listSelectedContact;
	private Context context;
	private Button btnPick;
	private Contact contact;

	public ContactAdapter(Context ctx, Button btn) {
		this.context = ctx;
		listSelectedContact = new ArrayList<Contact>();
		btnPick = btn;
	}

	public int getCount() {
		return listContact.size();
	}

	public Object getItem(int position) {
		return listContact.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public void setListContact(List<Contact> listContact){
		this.listContact = listContact;
	}
	
	public void setSelectedContacts(List<Contact> selectedList){
		listSelectedContact = selectedList;
	}
	
	public List<Contact> getSelectedContacts(){
		return listSelectedContact;
	}

	public View getView(final int position, View convertView, ViewGroup viewGroup) {
		contact = listContact.get(position);
		return new ContactAdapterView(context, contact, listSelectedContact, btnPick);
	}
}
