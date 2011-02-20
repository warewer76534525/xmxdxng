package com.triplelands.sd.sms.style;

public class AntiAlayGenerator {
	private final static String TABLE_HURUF = "AEGIOSZ";
	private final static String TABLE_ANGKA = "4361052";

	public static String decrypt(String alay) {
		String hasilAkhir = alay;
		for (int i = 0; i < TABLE_HURUF.length(); i++) {
			hasilAkhir = hasilAkhir.replace(TABLE_ANGKA.charAt(i),
					TABLE_HURUF.charAt(i));
		}
		return hasilAkhir.toLowerCase();
	}
}
