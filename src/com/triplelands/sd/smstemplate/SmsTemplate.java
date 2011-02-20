package com.triplelands.sd.smstemplate;


public class SmsTemplate {
	private long id;
	private String title;
	private String isi;
	private long category;
	
	public SmsTemplate() {
	}

	public SmsTemplate(long id, String title, String isi) {
		this.id = id;
		this.title = title;
		this.isi = isi;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsi() {
		return isi;
	}

	public void setIsi(String isi) {
		this.isi = isi;
	}
	
	public long getCategory() {
		return category;
	}
	
	public void setCategory(long category) {
		this.category = category;
	}
}
