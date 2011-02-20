package com.triplelands.sd.sms.style;

public class AlayGenerator {
	private final static String TABLE_HURUF = "AEGIOSZ";
	private final static String TABLE_ANGKA = "4361052";
	private final static String TABLE_VOCAL = "AIUEO";

	public static String hurufBesarKecil(String asli) {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < asli.length(); i++) {
			int seed = (int) Math.round(2 * Math.random());

			if (seed % 2 == 0)
				builder.append(("" + asli.charAt(i)).toLowerCase());
			else
				builder.append(("" + asli.charAt(i)).toUpperCase());

		}
		return builder.toString();
	}

	public static String hurufJadiAngka(String asli) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < asli.length(); i++) {
			
			int acak = (int) Math.round(2 * Math.random());
			int terganti = 0;
			
			if (acak % 2 == 0) {

				for (int j = 0; j < TABLE_HURUF.length(); j++) {
					if (("" + asli.charAt(i)).toUpperCase().equals(
							"" + TABLE_HURUF.charAt(j))) {
						builder.append(TABLE_ANGKA.charAt(j));
						terganti = 1;
						break;
					}
				}
			}

			if (terganti == 0)
				builder.append(asli.charAt(i));
		}
		
		return builder.toString();
	}

	public static String disingkatSingkat(String asli) {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < asli.length(); i++) {
			int acak = (int) Math.round(2 * Math.random());
			int terganti = 0;
			
			if (acak % 2 == 0) {

				for (int j = 0; j < TABLE_VOCAL.length(); j++) {
					if ((asli.charAt(i) + "").toUpperCase().equals(
							"" + TABLE_VOCAL.charAt(j))) {
						if ((asli.charAt(i - 1) != ' ') && (i > 0)) {
							// stemp=stemp+TabelAngka.charAt(j); hilangkan saja
							terganti = 1;
						}
						break;
					}
				}
			}

			if (terganti == 0)
				builder.append(asli.charAt(i));
		}

		return builder.toString();
	}
}
