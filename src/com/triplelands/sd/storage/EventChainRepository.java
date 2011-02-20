package com.triplelands.sd.storage;

import java.lang.reflect.Type;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.myjson.reflect.TypeToken;
import com.triplelands.sd.rest.JsonUtils;
import com.triplelands.sd.specialevent.EventChain;
import com.triplelands.sd.specialevent.SEvent;

/**
 * Event chain repository menyimpan domain logic utk chain of event
 * 
 * @author Welly
 * 
 */
public class EventChainRepository extends Repository {

	private final String DATABASE_TABLE = "eventchain";

	public EventChainRepository(Context context) {
		super(context);
	}
	
	@Override
	public void init() {
		_database.execSQL("DROP TABLE IF EXISTS eventchain;");
		_database.execSQL("CREATE TABLE IF NOT EXISTS eventchain (_id integer primary key autoincrement, nextid integer,obj text not null);");
	}

	public boolean isAlreadyIntialize() {
		Cursor cursor = _database.query(DATABASE_TABLE, new String[] { "_id", "obj"}, null, null, null, null, "_id ASC");
		boolean intialize =!(cursor == null || !cursor.moveToFirst());
		cursor.close();
		return intialize;
	}

	public void save(EventChain eventChain) {
		ContentValues newEventValues = new ContentValues();

		Type listType = new TypeToken<List<SEvent>>() {
		}.getType();

		newEventValues.put("nextid", eventChain.getNextEventIdx());
		String json = JsonUtils.toListJson(eventChain.getEvents(), listType);
		newEventValues.put("obj", json);
		
		_database.insert(DATABASE_TABLE, null, newEventValues);
	}

	public EventChain getInstance() {
		Cursor cursor = _database.query(DATABASE_TABLE, new String[] { "_id", "obj", "nextid"}, null, null, null, null, "_id ASC");
		
		if (cursor == null || !cursor.moveToFirst()){
			return null;
		}
		
		Type listType = new TypeToken<List<SEvent>>() {
		}.getType();
		
		String json = cursor.getString(cursor
				.getColumnIndexOrThrow("obj"));
		List<SEvent> eventList = JsonUtils.toListObject2(json, listType);
		
		int nextEventId =  cursor.getInt(cursor
				.getColumnIndexOrThrow("nextid"));
		cursor.close();
		return new EventChain(nextEventId, eventList);
	}

	public void update(EventChain chain) {
		ContentValues updateValues = new ContentValues();

		Type listType = new TypeToken<List<SEvent>>() {
		}.getType();

		updateValues.put("nextid", chain.getNextEventIdx());
		String json = JsonUtils.toListJson(chain.getEvents(), listType);
		updateValues.put("obj", json);

		_database.update(DATABASE_TABLE, updateValues, null, null);
	}
}
