package com.triplelands.sd.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.triplelands.sd.log.AppLog;

public class LogRepository extends Repository{
	
	private static final String DATABASE_TABLE = "applog";

	public LogRepository(Context context) {
		super(context);
	}
	
	public void save(AppLog log){
		ContentValues newLogValues = new ContentValues();

		newLogValues.put("type", log.getType());
		newLogValues.put("nama", log.getNama());
		newLogValues.put("deskripsi", log.getDeskripsi());
		newLogValues.put("date", log.getDate());
		
		Log.i("SD", "Save to db: " + log.getNama());
		_database.insert(DATABASE_TABLE, null, newLogValues);
	}
	
	public void clearLog(){
		_database.delete(DATABASE_TABLE, null, null);
	}
	
	public Cursor getAllLog(){
		return _database.query(DATABASE_TABLE, new String[] { "_id", "type", "nama", "deskripsi", "date"}, null, null, null, null,
				"date DESC");
	}
}
