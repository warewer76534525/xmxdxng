package com.triplelands.sd.view;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.triplelands.sd.activitypage.R;

public class SMSCategoryAdapter extends CursorAdapter {

	public SMSCategoryAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView txtName = (TextView) view.findViewById(R.id.txtCategory);
		txtName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
		
		view.setTag(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("_id"))));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.sms_category_row, parent, false);
		bindView(v, context, cursor);
		return v;
	}

}
