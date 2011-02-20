package com.triplelands.sd.view;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.util.DateTime;

public class SpecialEventAdapter extends CursorAdapter {

	public SpecialEventAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView txtName = (TextView) view.findViewById(R.id.txtNameEvent);
		TextView txtTime = (TextView) view.findViewById(R.id.txtTimeEvent);
		txtName.setText(cursor.getString(cursor.getColumnIndexOrThrow("nama")));
		
		txtTime.setText(
				cursor.getInt(cursor.getColumnIndexOrThrow("tanggal")) + "/" +
				cursor.getInt(cursor.getColumnIndexOrThrow("bulan")) + " " +
			 	DateTime.to2CharFormat(cursor.getInt(cursor.getColumnIndexOrThrow("jam")))+ ":" +
			 	DateTime.to2CharFormat(cursor.getInt(cursor.getColumnIndexOrThrow("menit"))));
		view.setTag(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.special_event_list_row, parent, false);
		bindView(v, context, cursor);
		return v;
	}
}
