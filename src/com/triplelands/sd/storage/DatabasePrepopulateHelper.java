package com.triplelands.sd.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabasePrepopulateHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "smsdong";
	private static final int DATABASE_VERSION = 5;
	private Context context;

	DatabasePrepopulateHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("SD", "copyDatabase()");
		copyDataBase();
	}
	
	private void copyDataBase() {
		
		try {
			InputStream myInput = context.getAssets().open(DATABASE_NAME);
			
			File outFile = context.getDatabasePath(DATABASE_NAME);
			outFile.delete();
			
			OutputStream myOutput = new FileOutputStream(outFile);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			myOutput.flush();
			myOutput.close();
			myInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("DB_SMSDONG_UPGRADE", "Upgrading database from version " + oldVersion + " to "
				 + newVersion + ", which will destroy all old data");
		db.delete("eventchain", null, null);
	}
}
