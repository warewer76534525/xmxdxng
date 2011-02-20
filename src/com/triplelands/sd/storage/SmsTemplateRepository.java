package com.triplelands.sd.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.triplelands.sd.smstemplate.SmsCategory;
import com.triplelands.sd.smstemplate.SmsTemplate;

public class SmsTemplateRepository extends Repository {
	private static final String CATEGORY_TABLE = "smscategory";
	private static final String SMS_TEMPLATE_TABLE = "smstemplate";

	public SmsTemplateRepository(Context context) {
		super(context);
	}

	public void save(SmsCategory smsCategory) {
		ContentValues newCategoryValues = new ContentValues();

		newCategoryValues.put("name", smsCategory.getName());

		_database.insert(CATEGORY_TABLE, null, newCategoryValues);
	}

	public void save(SmsTemplate smsTemplate) {
		ContentValues newSmsValues = new ContentValues();

		newSmsValues.put("title", smsTemplate.getTitle());
		newSmsValues.put("isi", smsTemplate.getIsi());
		newSmsValues.put("category", smsTemplate.getCategory());

		_database.insert(SMS_TEMPLATE_TABLE, null, newSmsValues);
	}

	public Cursor getAllCategories() {
		return _database.query(CATEGORY_TABLE, new String[] { "_id", "name" },
				null, null, null, null, null);
	}
	
	public String getCategoryName(int idCategory){
		Cursor cursor = _database.query(CATEGORY_TABLE, new String[] { "_id",
		"name" }, "_id=" + idCategory, null, null, null, null);
		
		if (cursor == null || !cursor.moveToFirst())
			return null;
		
		String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
		cursor.close();
		return name;
	}

	public Cursor getAllSmsTemplateByCategory(int category) {
		return _database.query(SMS_TEMPLATE_TABLE, new String[] { "_id",
				"title" }, "category=" + category, null, null, null, null);
	}

	public SmsTemplate getSmsTemplate(int smsId) {
		Cursor cursor = _database.query(true, SMS_TEMPLATE_TABLE, new String[] {
				"_id", "title", "isi" }, "_id=" + smsId, null, null, null,
				null, null);

		if (cursor == null || !cursor.moveToFirst())
			return null;

		SmsTemplate template = new SmsTemplate(cursor.getLong(cursor
				.getColumnIndexOrThrow("_id")), cursor.getString(cursor
				.getColumnIndexOrThrow("title")), cursor.getString(cursor
				.getColumnIndexOrThrow("isi")));

		cursor.close();
		return template;
	}
}
