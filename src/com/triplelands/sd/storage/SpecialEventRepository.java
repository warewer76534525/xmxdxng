package com.triplelands.sd.storage;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.triplelands.sd.specialevent.SpecialEvent;

/**
 * Table special event akan mengandung field2 berikut di database id, category,
 * nama, tanggal, bulan, jam notifikasi, menit notifikasi, complete
 * 
 * @author Welly
 * 
 */
public class SpecialEventRepository extends Repository {

	private final String DATABASE_TABLE = "specialevent";

	public SpecialEventRepository(Context context) {
		super(context);
	}

	public void save(SpecialEvent event) {
		ContentValues newTaskValues = new ContentValues();

		newTaskValues.put("nama", event.getName());
		newTaskValues.put("category", event.getIdCategory());
		newTaskValues.put("tanggal", event.getTanggal());
		newTaskValues.put("bulan", event.getBulan());
		newTaskValues.put("jam", event.getJam());
		newTaskValues.put("menit", event.getMenit());
		newTaskValues.put("complete", 0);

		Log.i("SD", "Save to db: " + event.getName());
		_database.insert(DATABASE_TABLE, null, newTaskValues);
	}

	 public void updateTime(int id, int hour, int minute){
		ContentValues updateValues = new ContentValues();
		updateValues.put("jam", hour);
		updateValues.put("menit", minute);
		
		_database.update(DATABASE_TABLE, updateValues, "_id=" + id, null);
	 }

	/**
	 * untuk mengambil semua event yang ada
	 * 
	 * @return Cursor semua event
	 */
	public Cursor getAllEvent() {
		return _database.query(DATABASE_TABLE, new String[] { "_id",
				"category", "nama", "tanggal", "bulan", "jam", "menit",
				"complete" }, null, null, null, null, "_id ASC");
	}

	public void markAsComplete(Integer id) {
		ContentValues updateValues = new ContentValues();
		updateValues.put("complete", 1);

		_database.update(DATABASE_TABLE, updateValues, "_id=" + id, null);
	}
		
	public List<SpecialEvent> getAllEventObjects() {
		Cursor cursor = getAllEvent();
		
		if (cursor == null || !cursor.moveToFirst()){
			return null;
		}
		
		List<SpecialEvent> events = new ArrayList<SpecialEvent>();
		while (!cursor.isAfterLast()) {
			SpecialEvent event = new SpecialEvent(cursor.getInt(cursor
					.getColumnIndexOrThrow("_id")), cursor.getInt(cursor
					.getColumnIndexOrThrow("category")), cursor.getString(cursor
					.getColumnIndexOrThrow("nama")), cursor.getInt(cursor
					.getColumnIndexOrThrow("tanggal")), cursor.getInt(cursor
					.getColumnIndexOrThrow("bulan")), cursor.getInt(cursor
					.getColumnIndexOrThrow("jam")), cursor.getInt(cursor
					.getColumnIndexOrThrow("menit")));
			events.add(event);
			cursor.moveToNext();
		}
		
		cursor.close();
		
		return events;
	}

	public SpecialEvent getEvent(int eventId) {
		Cursor cursor = _database.query(true, DATABASE_TABLE, new String[] { "_id",
				"category", "nama", "tanggal", "bulan", "jam", "menit",
				"complete" }, "_id=" + eventId, null, null, null,
				null, null);

		if (cursor == null || !cursor.moveToFirst())
			return null;
		
		SpecialEvent event = new SpecialEvent(cursor.getInt(cursor
				.getColumnIndexOrThrow("_id")), cursor.getInt(cursor
				.getColumnIndexOrThrow("category")), cursor.getString(cursor
				.getColumnIndexOrThrow("nama")), cursor.getInt(cursor
				.getColumnIndexOrThrow("tanggal")), cursor.getInt(cursor
				.getColumnIndexOrThrow("bulan")), cursor.getInt(cursor
				.getColumnIndexOrThrow("jam")), cursor.getInt(cursor
				.getColumnIndexOrThrow("menit")));
		
		cursor.close();
		
		return event;
	}
}
