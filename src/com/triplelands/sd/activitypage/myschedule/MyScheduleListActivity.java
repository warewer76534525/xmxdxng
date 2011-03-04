package com.triplelands.sd.activitypage.myschedule;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.storage.SmsTaskRepository;
import com.triplelands.sd.view.SMSTaskAdapter;

public class MyScheduleListActivity extends Activity {
	
	private ListView _listTask;
	private TextView _txtEmpty;
	private int _selectedId;
	
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
		
		_listTask.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int pos, long id) {
				_selectedId = Integer.parseInt(String.valueOf(view.getTag()));
				return false;
			}
		});
		registerForContextMenu(_listTask);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Schedule Menu");
		menu.add(0, _selectedId, 0, "Ubah Contact Tujuan");
		menu.add(0, _selectedId, 1, "Ubah Pesan");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getOrder() == 0) {
			Intent i = new Intent(this, EditDestination.class);
			i.putExtra("idSchedule", _selectedId);
			startActivity(i);
		} else if (item.getOrder() == 1) {
			Intent i = new Intent(this, EditSchedule.class);
			i.putExtra("idSchedule", _selectedId);
			startActivity(i);
		} else {
			return false;
		}
		return true;
	}
}
