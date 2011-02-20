package com.triplelands.sd.specialevent;

import java.util.ArrayList;
import java.util.List;

import com.triplelands.sd.util.DateTime;

public class EventChain {
	private List<SEvent> _events = new ArrayList<SEvent>();
	private int nextEventIdx = 0;

	public EventChain(int nextEventId, List<SEvent> eventList) {
		_events = eventList;
		nextEventIdx = nextEventId;
	}

	public EventChain() {
	}

	public int getNextEventIdx() {
		return nextEventIdx;
	}

	public List<SEvent> getEvents() {
		return _events;
	}

	public SEvent getNextEvent() {
		return _events.get(nextEventIdx - 1);
	}
	
	public void nextEvent() {
		// atasi akhir chain dan atasi pergantian tahun
		if (nextEventIdx == _events.size()) {

			moveChainToNextYear();
			
			nextEventIdx = 0;
		} 
		
		nextEventIdx++;
	}

	private void moveChainToNextYear() {
		for (SEvent event : _events) {
			event.moveToNextYear();
		}
	}

	private void moveChainToCurrentYear() {
		for (SEvent event : _events) {
			event.moveToCurrentYear();
		}
	}

	public void markAsComplete(int id) {
		_events.get(id).markAsCompleted();
	}

	public void addEvent(SpecialEvent specialEvent) {

		SEvent sevent = new SEvent(specialEvent.getId(),
				specialEvent.getTimeMillis());
		_events.add(sevent);

//		if (sevent.sudahLewat()) {
//			sevent.markAsCompleted();
//		} else if (nextEventIdx == 0) {
//			nextEventIdx = sevent.getId();
//		}
	}

	public void markOldEventAsComplete() {
		if (DateTime.fromMilis(_events.get(0).getWaktu()).isThisYear()) {
			for (int i = nextEventIdx; i < _events.size(); i++) {
				SEvent sevent = _events.get(i);
				if (sevent.sudahLewat()) {
					sevent.markAsCompleted();
					if (i == _events.size() - 1) {
						moveChainToNextYear();
						nextEvent();
					}
				} else {
					nextEventIdx = sevent.getId();
					break;
				}
			}
		} else {
			moveChainToCurrentYear();
			markOldEventAsComplete();
		}
	}

	public void updateNotificationTime(int idEvent, Integer currentHour,
			Integer currentMinute) {
		SEvent event = _events.get(idEvent - 1);
		event.changeNotificationTime(currentHour, currentMinute);
	}
}
