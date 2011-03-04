package com.triplelands.sd.activitypage.myschedule;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.triplelands.sd.activitypage.BaseCustomTitle;
import com.triplelands.sd.activitypage.ContactListActivity;
import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.sms.Contact;
import com.triplelands.sd.storage.SmsTaskRepository;

public class EditDestination extends BaseCustomTitle {
	int _idSchedule;
	private List<Contact> _listDestination;
	private TextView _txtDestination;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_destination);
		
		Bundle bundle = getIntent().getExtras();
		_idSchedule = bundle.getInt("idSchedule");
		
		final SmsTaskRepository repo = new SmsTaskRepository(this);
		repo.open();
		_listDestination = repo.getSmsTask(_idSchedule).getMessage().getDestinations();
		repo.close();
		
		_txtDestination = (TextView)findViewById(R.id.txtContacts);
		ImageButton btnPick = (ImageButton)findViewById(R.id.btnPickDestination);
		Button btnSubmit = (Button)findViewById(R.id.btnSubmitDestination);
		
		updateList();
		
		btnPick.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivityForResult(new Intent(EditDestination.this, ContactListActivity.class), 0);
			}
		});
		
		btnSubmit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				repo.open();
				repo.update(_idSchedule, _listDestination);
				repo.close();
				Toast.makeText(EditDestination.this, "Nomor tujuan sudah diperbaharui.", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}
	
	private void updateList(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < _listDestination.size(); i++){
			sb.append("\n* " + _listDestination.get(i).getName() + " (" + _listDestination.get(i).getNumber() + ")");
		}
		_txtDestination.setText(sb.toString());
	}
	
	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			_listDestination = (ArrayList<Contact>) data.getExtras().getSerializable("contacts");
			updateList();
		}
	}
}
