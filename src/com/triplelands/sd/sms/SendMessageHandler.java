package com.triplelands.sd.sms;

public interface SendMessageHandler {
	public void onSendProgress(int sent);
	public void onSentComplete(int total);
}
