package com.triplelands.sd.activitypage;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedBackActivity extends BaseCustomTitle {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review_page);

		final String imei = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE))
				.getDeviceId();
		final int sdkVersion = Integer.parseInt(Build.VERSION.SDK);

		final EditText txtFeedback = (EditText) findViewById(R.id.txtSaran);
		Button btnKirimFeedBack = (Button) findViewById(R.id.btnKirim);

		btnKirimFeedBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (txtFeedback.getText().toString().equals("")) {
					Toast.makeText(FeedBackActivity.this,
							"Silakan isi saran/kritik/pertanyaan kamu.",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent sendIntent = new Intent(Intent.ACTION_SEND);
					sendIntent.putExtra(Intent.EXTRA_EMAIL,
							new String[] { "triplelands@gmail.com" });
					sendIntent.putExtra(Intent.EXTRA_TEXT, txtFeedback
							.getText().toString()+ "\n\nsmsdong_device_imei: "
							+ imei + "; smsdong_device_sdk: " + sdkVersion);
					sendIntent.putExtra(Intent.EXTRA_SUBJECT,
							"Kritik & Saran");
					sendIntent.setType("message/rfc822"); 
					startActivity(sendIntent);
					finish();
				}
			}
		});
	}
}
