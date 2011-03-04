package com.triplelands.sd.storage;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.Contacts.People;

import com.triplelands.sd.sms.Contact;
import com.triplelands.sd.util.StringUtil;

public class ContactsOldRepository extends ContactsRepository {

	@Override
	public List<Contact> getContacts(Activity activity, String filter) {
		if (filter == null)
			filter = "";
		
		String numberFilter = "";
		if (StringUtil.isIntNumber(filter)) {
			numberFilter = filter;
			filter = "";
		}
		
		Uri contacts = People.CONTENT_URI;
		String[] NAME_PROJECTION = new String[] { Contacts.Phones._ID,
				Contacts.Phones.NAME, Contacts.People.PRIMARY_PHONE_ID };
		Cursor cur = activity.managedQuery(contacts, NAME_PROJECTION,
				Contacts.Phones.NAME + " LIKE %'" + filter + "%'", null,
				People.NAME + " ASC");
		System.out.println("Jumlah nama: " + cur.getCount());

		List<Contact> listContact = new ArrayList<Contact>();
		if (cur.moveToFirst()) {

			String name;
			String id;
			int nameColumn = cur.getColumnIndex(People.NAME);
			int idColumn = cur.getColumnIndexOrThrow(People._ID);

			do {
				id = cur.getString(idColumn);
				name = cur.getString(nameColumn);
				if (Integer.parseInt(cur.getString(cur
						.getColumnIndex(Contacts.People.PRIMARY_PHONE_ID))) > 0) {
					String[] PHONE_PROJECTION = new String[] {
							Contacts.Phones._ID, Contacts.Phones.NAME,
							Contacts.Phones.PERSON_ID, Contacts.Phones.NUMBER };
					Cursor pCur = activity.managedQuery(
							Contacts.Phones.CONTENT_URI, PHONE_PROJECTION,
							Contacts.Phones.PERSON_ID + " LIKE '" + id + "' and replace(" + Contacts.Phones.NUMBER + ", '-','') LIKE '%" + numberFilter + "%'",
							null, null);
					// if(pCur.moveToFirst()){
					while (pCur.moveToNext()) {
						String phoneNum = pCur.getString(pCur
								.getColumnIndex(Contacts.Phones.NUMBER));
						System.out.println("name: " + name);
						System.out.println("number: " + phoneNum);
						listContact.add(new Contact(name, phoneNum));
					}
					// }
					pCur.close();
				}
			} while (cur.moveToNext());
			cur.close();
		}

		return listContact;
	}

}
