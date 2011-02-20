/*
 * Copyright (C) 2007-2010 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package com.triplelands.sd.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.triplelands.sd.activitypage.R;

public class BugReportActivity extends Activity {
	static final String STACKTRACE = "fbreader.stacktrace";

	private String getVersionName() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (Exception e) {
			return "";
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bug_report_view);
		final String imei = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE))
		.getDeviceId();
		final int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
		final String stackTrace = getIntent().getStringExtra(STACKTRACE);
		final TextView reportTextView = (TextView) findViewById(R.id.report_text);
		reportTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
		reportTextView.setClickable(false);
		reportTextView.setLongClickable(false);

		final String versionName = getVersionName();
		reportTextView
				.append("Ups. SMS Dong " + versionName + " mengalami kesalahan. Maaf atas ketidaknyamanan ini. Kami harap Kamu dapat melaporkan kesalahan ini agar bisa segera kami perbaiki. Laporan akan dikirimkan via email. Terima kasih!\n\n");
//		 reportTextView.append(stackTrace);

		findViewById(R.id.send_report).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						Intent sendIntent = new Intent(Intent.ACTION_SEND);
						sendIntent.putExtra(Intent.EXTRA_EMAIL,
								new String[] { "exception.smsdong@gmail.com" });
						sendIntent.putExtra(Intent.EXTRA_TEXT, stackTrace + "\n\nimei: " + imei + "\nsdk_version: " + sdkVersion + "\n" +
								"URI: " + Phone.CONTENT_URI.toString() + "\n" + 
								"URI absolute: " + ContactsContract.CommonDataKinds.Phone.CONTENT_URI.toString());
						sendIntent.putExtra(Intent.EXTRA_SUBJECT,
								"Laporan Kesalahan Aplikasi SMSDong");
						sendIntent.setType("message/rfc822");
						startActivity(sendIntent);
						finish();
					}
				});
	}
}
