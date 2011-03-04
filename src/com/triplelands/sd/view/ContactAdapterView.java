package com.triplelands.sd.view;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.sms.Contact;

public class ContactAdapterView extends LinearLayout {
	
	public ContactAdapterView(final Context context, Contact contact, final List<Contact> listSelectedContact, final Button btn) {
		super(context);

		this.setOrientation(VERTICAL);

		View v = inflate(context, R.layout.contact_row_item, null);

		CheckBox ckBox = (CheckBox) v.findViewById(R.id.ckBoxContact);
		TextView txtName = (TextView) v.findViewById(R.id.txtName);
		TextView txtNumber = (TextView) v.findViewById(R.id.txtNumber);
		
		for(int i=0; i<listSelectedContact.size(); i++){
			System.out.println("NOMOR VIEW: " + contact.getNumber());
			if(contact.getNumber().equals(listSelectedContact.get(i).getNumber())){
				ckBox.setChecked(true);
			}
		}
			
		if(listSelectedContact.contains(contact)) ckBox.setChecked(true);
		
		ckBox.setTag(contact);
		txtName.setText(contact.getName());
		txtNumber.setText(contact.getNumber());

		ckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Contact contact = (Contact)buttonView.getTag();
				if (isChecked) {
					listSelectedContact.add(contact);
				} else {
					for(int i=0; i<listSelectedContact.size(); i++){
						System.out.println("NOMOR UNCHECKED: " + contact.getNumber());
						if(contact.getNumber().equals(listSelectedContact.get(i).getNumber())){
							listSelectedContact.remove(i);
						}
					}
					listSelectedContact.remove(contact);
				}
				btn.setText("Pilih (" + listSelectedContact.size() + ")");
			}
		});

		addView(v);
	}

}
