package com.triplelands.sd.log;

public class AppLog {
	
	private int id;
	private int type;
	private String nama;
	private String deskripsi;
	private long date;
	
	public static final int LOG_TYPE_MY_SCHEDULE = 0;
	public static final int LOG_TYPE_SPECIAL_EVENT = 1;
	
	public AppLog() {
	}
	
	public AppLog(int type, String nama, String deskripsi, long date){
		this.type = type;
		this.nama = nama;
		this.deskripsi = deskripsi;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public int getType() {
		return type;
	}

	public String getNama() {
		return nama;
	}

	public String getDeskripsi() {
		return deskripsi;
	}

	public long getDate() {
		return date;
	}
}
