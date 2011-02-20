package com.triplelands.sd.app;

import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.triplelands.sd.specialevent.EventChain;
import com.triplelands.sd.specialevent.SEvent;
import com.triplelands.sd.specialevent.SpecialEvent;
import com.triplelands.sd.storage.EventChainRepository;
import com.triplelands.sd.storage.SpecialEventRepository;
import com.triplelands.sd.util.DateTime;

public class SpecialEventScheduler {
	private SpecialEventRepository _specialEventRepository;
	private EventChainRepository _eventChainRepository;
	private boolean HAS_ALREADY_SCHEDULED = false;
	private AlarmManager _alarmManager;
	private Context _context;

	public SpecialEventScheduler(Context context) {
		_specialEventRepository = new SpecialEventRepository(context);
		_eventChainRepository = new EventChainRepository(context);

		_alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		_context = context;
	}

	private void initSpecialEventChain() {
		_eventChainRepository.open();
		if (!_eventChainRepository.isAlreadyIntialize()) {
			EventChain eventChain = new EventChain();

			// TODO perlu dibuat template pattern utk open close db
			_specialEventRepository.open();
			List<SpecialEvent> events = _specialEventRepository
					.getAllEventObjects();
			_specialEventRepository.close();

			for (SpecialEvent specialEvent : events) {
				eventChain.addEvent(specialEvent);
			}
			eventChain.markOldEventAsComplete();
			
			_eventChainRepository.save(eventChain);
			
		}
		_eventChainRepository.close();
	}

	public void init() {
		if (HAS_ALREADY_SCHEDULED)
			return;

		initSpecialEventChain();

		scheduleNextIncomingEvent();

		HAS_ALREADY_SCHEDULED = true;
	}

	/**
	 * AlarmManager dengan requestcode yg sama dan pendingintent yg berbeda bisa
	 * dieksekusi (bisa diparalelkan dengan smsscheduler)
	 * 
	 * set alarm manager berdasarkan waktu event
	 **/
	public void scheduleNextIncomingEvent() {
		_eventChainRepository.open();
		EventChain eventChain = _eventChainRepository.getInstance();
		_eventChainRepository.close();

		SEvent event = eventChain.getNextEvent();

		DateTime waktuEvent = DateTime.fromMilis(event.getWaktu());

		Log.i("NEXT EVENT - SEVENT: ",
				event.getId() + " " + waktuEvent.getDate() + "-"
						+ waktuEvent.getMonth() + "-" + waktuEvent.getYear());
		Log.i("NEXT EVENT - SEVENT: ",
				event.getId() + " " + waktuEvent.getHour() + "-"
						+ waktuEvent.getMinute());
		_specialEventRepository.open();
		SpecialEvent specialEvent = _specialEventRepository.getEvent(event
				.getId());
		_specialEventRepository.close();

		Intent intent = new Intent(_context, SpecialEventSchedulerHandler.class);
		intent.putExtra("eventId", specialEvent.getId());
		intent.putExtra("catId", specialEvent.getIdCategory());
		intent.putExtra("eventName", specialEvent.getName());

		Log.i("NEXT EVENT: ",
				specialEvent.getName() + " " + specialEvent.getJam() + ":"
						+ specialEvent.getMenit());

		PendingIntent senderIntent = PendingIntent
				.getBroadcast(_context, specialEvent.getId(), intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
		Log.i("Event.GetWaktu()", ""+event.getWaktu());
		_alarmManager.set(AlarmManager.RTC_WAKEUP, event.getWaktu(),
				senderIntent);
	}

	private void markOldEventExecuted() {
		_eventChainRepository.open();
		EventChain chain = _eventChainRepository.getInstance();
		chain.markOldEventAsComplete();
		_eventChainRepository.update(chain);
		_eventChainRepository.close();
	}

	public void moveToNextEvent() {
		_eventChainRepository.open();

		EventChain eventChain = _eventChainRepository.getInstance();
		eventChain.nextEvent();

		_eventChainRepository.update(eventChain);
		_eventChainRepository.close();

		scheduleNextIncomingEvent();
	}

	public void prepareForStartUp() {
		initSpecialEventChain();
		markOldEventExecuted();
		scheduleNextIncomingEvent();
		HAS_ALREADY_SCHEDULED = true;
	}

	public void updateNotificationTime(int idEvent, Integer currentHour,
			Integer currentMinute) {
		
		_specialEventRepository.open();
		_specialEventRepository.updateTime(idEvent, currentHour, currentMinute);
		_specialEventRepository.close();
		
		_eventChainRepository.open();
		
		EventChain eventChain = _eventChainRepository.getInstance();
		eventChain.updateNotificationTime(idEvent, currentHour, currentMinute);
		
		_eventChainRepository.update(eventChain);
		_eventChainRepository.close();
		
		scheduleNextIncomingEvent();
	}

	public boolean isTodayEvent() {
		_eventChainRepository.open();
		EventChain eventChain = _eventChainRepository.getInstance();
		_eventChainRepository.close();
		
		return eventChain.getNextEvent().isToday();
	}
}
