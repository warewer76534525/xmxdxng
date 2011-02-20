package com.triplelands.sd.sms;

import com.triplelands.sd.util.DateTime;
/**
 * Kelas ini digunakan utk menampung satu task yang ada di storage database
 * @author Welly
 */
public class SmsTask {
	
	private Integer id;
	private String name;
	private Message message;
	private DateTime schedule;
	
	public SmsTask(Integer id, String name, Message message, DateTime schedule) {
		this.id = id;
		this.name = name;
		this.message = message;
		this.schedule = schedule;
	}

	public Message getMessage() {
		return message;
	}

	public Integer getId() {
		return id;
	}
	
	public String getName(){
		return name;
	}

	public DateTime getSchedule() {
		return schedule;
	}

	public long getScheduleMilis() {
		return schedule.toMilis();
	}

}
