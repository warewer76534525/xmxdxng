package com.triplelands.sd.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.triplelands.sd.activitypage.ConfirmationActivity;
import com.triplelands.sd.activitypage.HomeActivity;
import com.triplelands.sd.log.AppLog;
import com.triplelands.sd.sms.Message;
import com.triplelands.sd.sms.SmsSender;
import com.triplelands.sd.storage.LogRepository;
import com.triplelands.sd.storage.SmsTaskRepository;

public class SmsSchedulerHandler extends BroadcastReceiver {

	private Context _context;
	private SharedPreferences _sp;
	private boolean _useFooter;
	private String _footer;

	public void onReceive(Context ctx, Intent intent) {
		_context = ctx.getApplicationContext();
		_footer = "";
		_sp = PreferenceManager.getDefaultSharedPreferences(_context);

		boolean useConfirmation = _sp.getBoolean("checkboxAutoPref", true);
		_useFooter = _sp.getBoolean("checkboxFooterPref", false);
		if (_useFooter)
			_footer = _sp.getString("editTextFooterPref", "");

		long time = Long.parseLong(_sp.getString("listTimeAutoPref", "0"));
		int taskId = intent.getExtras().getInt("taskId");
		saveLog(taskId);
		if (useConfirmation) {
			Intent i = new Intent(_context, ConfirmationActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra("taskId", taskId);
			i.putExtra("time", time);
			i.putExtra("footer", _footer);
			ctx.startActivity(i);
		} else {
			sendSmsSchedule(taskId);
			SmsScheduler smsScheduler = new SmsScheduler(_context);
			smsScheduler.scheduleNextIncomingTask();
		}
		
	}

	private void sendSmsSchedule(int taskId) {
		SmsTaskRepository smsTaskRepository = new SmsTaskRepository(_context);
		
		smsTaskRepository.open();
		Message msg = smsTaskRepository.getMessageForTask(taskId);
		AppNotification notification = AppNotification.getInstance();
		if (_useFooter) {
			new SmsSender().send(msg, _footer);
		} else {
			new SmsSender().send(msg);
		}

		smsTaskRepository.delete(taskId);
		notification.show(_context, "SMS Terkirim", "SMS Terkirim: "
				+ msg.getDestinations().size() + " tujuan", msg.getContent(),
				HomeActivity.class);
		smsTaskRepository.close();
	}
	
	private void saveLog(int taskId){
		SmsTaskRepository smsTaskRepository = new SmsTaskRepository(_context);
		LogRepository logRepo = new LogRepository(_context);
		smsTaskRepository.open();
		logRepo.open();
		Message msg = smsTaskRepository.getMessageForTask(taskId);
		logRepo.save(new AppLog(AppLog.LOG_TYPE_MY_SCHEDULE, smsTaskRepository
				.getSmsTask(taskId).getName(), msg.getDestinations().size() + " tujuan", System.currentTimeMillis()));
		logRepo.close();
		smsTaskRepository.close();
	}
}
