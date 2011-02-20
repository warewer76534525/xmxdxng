package com.triplelands.sd.sms;

public class PhoneNumber {
	private String phoneNumber;

	public PhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public static PhoneNumber parse(String phoneNumber) {
		if (phoneNumber.substring(0, 1).equals("0")) {
			phoneNumber = "+62" + phoneNumber.substring(1);
		}

		return new PhoneNumber(phoneNumber);
	}

}
