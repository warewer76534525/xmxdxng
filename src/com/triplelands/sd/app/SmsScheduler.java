package com.triplelands.sd.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.triplelands.sd.sms.Message;
import com.triplelands.sd.sms.SmsTask;
import com.triplelands.sd.storage.SmsTaskRepository;
import com.triplelands.sd.util.DateTime;

public class SmsScheduler {

	private Context _pageContext;
	private AlarmManager _alarmManager;
	private static int LAST_SCHEDULED_TASK_ID = 0;
	private SmsTaskRepository _smsTaskRepository;
	
	public SmsScheduler(Context context) {
		_pageContext = context;
		_alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		_smsTaskRepository = new SmsTaskRepository(context);
	}

	public void schedule(String name, Message message, DateTime schedule) {
		_smsTaskRepository.open();
		_smsTaskRepository.save(name, message, schedule);
		_smsTaskRepository.close();
		
		scheduleNextIncomingTask();
	}

	public void scheduleNextIncomingTask() {
		_smsTaskRepository.open();
		SmsTask smsTask = _smsTaskRepository.getNextIncomingTask();
		_smsTaskRepository.close();
		
		if (smsTask == null) return;
		
		// jika task sudah di set di alarm maka abaikan
		if (smsTask.getId() == LAST_SCHEDULED_TASK_ID) return;
		
		LAST_SCHEDULED_TASK_ID = smsTask.getId();
		
		Intent intent = new Intent(_pageContext, SmsSchedulerHandler.class);
		intent.putExtra("taskId", smsTask.getId());
		
		PendingIntent senderIntent = PendingIntent.getBroadcast(_pageContext,
				smsTask.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
		_alarmManager.set(AlarmManager.RTC_WAKEUP, smsTask.getScheduleMilis(),
				senderIntent);
	}
}
