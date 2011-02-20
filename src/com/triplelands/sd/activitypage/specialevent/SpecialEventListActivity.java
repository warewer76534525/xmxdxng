package com.triplelands.sd.activitypage.specialevent;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.app.SpecialEventScheduler;
import com.triplelands.sd.storage.SpecialEventRepository;
import com.triplelands.sd.view.SpecialEventAdapter;

public class SpecialEventListActivity extends Activity {
	
	private ListView _listTask;
	private TextView _txtEmpty;
	private Dialog _scheduleTimePicker;
	private TimePicker _timePicker;
	private Button _btnSetTimePicker;
	SpecialEventRepository _specialEventRepo;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.special_event_list);
		
		_listTask = (ListView) findViewById(R.id.listViewSpecialEvent);
		_txtEmpty = (TextView) findViewById(R.id.txtEmpty);
		
	}
	
	protected void onResume() {
		super.onResume();
		
		_specialEventRepo = new SpecialEventRepository(this);
		_specialEventRepo.open();
		Cursor cursor = _specialEventRepo.getAllEvent();
//		startManagingCursor(cursor);
		
		if(cursor.getCount() > 0) _txtEmpty.setVisibility(View.GONE);
		else _txtEmpty.setVisibility(View.VISIBLE);
		
		_listTask.setAdapter(new SpecialEventAdapter(this, cursor));
		
		_listTask.setOnItemLongClickListener(new OnItemLongClickListener(){
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				showTimeDialog(Integer.parseInt("" + view.getTag()));
				return false;
			}
		});
		
		_specialEventRepo.close();
	}
	
	private void showTimeDialog(final int idEvent){
		_scheduleTimePicker = new Dialog(this);
		_scheduleTimePicker.setTitle("Edit Waktu Notifikasi");
		_scheduleTimePicker.setContentView(R.layout.time_picker_dialog);
		_scheduleTimePicker.setCancelable(true);
		_timePicker = (TimePicker) _scheduleTimePicker
				.findViewById(R.id.timeSchedule);
		_timePicker.setCurrentHour(7);
		_timePicker.setCurrentMinute(0);
		_btnSetTimePicker = (Button) _scheduleTimePicker
				.findViewById(R.id.btnSetTimePicker);
		_btnSetTimePicker.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(SpecialEventListActivity.this, "Waktu notifikasi sudah diedit.", Toast.LENGTH_SHORT).show();
				_scheduleTimePicker.dismiss();
				
				SpecialEventScheduler scheduler = new SpecialEventScheduler(SpecialEventListActivity.this);
				scheduler.updateNotificationTime(idEvent, _timePicker.getCurrentHour(), _timePicker.getCurrentMinute());
				onResume();
			}
		});
		_scheduleTimePicker.show();
	}
	
}
