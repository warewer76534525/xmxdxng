package com.triplelands.sd.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.triplelands.sd.activitypage.R;

public class EventNotification {
	private NotificationManager _mNotificationManager;
	private Notification _notifyDetails;
	private int _id;
	private Context _context;
	private String _alert, _title, _content;
	private Class<?> _destinationClass;
	private int idDestinationCategory;
	
	public EventNotification(Context context, int id, String alert, String title, String content, Class<?> destClass, int idCategory) {
		_id = id;
		_context = context;
		_alert = alert;
		_title = title;
		_content = content;
		_destinationClass = destClass;
		idDestinationCategory = idCategory;
		
		initNotification(_context, _alert);
	}
	
	public void show(){
		showNotification(_context, _title, _content, _destinationClass);
	}
	
	public void clear(){
		if(_mNotificationManager != null) _mNotificationManager.cancelAll();
	}
	
	private void initNotification(Context context, String alert) {
		_mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		_notifyDetails = new Notification(R.drawable.icon_notif_red, alert, System.currentTimeMillis());

		long[] vibrate = { 100, 100, 200, 300 };
		_notifyDetails.vibrate = vibrate;
		_notifyDetails.defaults = Notification.DEFAULT_ALL;
	}
	
	private void showNotification(Context context, String title, String text, Class<?> destClass) {
		Intent notifyIntent = new Intent(context, destClass);
		notifyIntent.putExtra("idCategory", idDestinationCategory);
		PendingIntent intent = PendingIntent.getActivity(context, 0, notifyIntent, android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
		_notifyDetails.setLatestEventInfo(context, title, text, intent);
		_mNotificationManager.notify(_id, _notifyDetails);
	}
}
