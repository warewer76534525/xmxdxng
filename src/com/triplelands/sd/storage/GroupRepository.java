package com.triplelands.sd.storage;

import java.lang.reflect.Type;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.myjson.reflect.TypeToken;
import com.triplelands.sd.rest.JsonUtils;
import com.triplelands.sd.sms.Contact;
import com.triplelands.sd.sms.Group;

public class GroupRepository extends Repository {
	private static final String DATABASE_TABLE = "groups";

	public GroupRepository(Context context) {
		super(context);
	}
	
	public void save(Group group) {
		ContentValues newTaskValues = new ContentValues();

		Type listType = new TypeToken<List<Contact>>() {
		}.getType();

		newTaskValues.put("name", group.getName());
		String json = JsonUtils.toListJson(group.getContacts(), listType);
		newTaskValues.put("contacts", json);
		
		Log.i("SD", "Save to db: " + group.getName());
		_database.insert(DATABASE_TABLE, null, newTaskValues);
	}
	
	public Cursor getAllGroup() {
		return _database.query(DATABASE_TABLE, new String[] { "_id", "name"}, null, null, null, null,
				null);
	}
	
	public List<Contact> getContacts(int groupId) {
		Cursor cursor = _database.query(true, DATABASE_TABLE, new String[] {
				"_id", "contacts" }, "_id=" + groupId, null, null,
				null, null, null);

		if (cursor == null || !cursor.moveToFirst())
			return null;

		Type listType = new TypeToken<List<Contact>>() {
		}.getType();
		
		String json = cursor.getString(cursor
				.getColumnIndexOrThrow("contacts"));
		List<Contact> contactList = JsonUtils.toListObject2(json, listType);
		cursor.close();
		return contactList;
	}
}
