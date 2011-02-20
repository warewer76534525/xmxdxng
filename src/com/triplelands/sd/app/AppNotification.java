package com.triplelands.sd.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.triplelands.sd.activitypage.R;

public class AppNotification {
	private static AppNotification _instance;
	private NotificationManager _mNotificationManager;
	private Notification _notifyDetails;
	private int NOTIFICATION_ID;
	private int _unreadNotificationNum;
	
	private AppNotification() {
	}
	
	public static AppNotification getInstance() {
        if (_instance == null) {
        	_instance = new AppNotification();
        }
        return _instance;
    }
	
	public void show(Context context, String alert, String title, String content, Class<?> destClass){
		initNotification(context, alert);
		
		if(_unreadNotificationNum > 0){
			title = "Schedule SMS Terkirim.";
			content = (_unreadNotificationNum + 1) + " schedule sudah terkirim.";
		}
		showNotification(context, title, content, destClass);
	}
	
	public void clear(){
		_unreadNotificationNum = 0;
		if(_mNotificationManager != null) _mNotificationManager.cancelAll();
	}
	
	private void initNotification(Context context, String alert) {
		_mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		_notifyDetails = new Notification(R.drawable.icon_notif, alert, System.currentTimeMillis());

		long[] vibrate = { 100, 100, 200, 300 };
		_notifyDetails.vibrate = vibrate;
		_notifyDetails.defaults = Notification.DEFAULT_ALL;
	}
	
	private void showNotification(Context context, String title, String text, Class<?> destClass) {
		Intent notifyIntent = new Intent(context, destClass);
		PendingIntent intent = PendingIntent.getActivity(context, 0, notifyIntent, android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
		_notifyDetails.setLatestEventInfo(context, title, text, intent);
		_mNotificationManager.notify(NOTIFICATION_ID, _notifyDetails);
		_unreadNotificationNum++;
	}
}
