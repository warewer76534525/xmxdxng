package com.triplelands.sd.storage;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.triplelands.sd.sms.Contact;
import com.triplelands.sd.util.StringUtil;

public class ContactsNewRepository extends ContactsRepository {

	@Override
	public List<Contact> getContacts(Activity activity, String filter) {
		System.out.println("new contact");
		if (filter == null)
			filter = "";

		String numberFilter = "";
		if (StringUtil.isIntNumber(filter)) {
			numberFilter = filter;
			filter = "";
		}
		System.out.println("number filter: " + numberFilter);

		String[] NAME_PROJECTION = new String[] {
				ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.HAS_PHONE_NUMBER };
		Cursor cursor = activity.managedQuery(
				ContactsContract.Contacts.CONTENT_URI, NAME_PROJECTION,
				ContactsContract.Contacts.DISPLAY_NAME + " LIKE '%" + filter
						+ "%'", null, ContactsContract.Contacts.DISPLAY_NAME
						+ " ASC");

		int nameIdx = cursor
				.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
		int idIdx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);

		List<Contact> contacts = new ArrayList<Contact>();
		System.out.println("position kursor name: " + cursor.getPosition());
		if (cursor.moveToFirst()) {
			do {
				String name = cursor.getString(nameIdx);
				String id = cursor.getString(idIdx);

				if (Integer
						.parseInt(cursor
								.getString(cursor
										.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					String[] PHONE_PROJECTION = new String[] {
							ContactsContract.Contacts._ID,
							ContactsContract.Contacts.DISPLAY_NAME,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
							ContactsContract.CommonDataKinds.Phone.NUMBER };
					Cursor pCur = activity
							.managedQuery(
									ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
									PHONE_PROJECTION,
									ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " LIKE "+ id
											+ " and replace(" + ContactsContract.CommonDataKinds.Phone.NUMBER + ", '-', '')" 
											+ " LIKE '%" + numberFilter + "%'", null, null);
					if (pCur == null)
						throw new RuntimeException("Cursor pCur is null");
					System.out.println("jumlah nomor kontak: "
							+ pCur.getCount());
					System.out
							.println("position kursor: " + pCur.getPosition());
					// if(pCur.moveToFirst()){
					while (pCur.moveToNext()) {
						String phoneNum = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						System.out.println("name: " + name);
						System.out.println("number: " + phoneNum);
						contacts.add(new Contact(name, phoneNum));
					}
					// }
					pCur.close();
				}
			} while (cursor.moveToNext());
			cursor.close();
		}

		return contacts;
	}

}
