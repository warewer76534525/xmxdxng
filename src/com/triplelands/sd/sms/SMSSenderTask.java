package com.triplelands.sd.sms;

import android.os.AsyncTask;

public class SMSSenderTask extends AsyncTask<Void, String, Void> {

	private SendMessageHandler _handler;
	private String _destination, _message;
	private int _count;
	
	public SMSSenderTask(SendMessageHandler handler, String destination, String msg, int count) {
		_handler = handler;
		_destination = destination;
		_message = msg;
		_count = count;
	}
	
	protected Void doInBackground(Void... params) {
		SmsSender sender = new SmsSender();
		for(int i=0; i < _count; i++){
			sender.send(_destination, _message);
			_handler.onSendProgress(i+1);
		}
		_handler.onSentComplete(_count);
		return null;
	}

}
