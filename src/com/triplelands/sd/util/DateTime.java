package com.triplelands.sd.util;

import java.util.Calendar;

public class DateTime {
	private Calendar calendar;

	public DateTime() {
		calendar = trimToDays(Calendar.getInstance());
	}

	public DateTime(Calendar calendar) {
		this.calendar = calendar;
	}

	public DateTime(int jam) {
		calendar = trimToDays(Calendar.getInstance());
		calendar.set(Calendar.HOUR_OF_DAY, jam);
	}

	public DateTime(int jam, int menit) {
		calendar = trimToDays(Calendar.getInstance());
		setTime(jam, menit);
	}

	public DateTime(int hari, int bulan, int tahun) {
		calendar = trimToDays(Calendar.getInstance());
		setDay(hari, bulan, tahun);
	}

	private void setDay(int hari, int bulan, int tahun) {
		calendar.set(Calendar.DATE, hari);
		calendar.set(Calendar.MONTH, bulan - 1);
		calendar.set(Calendar.YEAR, tahun);
	}

	private void setTime(int jam, int menit) {
		calendar.set(Calendar.HOUR_OF_DAY, jam);
		calendar.set(Calendar.MINUTE, menit);
	}

	public DateTime(int hari, int bulan, int tahun, int jam, int menit) {
		calendar = trimToDays(Calendar.getInstance());
		setDay(hari, bulan, tahun);
		setTime(jam, menit);
	}

	public int getDayOfWeek() {
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	public String getDayName(int dayOfWeek) {
		String name = null;
		switch (dayOfWeek) {
		case Calendar.SUNDAY:
			name = "Minggu";
			break;
		case Calendar.MONDAY:
			name = "Senin";
			break;
		case Calendar.TUESDAY:
			name = "Selasa";
			break;
		case Calendar.WEDNESDAY:
			name = "Rabu";
			break;
		case Calendar.THURSDAY:
			name = "Kamis";
			break;
		case Calendar.FRIDAY:
			name = "Jumat";
			break;
		case Calendar.SATURDAY:
			name = "Sabtu";
			break;
		}
		return name;
	}

	public int getDate() {
		return calendar.get(Calendar.DATE);
	}

	public int getMonth() {
		return calendar.get(Calendar.MONTH) + 1;
	}

	public int getYear() {
		return calendar.get(Calendar.YEAR);
	}

	public int getHour() {
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public int getMinute() {
		return calendar.get(Calendar.MINUTE);
	}

	public static int getTodayDate() {
		return today().getDate();
	}

	public static int getTodayMonth() {
		return today().getMonth();
	}

	private Calendar trimToDays(Calendar result) {
		result.set(Calendar.HOUR_OF_DAY, 0);
		result.set(Calendar.MINUTE, 0);
		result.set(Calendar.SECOND, 0);
		result.set(Calendar.MILLISECOND, 0);
		return result;
	}

	public static DateTime today() {
		return new DateTime();
	}

	public long toMilis() {
		return calendar.getTimeInMillis();
	}

	public static String to2CharFormat(int time) {
		return ((String.valueOf(time)).length() == 1) ? ("0" + time) : (String
				.valueOf(time));
	}

	public static DateTime today(int jam) {
		return new DateTime(jam);
	}

	public static DateTime today(int jam, int menit) {
		return new DateTime(jam, menit);
	}

	public static DateTime date(int hari, int bulan, int tahun) {
		return new DateTime(hari, bulan, tahun);
	}

	public static DateTime dateTime(int hari, int bulan, int tahun, int jam,
			int menit) {
		return new DateTime(hari, bulan, tahun, jam, menit);
	}

	public static DateTime thisYear(int hari, int bulan, int jam, int menit) {
		return new DateTime(hari, bulan, today().getYear(), jam, menit);
	}

	public static DateTime fromMilis(long milis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milis);
		return new DateTime(calendar);
	}

	public DateTime currentYear() {
		return DateTime.thisYear(getDate(), getMonth(), getHour(), getMinute());
	}

	public boolean isThisYear() {
		Calendar todayCalendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR);
	}

	public DateTime nextYear() {
		return DateTime.dateTime(getDate(), getMonth(), getYear() + 1,
				getHour(), getMinute());
	}

	public DateTime changeTime(Integer currentHour, Integer currentMinute) {
		return DateTime.dateTime(getDate(), getMonth(), getYear(), currentHour,
				currentMinute);
	}

	public boolean isToday() {
		return 
			today().getYear() == this.getYear() && 
			today().getMonth() == this.getMonth() && 
			today().getDate() == this.getDate();
	}
}