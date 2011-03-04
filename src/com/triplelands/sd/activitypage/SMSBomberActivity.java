package com.triplelands.sd.activitypage;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.triplelands.sd.sms.SMSSenderTask;
import com.triplelands.sd.sms.SendMessageHandler;

public class SMSBomberActivity extends BaseCustomTitle implements
		SendMessageHandler {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_bomber);

		final EditText txtNoTujuan = (EditText) findViewById(R.id.txtNomorTujuanBomber);
		final EditText txtSMS = (EditText) findViewById(R.id.txtSMSBomber);
		final EditText txtJumlahSMS = (EditText) findViewById(R.id.txtJumlahSms);
		Button btnBomb = (Button) findViewById(R.id.btnBomb);
		btnBomb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(txtNoTujuan.getText().toString().equals("") ||
						txtJumlahSMS.getText().toString().equals("") ||
						txtSMS.getText().toString().equals("")){
					Toast.makeText(SMSBomberActivity.this, "Silakan isi semua data.", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(SMSBomberActivity.this, "SMS Bomber dieksekusi.", Toast.LENGTH_SHORT).show();
					SMSSenderTask senderTask = new SMSSenderTask(
							SMSBomberActivity.this, txtNoTujuan.getText()
									.toString(), txtSMS.getText().toString(),
							Integer.parseInt(txtJumlahSMS.getText().toString()));
					senderTask.execute();
					txtJumlahSMS.setText("");
					txtNoTujuan.setText("");
					txtSMS.setText("");
				}
			}
		});
	}

	public void onSendProgress(int sent) {
		System.out.println("progress: " + sent);
	}

	public void onSentComplete(int total) {
		Looper.prepare();
		System.out.println("completed");
		Toast.makeText(this, total + " SMS Bomber sudah terkirim.", Toast.LENGTH_SHORT).show();
		Looper.loop();
	}
}
