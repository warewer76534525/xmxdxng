package com.triplelands.sd.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Repository {
	
	private SQLiteOpenHelper _databaseHelper;
	private final Context _context;
	protected SQLiteDatabase _database;
	
	public Repository(Context context) {
		_context = context;
	}

	public void open() {
		_databaseHelper = new DatabasePrepopulateHelper(_context);
//		_databaseHelper = new DatabaseHelper(_context);
		_database = _databaseHelper.getWritableDatabase();
	}
	
	public void close() {
		_database.close();
	}
	
	public void init() {
		open();
		close();
	} 
}
