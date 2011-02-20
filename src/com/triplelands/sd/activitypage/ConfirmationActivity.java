package com.triplelands.sd.activitypage;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.triplelands.sd.app.AppNotification;
import com.triplelands.sd.app.SmsScheduler;
import com.triplelands.sd.sms.Message;
import com.triplelands.sd.sms.SmsSender;
import com.triplelands.sd.storage.SmsTaskRepository;

public class ConfirmationActivity extends Activity {
	private SmsTaskRepository _smsTaskRepository;
	private SmsSender _smsSender;
	private AppNotification _notification;
	private Button _btnSend, _btnCancel;
	private int _taskId;
	private TextView _txtContent;
	private boolean finished;
	private long _time;
	private String _footer;
	private SmsScheduler _smsScheduler;
	private CountDownTimer _countdownTimer;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirmation_activity);
		_smsScheduler = new SmsScheduler(getApplicationContext());
		_smsTaskRepository = new SmsTaskRepository(getApplicationContext());
		_smsTaskRepository.open();
		_smsSender = new SmsSender();
		_notification = AppNotification.getInstance();
		_taskId = getIntent().getExtras().getInt("taskId");
		_time = getIntent().getExtras().getLong("time");
		_footer = getIntent().getExtras().getString("footer");
		
		_btnSend = (Button) findViewById(R.id.btnSend);
		_btnCancel = (Button) findViewById(R.id.btnCancel);
		_btnSend.setOnClickListener(new OnButtonClick());
		_btnCancel.setOnClickListener(new OnButtonClick());
//		_txtTitle = (TextView) findViewById(R.id.txtConfirmationTitle);
		_txtContent = (TextView) findViewById(R.id.txtSMSContent);
		_txtContent.setText(_smsTaskRepository.getMessageForTask(_taskId).getContent());
		setTitle("SMSDong: " + _smsTaskRepository.getSmsTask(_taskId).getName());
		
		if(_time > 0){
			startTimer();
		}
	}

	private class OnButtonClick implements OnClickListener {
		public void onClick(View v) {
			if (v == _btnSend) {
				sendSmsSchedule(_taskId, false);
				Toast.makeText(ConfirmationActivity.this, "SMS Terkirim.", Toast.LENGTH_SHORT).show();
			}
			if (v == _btnCancel) {
				cancelSmsSchedule(_taskId);
			}
			finished = true;
			
			_smsTaskRepository.close();
			scheduleNextTask();
			finish();
		}
	}

	private void sendSmsSchedule(int taskId, boolean showNotification) {
		if(_countdownTimer != null)_countdownTimer.cancel();
		Message msg = _smsTaskRepository.getMessageForTask(taskId);
		
		if(!_footer.equals("")){
			_smsSender.send(msg, _footer);
		}else{
			_smsSender.send(msg);
		}
		
		_smsTaskRepository.delete(taskId);
		if(showNotification){
			_notification.show(getApplicationContext(), "SMS Terkirim",
					"SMS Terkirim: " + msg.getDestinations().size() + " tujuan", msg.getContent(),
					HomeActivity.class);
		}
	}

	private void cancelSmsSchedule(int id) {
		if(_countdownTimer != null) _countdownTimer.cancel();
		_smsTaskRepository.delete(id);
	}
	
	protected void onPause() {
		if(!finished){
			cancelSmsSchedule(_taskId);
			scheduleNextTask();
			Toast.makeText(this, "Kirim schedule batal.", Toast.LENGTH_SHORT).show();
		}
		super.onPause();
	}
	
	private void startTimer(){
		_countdownTimer = new CountDownTimer(_time, _time) {
			public void onTick(long millisUntilFinished) {
			}
			
			public void onFinish() {
				sendSmsSchedule(_taskId, true);
				scheduleNextTask();
				finished = true;
				finish();
			}
		};
		_countdownTimer.start();
	}
	
	private void scheduleNextTask(){
		_smsScheduler.scheduleNextIncomingTask();
	}
}
