package com.triplelands.sd.activitypage.smstemplate;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.triplelands.sd.activitypage.BaseCustomTitle;
import com.triplelands.sd.activitypage.ContactListActivity;
import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.activitypage.group.PickGroupActivity;
import com.triplelands.sd.sms.Contact;
import com.triplelands.sd.sms.Message;
import com.triplelands.sd.sms.SmsSender;
import com.triplelands.sd.sms.style.AlayGenerator;
import com.triplelands.sd.sms.style.AntiAlayGenerator;
import com.triplelands.sd.smstemplate.SmsTemplate;
import com.triplelands.sd.storage.SmsTemplateRepository;

public class SmsContentActivity extends BaseCustomTitle {

	private final int PICK_CONTACT_CODE = 1;
	private final int PICK_GROUP_CODE = 2;
	private SmsTemplate _sms;
	private TextView _txtIsiSms;
	private CheckBox _ckAlay, _ckAntiAlay;
	private Button _btnSendContact, _btnSendGroup;
	private ImageButton _btnClipboard;
	private String _originalSMS;
	private ClipboardManager _clipboard;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_content);
		
		_clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

		SmsTemplateRepository smsTemplateRepo = new SmsTemplateRepository(this);
		int idSms = getIntent().getExtras().getInt("idSms");
		System.out.println("id sms: " + idSms);
		smsTemplateRepo.open();

		_sms = smsTemplateRepo.getSmsTemplate(idSms);
		_txtIsiSms = (TextView) findViewById(R.id.txtSMSContent);
		_originalSMS = _sms.getIsi();
		_txtIsiSms.setText(_originalSMS);

		TextView txtTitle = (TextView) findViewById(R.id.txtTitleIsi);
		txtTitle.setText(_sms.getTitle());

		smsTemplateRepo.close();

		_ckAlay = (CheckBox) findViewById(R.id.ckAlay);
		_ckAntiAlay = (CheckBox) findViewById(R.id.ckAntiAlay);
		_btnSendContact = (Button) findViewById(R.id.btnSendToContact);
		_btnSendGroup = (Button) findViewById(R.id.btnSendToGroup);
		_btnClipboard = (ImageButton) findViewById(R.id.btnClipBoard);
		_ckAlay.setOnCheckedChangeListener(new OnCheckChangeListener());
		_ckAntiAlay.setOnCheckedChangeListener(new OnCheckChangeListener());
		_btnSendContact.setOnClickListener(new OnButtonClickListener());
		_btnSendGroup.setOnClickListener(new OnButtonClickListener());
		_btnClipboard.setOnClickListener(new OnButtonClickListener());
	}

	private class OnCheckChangeListener implements OnCheckedChangeListener {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (buttonView == _ckAlay) {
				if (isChecked) {
					_ckAntiAlay.setChecked(false);
					_txtIsiSms.setText(AlayGenerator
							.hurufBesarKecil(AlayGenerator
									.hurufJadiAngka(_txtIsiSms.getText()
											.toString())));
				} else {
					_txtIsiSms.setText(_originalSMS);
				}
			}
			if (buttonView == _ckAntiAlay) {
				if (isChecked) {
					_ckAlay.setChecked(false);
					_txtIsiSms.setText(AntiAlayGenerator.decrypt(_txtIsiSms
							.getText().toString()));
				} else {
					_txtIsiSms.setText(_originalSMS);
				}
			}
		}
	}

	private class OnButtonClickListener implements OnClickListener {
		public void onClick(View v) {
			if (v == _btnSendContact) {
				startActivityForResult(new Intent(SmsContentActivity.this,
						ContactListActivity.class), PICK_CONTACT_CODE);
			}
			if (v == _btnSendGroup) {
				startActivityForResult(new Intent(SmsContentActivity.this,
						PickGroupActivity.class), PICK_GROUP_CODE);
			}
			if (v == _btnClipboard) {
				copyToClipBoard(_txtIsiSms.getText().toString());
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == PICK_CONTACT_CODE
					|| requestCode == PICK_GROUP_CODE) {
				List<Contact> destinationList = (ArrayList<Contact>) data
						.getExtras().getSerializable("contacts");
				showSendingConfirmation(destinationList);
			}
		}
	}

	private void showSendingConfirmation(final List<Contact> listContact) {
		StringBuilder sb = new StringBuilder();
		for (Contact contact : listContact) {
			sb.append("\n* " + contact.getName());
		}
		new AlertDialog.Builder(SmsContentActivity.this).setIcon(
				android.R.drawable.ic_dialog_info).setMessage(
				"Kirim pesan SMS ke:" + sb.toString()).setPositiveButton("Ya",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int sumthin) {
						SmsSender sender = new SmsSender();
						sender.send(new Message(listContact, _txtIsiSms
								.getText().toString()));
						Toast.makeText(SmsContentActivity.this,
								"SMS Terkirim.", Toast.LENGTH_SHORT).show();
					}
				}).setNegativeButton("Tidak",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int sumthin) {
					}
				}).show();
	}
	
	private void copyToClipBoard(String text){
		_clipboard.setText(text);
		Toast.makeText(this, "SMS sudah dicopy ke clipboard", Toast.LENGTH_SHORT).show();
	}

}
