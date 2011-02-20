package com.triplelands.sd.activitypage.myschedule;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.storage.SmsTaskRepository;
import com.triplelands.sd.view.SMSTaskAdapter;

public class MyScheduleListActivity extends Activity {
	
	private ListView _listTask;
	private TextView _txtEmpty;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_schedule_list);
		_listTask = (ListView) findViewById(R.id.listViewSchedule);
		_txtEmpty = (TextView) findViewById(R.id.txtEmpty);
		
		Button btnAdd = (Button) findViewById(R.id.btnAddSchedule);
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MyScheduleListActivity.this, AddMyScheduleActivity.class));
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		SmsTaskRepository smsTaskRepo = new SmsTaskRepository(this);
		smsTaskRepo.open();
		Cursor cursor = smsTaskRepo.getAllTask();
		startManagingCursor(cursor);
		
		if(cursor.getCount() > 0) _txtEmpty.setVisibility(View.GONE);
		else _txtEmpty.setVisibility(View.VISIBLE);
		
		_listTask.setAdapter(new SMSTaskAdapter(this, cursor));
		smsTaskRepo.close();
	}
}
