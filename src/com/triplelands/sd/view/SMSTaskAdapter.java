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

public class SMSTaskAdapter extends CursorAdapter {

	public SMSTaskAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView txtName = (TextView) view.findViewById(R.id.txtName);
		TextView txtTime = (TextView) view.findViewById(R.id.txtTime);
		DateTime time = DateTime.fromMilis(cursor.getLong(cursor
				.getColumnIndexOrThrow("schedule")));
		txtName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
		txtTime.setText(time.getDayName(time.getDayOfWeek()) + ", "
				+ time.getDate() + "-" + (time.getMonth()) + "-" + time.getYear()
				+ " " + DateTime.to2CharFormat(time.getHour()) + ":" + DateTime.to2CharFormat(time.getMinute()));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.my_schedule_list_row, parent, false);
		bindView(v, context, cursor);
		v.setTag(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
		return v;
	}

}
