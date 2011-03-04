package com.triplelands.sd.sms;

import java.util.ArrayList;

import android.telephony.SmsManager;

public class SmsSender {
	
	public void send(String phoneNumber, String messageToSent) {
		
		PhoneNumber number = PhoneNumber.parse(phoneNumber);

		SmsManager sms = SmsManager.getDefault();
		System.out.println("Sending SMS to: " + phoneNumber + ", message: "
				+ messageToSent);
		
		ArrayList<String> messageArray = sms.divideMessage(messageToSent);
		sms.sendMultipartTextMessage(number.getPhoneNumber(), null, messageArray, null, null);
	}
	
	public void send(Message message) {
		for (Contact contact : message.getDestinations()) {
			send(contact.getNumber(), message.getContent());	
		}
	}
	
	public void send(Message message, String footer){
		String msgToSent = message.getContent() + "\n\n" + footer;
		
		for (Contact contact : message.getDestinations()) {
			send(contact.getNumber(), msgToSent);	
		}
	}
}
