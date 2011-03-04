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
import com.triplelands.sd.sms.Message;
import com.triplelands.sd.sms.SmsTask;
import com.triplelands.sd.util.DateTime;

/**
 * Memaintain storage utk penyimpanan task yang akan di eksekusi
 * 
 * @author Welly
 * 
 */
public class SmsTaskRepository extends Repository {
	private static final String DATABASE_TABLE = "smstask";

	public SmsTaskRepository(Context context) {
		super(context);
	}

	/**
	 * Menyimpan message dan schedule ke dalam sql lite table
	 * 
	 * @param message
	 *            message yang akan dikirimkan
	 * @param schedule
	 *            waktu pengiriman
	 */
	public void save(String name, Message message, DateTime schedule) {
		ContentValues newTaskValues = new ContentValues();

		Type listType = new TypeToken<List<Contact>>() {
		}.getType();

		newTaskValues.put("name", name);
		String json = JsonUtils.toListJson(message.getDestinations(), listType);
		newTaskValues.put("destination", json);
		newTaskValues.put("message", message.getContent());
		newTaskValues.put("schedule", schedule.toMilis());

		Log.i("SD", "Save to db: " + name);
		_database.insert(DATABASE_TABLE, null, newTaskValues);
	}

	/**
	 * Mengambil task utk mengirimkan sms sesuai dengan id
	 * 
	 * @param taskId
	 *            id dari task yang akan diambil
	 * @return
	 */
	public Message getMessageForTask(int taskId) {
		Cursor cursor = _database.query(true, DATABASE_TABLE, new String[] {
				"_id", "message", "destination" }, "_id=" + taskId, null, null,
				null, null, null);

		if (cursor == null || !cursor.moveToFirst())
			return null;

		Type listType = new TypeToken<List<Contact>>() {
		}.getType();
		String json = cursor.getString(cursor
				.getColumnIndexOrThrow("destination"));
		List<Contact> contactList = JsonUtils.toListObject2(json, listType);
		Message message = new Message(contactList, cursor.getString(cursor
				.getColumnIndexOrThrow("message")));
		cursor.close();
		return message;
	}

	/**
	 * Mendelete task yang sudah dikerjakan dari storage
	 * 
	 * @param taskId
	 *            task yang akan di delete
	 */
	public void delete(int taskId) {
		_database.delete(DATABASE_TABLE, "_id=" + taskId, null);
	}

	/**
	 * Mengambil task terdekat utk di eksekusi. Algoritma : Semua task yang ada
	 * diurutkan berdasarkan waktu. Task yang terlebih dahulu dieksekusi akan
	 * berada pada urutan yang atas
	 * 
	 * @return Task yang terdekat utk di eksekusi
	 */
	public SmsTask getNextIncomingTask() {
		Cursor cursor = _database.query(DATABASE_TABLE, new String[] { "_id",
				"name", "destination", "message", "schedule" }, null, null,
				null, null, "schedule ASC", null);

		if (cursor == null || !cursor.moveToFirst())
			return null;

		Type listType = new TypeToken<List<Contact>>() {
		}.getType();

		String json = cursor.getString(cursor
				.getColumnIndexOrThrow("destination"));

		List<Contact> contactList = JsonUtils.toListObject2(json, listType);

		Message msg = new Message(contactList, cursor.getString(cursor
				.getColumnIndexOrThrow("message")));
		SmsTask task = new SmsTask(cursor.getInt(cursor
				.getColumnIndexOrThrow("_id")), cursor.getString(cursor
				.getColumnIndexOrThrow("name")), msg, DateTime.fromMilis(cursor
				.getLong(cursor.getColumnIndexOrThrow("schedule"))));
		cursor.close();
		return task;
	}
	
	public void update (int id, List<Contact> destinations){
		ContentValues updateValues = new ContentValues();

		Type listType = new TypeToken<List<Contact>>() {
		}.getType();
		
		updateValues.put("destination", JsonUtils.toListJson(destinations, listType));
		_database.update(DATABASE_TABLE, updateValues, "_id=" + id, null);
	}
	
	public void update(int id, String name, String message){
		ContentValues updateValues = new ContentValues();

		updateValues.put("name", name);
		updateValues.put("message", message);

		_database.update(DATABASE_TABLE, updateValues, "_id=" + id, null);
	}

	public void update(int id, SmsTask smsTask) {
		ContentValues updateValues = new ContentValues();

		Type listType = new TypeToken<List<Contact>>() {
		}.getType();

		updateValues.put("name", smsTask.getName());
		updateValues.put("destination", JsonUtils.toListJson(smsTask
				.getMessage().getDestinations(), listType));
		updateValues.put("message", smsTask.getMessage().getContent());
		updateValues.put("schedule", smsTask.getScheduleMilis());

		_database.update(DATABASE_TABLE, updateValues, "_id=" + id, null);
	}

	public Cursor getAllTask() {
		return _database.query(DATABASE_TABLE, new String[] { "_id", "name",
				"destination", "message", "schedule" }, null, null, null, null,
				"schedule ASC");
	}
	
	public SmsTask getSmsTask(int taskId){
		Cursor cursor = _database.query(DATABASE_TABLE, new String[] { "_id",
				"name", "destination", "message", "schedule" }, "_id=" + taskId, null,
				null, null, null, null);
		
		if (cursor == null || !cursor.moveToFirst())
			return null;

		Type listType = new TypeToken<List<Contact>>() {
		}.getType();

		String json = cursor.getString(cursor
				.getColumnIndexOrThrow("destination"));
		List<Contact> contactList = JsonUtils.toListObject2(json, listType);
		Message msg = new Message(contactList, cursor.getString(cursor
				.getColumnIndexOrThrow("message")));
		
		SmsTask task = new SmsTask(cursor.getInt(cursor
				.getColumnIndexOrThrow("_id")), cursor.getString(cursor
				.getColumnIndexOrThrow("name")), msg, DateTime.fromMilis(cursor
				.getLong(cursor.getColumnIndexOrThrow("schedule"))));
		
		return task;
	}
}
