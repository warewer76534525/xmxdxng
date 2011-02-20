package com.triplelands.sd.specialevent;

import com.triplelands.sd.util.DateTime;

public class SEvent {
	private int id;
	private long waktu;
	private boolean completed;
	
	public SEvent() {
	}

	public SEvent(int id, long waktu) {
		this.id = id;
		this.waktu = waktu;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getWaktu() {
		return waktu;
	}

	public void setWaktu(long waktu) {
		this.waktu = waktu;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	public void markAsCompleted() {
		this.completed = true;
	}

	public void moveToCurrentYear() {
		completed = false;
		waktu = DateTime.fromMilis(waktu).currentYear().toMilis();
	}

	public boolean sudahLewat() {
		return waktu < DateTime.today().toMilis();
	}

	public void moveToNextYear() {
		completed = false;
		waktu = DateTime
			.fromMilis(waktu)
			.nextYear()
			.toMilis();
	}

	public void changeNotificationTime(Integer currentHour,
			Integer currentMinute) {
		waktu = DateTime
			.fromMilis(waktu)
			.changeTime(currentHour, currentMinute)
			.toMilis();
	}

	public boolean isToday() {
		return DateTime.fromMilis(waktu).isToday();
	}
}

