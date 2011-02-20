package com.triplelands.sd.specialevent;

import com.triplelands.sd.util.DateTime;

public class SpecialEvent {

	private Integer id;
	private Integer category;
	private String nama;
	private Integer tanggal;
	private Integer bulan;
	private Integer jam;
	private Integer menit;

	public SpecialEvent() {
	}
	
	/**
	 * Special event ini mengingat tanggal dan bulan saja tetapi tahun tidak
	 * dicatat. juga mencatat jam waktu notifikasi sehingga kita harus
	 * mengkonstruk date time yang tepat dari database
	 * DateTime.thisYear(tanggal, bulan, jam);
	 */
	
	public SpecialEvent(Integer id, Integer idCategory, String name,
			Integer tanggal, Integer bulan, Integer jam, Integer menit) {
		this.id = id;
		this.category = idCategory;
		this.nama = name;
		this.tanggal = tanggal;
		this.bulan = bulan;
		this.jam = jam;
		this.menit = menit;
	}

	public Integer getId() {
		return id;
	}

	public Integer getIdCategory() {
		return category;
	}

	public String getName() {
		return nama;
	}
	
	public Integer getTanggal() {
		return tanggal;
	}

	public Integer getBulan() {
		return bulan;
	}

	public Integer getJam() {
		return jam;
	}

	public Integer getMenit() {
		return menit;
	}

	public DateTime getTime() {
		return DateTime.thisYear(tanggal, bulan, jam, menit);
	}

	public long getTimeMillis() {
		return DateTime.thisYear(tanggal, bulan, jam, menit).toMilis();
	}

}
