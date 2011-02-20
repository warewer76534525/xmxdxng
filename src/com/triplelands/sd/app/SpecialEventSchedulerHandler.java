package com.triplelands.sd.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.triplelands.sd.activitypage.smstemplate.SmsTemplatesListActivity;
import com.triplelands.sd.log.AppLog;
import com.triplelands.sd.storage.LogRepository;

public class SpecialEventSchedulerHandler extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		
		Log.i("SPECIAL_EVENT", "Special event coming");
		
		SpecialEventScheduler scheduler = new SpecialEventScheduler(context);
		int eventId = intent.getExtras().getInt("eventId");
		int catId = intent.getExtras().getInt("catId");
		String name = intent.getExtras().getString("eventName");
		
		if (scheduler.isTodayEvent()) {
			EventNotification notification = new EventNotification(
					context.getApplicationContext(), eventId, "Special Event!",
					name, "Klik untuk pilih template sms " + name,
					SmsTemplatesListActivity.class, catId);
			notification.show();
			
			LogRepository logRepo = new LogRepository(
					context.getApplicationContext());
			logRepo.open();
			logRepo.save(new AppLog(AppLog.LOG_TYPE_SPECIAL_EVENT, name,
					"Special Event '" + name + "' Reminder", System
							.currentTimeMillis()));
			logRepo.close();
		}

		scheduler.moveToNextEvent();
	}
}
