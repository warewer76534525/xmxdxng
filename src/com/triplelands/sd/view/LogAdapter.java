package com.triplelands.sd.view;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.log.AppLog;
import com.triplelands.sd.util.DateTime;

public class LogAdapter extends CursorAdapter {

	public LogAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ImageView imgSchedule = (ImageView) view.findViewById(R.id.imgSchedule);
		ImageView imgEvent = (ImageView) view.findViewById(R.id.imgSpecialEvent);
		TextView txtName = (TextView) view.findViewById(R.id.txtLogName);
		TextView txtTime = (TextView) view.findViewById(R.id.txtLogTime);
		TextView txtDesc = (TextView) view.findViewById(R.id.txtLogDesc);
		
		DateTime time = DateTime.fromMilis(cursor.getLong(cursor
				.getColumnIndexOrThrow("date")));
		txtName.setText(cursor.getString(cursor.getColumnIndexOrThrow("nama")));
		txtTime.setText(time.getDayName(time.getDayOfWeek()) + ", "
				+ time.getDate() + "-" + (time.getMonth()) + "-" + time.getYear()
				+ " " + DateTime.to2CharFormat(time.getHour()) + ":" + DateTime.to2CharFormat(time.getMinute()));
		txtDesc.setText(cursor.getString(cursor.getColumnIndexOrThrow("deskripsi")));
		
		int type = cursor.getInt(cursor.getColumnIndexOrThrow("type"));
		if(AppLog.LOG_TYPE_MY_SCHEDULE == type) {
			imgSchedule.setVisibility(View.VISIBLE);
			imgEvent.setVisibility(View.GONE);
		}else if(AppLog.LOG_TYPE_SPECIAL_EVENT == type){
			imgEvent.setVisibility(View.VISIBLE);
			imgSchedule.setVisibility(View.GONE);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.log_list_row, parent, false);
		bindView(v, context, cursor);
		return v;
	}

}
