package com.triplelands.sd.activitypage.myschedule;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.triplelands.sd.activitypage.BaseCustomTitle;
import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.sms.SmsTask;
import com.triplelands.sd.storage.SmsTaskRepository;

public class EditSchedule extends BaseCustomTitle {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_sms_schedule);
		
		Bundle bundle = getIntent().getExtras();
		final int idSchedule = bundle.getInt("idSchedule");
		
		final SmsTaskRepository repo = new SmsTaskRepository(this);
		repo.open();
		SmsTask task = repo.getSmsTask(idSchedule);
		repo.close();
		
		final EditText txtName = (EditText)findViewById(R.id.txtJudulSchedule);
		txtName.setText(task.getName().toString());
		final EditText txtMessage = (EditText) findViewById(R.id.txtContentSchedule);
		txtMessage.setText(task.getMessage().getContent());
		
		Button btnSubmit = (Button) findViewById(R.id.btnEditSchedule);
		btnSubmit.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				if(txtName.getText().toString().equals("")){
					Toast.makeText(EditSchedule.this, "Nama schedule masih kosong.", Toast.LENGTH_SHORT).show();
				} else if(txtMessage.getText().toString().equals("")){
					Toast.makeText(EditSchedule.this, "Pesan schedule masih kosong.", Toast.LENGTH_SHORT).show();
				} else {
					repo.open();
					repo.update(idSchedule, txtName.getText().toString(), txtMessage.getText().toString());
					repo.close();
					Toast.makeText(EditSchedule.this, "Pesan schedule sudah diperbaharui.", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});
	}
}
