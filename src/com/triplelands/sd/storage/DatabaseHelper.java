package com.triplelands.sd.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String CREATE_TABLE_SMS_TASK = "CREATE TABLE IF NOT EXISTS smstask (_id integer primary key autoincrement, name text not null, "
			+ "destination text not null, message text not null, schedule real not null);";

	private static final String CREATE_TABLE_SMS_CATEGORY = "CREATE TABLE IF NOT EXISTS smscategory (_id integer primary key autoincrement, name text not null);";

	private static final String CREATE_TABLE_SMS_TEMPLATE = "CREATE TABLE IF NOT EXISTS smstemplate (_id integer primary key autoincrement, title text not null, isi text not null, category integer);";

	private static final String CREATE_TABLE_LOG = "CREATE TABLE IF NOT EXISTS applog (_id integer primary key autoincrement, type integer not null, nama text not null, deskripsi text not null, date real not null);";

	private static final String CREATE_TABLE_SPECIAL_EVENT = "CREATE TABLE IF NOT EXISTS specialevent (_id integer primary key autoincrement, category integer not null, nama text not null, "
			+ "tanggal integer not null, bulan integer not null, jam integer not null, menit integer not null, complete integer not null);";

	private static final String CREATE_TABLE_GROUPS = "CREATE TABLE IF NOT EXISTS groups (_id integer primary key autoincrement, name text not null, "
			+ "contacts text not null);";
	
	private static final String CREATE_TABLE_EVENT_CHAIN = "CREATE TABLE IF NOT EXISTS eventchain (_id integer primary key autoincrement, " +
			"	nextid integer,obj text not null);";
	
	private static final String DATABASE_NAME = "smsdong";
	private static final int DATABASE_VERSION = 1;

	DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SMS_TASK);
		db.execSQL(CREATE_TABLE_SMS_CATEGORY);
		db.execSQL(CREATE_TABLE_SMS_TEMPLATE);
		db.execSQL(CREATE_TABLE_GROUPS);
		db.execSQL(CREATE_TABLE_SPECIAL_EVENT);
		db.execSQL(CREATE_TABLE_LOG);
		db.execSQL(CREATE_TABLE_EVENT_CHAIN);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
		// + newVersion + ", which will destroy all old data");
		// db.execSQL("DROP TABLE IF EXISTS notes");
		// onCreate(db);
	}
}
