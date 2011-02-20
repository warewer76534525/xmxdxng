package com.triplelands.sd.activitypage.myschedule;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.triplelands.sd.activitypage.BaseCustomTitle;
import com.triplelands.sd.activitypage.ContactListActivity;
import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.activitypage.group.PickGroupActivity;
import com.triplelands.sd.app.SmsScheduler;
import com.triplelands.sd.sms.Contact;
import com.triplelands.sd.sms.Message;
import com.triplelands.sd.sms.style.AlayGenerator;
import com.triplelands.sd.sms.style.AntiAlayGenerator;
import com.triplelands.sd.util.DateTime;

public class AddMyScheduleActivity extends BaseCustomTitle {

	private SmsScheduler _smsScheduler;
	private EditText _txtScheduleName, _txtScheduleMessage, _txtDestination,
			_txtScheduleDate, _txtScheduleTime;
	private Button _btnPickDate, _btnSetDatePicker, _btnPickTime,
			_btnSetTimePicker, _btnSummary, _btnSave, _btnNext;
	private ImageButton _btnBack, _btnPickMessage, _btnPickContact,
			_btnPickGroup;
	// private Spinner _spinnerInterval;
	private Dialog _scheduleDatePicker, _scheduleTimePicker;
	private DatePicker _dtPicker;
	private TimePicker _timePicker;
	private int _day, _month, _year, _hour, _minute;
	private LinearLayout _layoutDescCategory, _layoutTimeCategory;
	private CheckBox _ckAlay, _ckAntiAlay;
	private String _originalMessage;
	private boolean _isStyleChanged;

	private final int PICK_TEMPLATE_CODE = 0;
	private final int PICK_CONTACT_CODE = 1;
	private final int PICK_GROUP_CODE = 2;
	private ArrayList<Contact> _destinationList;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_schedule);

		initDialog();
		initViewComponent();

	}

	private class OnCheckChangeListener implements OnCheckedChangeListener {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			_isStyleChanged = true;
			if (buttonView == _ckAlay) {
				if (!_txtScheduleMessage.getText().toString().equals("")) {
					if (isChecked) {
						_ckAntiAlay.setChecked(false);
						_txtScheduleMessage.setText(AlayGenerator
								.hurufBesarKecil(AlayGenerator
										.hurufJadiAngka(_txtScheduleMessage
												.getText().toString())));
					} else {
						_txtScheduleMessage.setText(_originalMessage);
					}
				}

			}
			if (buttonView == _ckAntiAlay) {
				if (!_txtScheduleMessage.getText().toString().equals("")) {
					if (isChecked) {
						_ckAlay.setChecked(false);
						_txtScheduleMessage.setText(AntiAlayGenerator
								.decrypt(_txtScheduleMessage.getText()
										.toString()));
					} else {
						_txtScheduleMessage.setText(_originalMessage);
					}
				}
			}
			_isStyleChanged = false;
		}
	}

	private class ButtonOnClick implements OnClickListener {

		public void onClick(View v) {
			if (v == _btnPickMessage) {
				startActivityForResult(new Intent(AddMyScheduleActivity.this,
						MyScheduleSMSTemplateActivity.class),
						PICK_TEMPLATE_CODE);
			}
			if (v == _btnPickContact) {
				startActivityForResult(new Intent(AddMyScheduleActivity.this,
						ContactListActivity.class), PICK_CONTACT_CODE);
			}
			if (v == _btnPickGroup) {
				startActivityForResult(new Intent(AddMyScheduleActivity.this,
						PickGroupActivity.class), PICK_GROUP_CODE);
			}
			if (v == _btnSetDatePicker) {
				populateDateField();
				_scheduleDatePicker.dismiss();
			}
			if (v == _btnPickDate) {
				_scheduleDatePicker.show();
			}
			if (v == _btnSetTimePicker) {
				populateTimeField();
				_scheduleTimePicker.dismiss();
			}
			if (v == _btnPickTime) {
				_scheduleTimePicker.show();
			}
			if (v == _btnSummary) {
				Toast.makeText(AddMyScheduleActivity.this, "Show Summary",
						Toast.LENGTH_SHORT).show();
			}
			if (v == _btnNext) {
				_layoutDescCategory.setVisibility(View.GONE);
				_layoutTimeCategory.setVisibility(View.VISIBLE);
			}
			if (v == _btnBack) {
				_layoutDescCategory.setVisibility(View.VISIBLE);
				_layoutTimeCategory.setVisibility(View.GONE);
			}
			if (v == _btnSave) {
				if (_txtScheduleName.getText().toString().equals("")) {
					Toast.makeText(AddMyScheduleActivity.this, "Nama schedule masih kosong.", Toast.LENGTH_SHORT).show();
				} else if(_txtScheduleMessage.getText().toString().equals("")) {
					Toast.makeText(AddMyScheduleActivity.this, "Pesan schedule masih kosong.", Toast.LENGTH_SHORT).show();
				} else if(_txtDestination.getText().toString().equals("")) {
					Toast.makeText(AddMyScheduleActivity.this, "Nomor tujuan masih kosong.", Toast.LENGTH_SHORT).show();
				} else {
					scheduleSmsSender(_destinationList, _txtScheduleMessage
							.getText().toString(), DateTime.dateTime(_day,
							_month, _year, _hour, _minute));
					Toast.makeText(AddMyScheduleActivity.this,
							"Schedule tersimpan.", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		}
	}

	private TextWatcher textWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
			if (!_isStyleChanged) {
				_originalMessage = s.toString();
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == PICK_TEMPLATE_CODE) {
				String isiSms = data.getExtras().getString("isiSms");
				_txtScheduleMessage.setText(isiSms);
			}
			if (requestCode == PICK_CONTACT_CODE
					|| requestCode == PICK_GROUP_CODE) {
				_destinationList = (ArrayList<Contact>) data.getExtras()
						.getSerializable("contacts");

				StringBuilder sb = new StringBuilder();
				for (Contact contact : _destinationList) {
					sb.append(contact.getName() + ";");
				}
				_txtDestination.setText(sb.toString());
			}
		}
	}

	private void initViewComponent() {
		_layoutDescCategory = (LinearLayout) findViewById(R.id.layoutDescCategory);
		_layoutTimeCategory = (LinearLayout) findViewById(R.id.layoutTimeCategory);
		_txtScheduleName = (EditText) findViewById(R.id.txtScheduleName);
		_txtScheduleMessage = (EditText) findViewById(R.id.txtScheduleMessage);
		_txtDestination = (EditText) findViewById(R.id.txtScheduleDestination);
		_txtScheduleDate = (EditText) findViewById(R.id.txtScheduleDate);
		_txtScheduleTime = (EditText) findViewById(R.id.txtScheduleTime);
		_btnPickMessage = (ImageButton) findViewById(R.id.btnPickMessage);
		_btnPickContact = (ImageButton) findViewById(R.id.btnPickNo);
		_btnPickGroup = (ImageButton) findViewById(R.id.btnPickGroup);
		_btnPickDate = (Button) findViewById(R.id.btnSetDate);
		_btnPickTime = (Button) findViewById(R.id.btnSetTime);
		_btnSave = (Button) findViewById(R.id.btnSave);
		_btnSummary = (Button) findViewById(R.id.btnSummary);
		_btnNext = (Button) findViewById(R.id.btnNextCategory);
		_btnBack = (ImageButton) findViewById(R.id.btnBackCategory);
		_ckAlay = (CheckBox) findViewById(R.id.ckAlay);
		_ckAntiAlay = (CheckBox) findViewById(R.id.ckAntiAlay);
		_ckAlay.setOnCheckedChangeListener(new OnCheckChangeListener());
		_ckAntiAlay.setOnCheckedChangeListener(new OnCheckChangeListener());
		_btnPickDate.setOnClickListener(new ButtonOnClick());
		_btnPickTime.setOnClickListener(new ButtonOnClick());
		_btnPickMessage.setOnClickListener(new ButtonOnClick());
		_btnPickContact.setOnClickListener(new ButtonOnClick());
		_btnPickGroup.setOnClickListener(new ButtonOnClick());
		_btnSave.setOnClickListener(new ButtonOnClick());
		_btnSummary.setOnClickListener(new ButtonOnClick());
		_btnNext.setOnClickListener(new ButtonOnClick());
		_btnBack.setOnClickListener(new ButtonOnClick());
		_txtScheduleMessage.addTextChangedListener(textWatcher);

		populateDateField();
		populateTimeField();
	}

	private void initDialog() {
		_scheduleDatePicker = new Dialog(this);
		_scheduleDatePicker.setContentView(R.layout.date_picker_dialog);
		_scheduleDatePicker.setCancelable(true);
		_dtPicker = (DatePicker) _scheduleDatePicker
				.findViewById(R.id.dtSchedule);
		_btnSetDatePicker = (Button) _scheduleDatePicker
				.findViewById(R.id.btnSetDtPicker);
		_btnSetDatePicker.setOnClickListener(new ButtonOnClick());

		_scheduleTimePicker = new Dialog(this);
		_scheduleTimePicker.setContentView(R.layout.time_picker_dialog);
		_scheduleTimePicker.setCancelable(true);
		_timePicker = (TimePicker) _scheduleTimePicker
				.findViewById(R.id.timeSchedule);
		_btnSetTimePicker = (Button) _scheduleTimePicker
				.findViewById(R.id.btnSetTimePicker);
		_btnSetTimePicker.setOnClickListener(new ButtonOnClick());
	}

	private void populateDateField() {
		_day = _dtPicker.getDayOfMonth();
		_month = _dtPicker.getMonth() + 1;
		_year = _dtPicker.getYear();
		_txtScheduleDate.setText(_day + "-" + _month + "-" + _year);
	}

	private void populateTimeField() {
		_hour = _timePicker.getCurrentHour();
		_minute = _timePicker.getCurrentMinute();
		_txtScheduleTime.setText(DateTime.to2CharFormat(_hour) + " : "
				+ DateTime.to2CharFormat(_minute));
	}

	private void scheduleSmsSender(List<Contact> destinations, String message,
			DateTime schedule) {
		_smsScheduler = new SmsScheduler(this);
		_smsScheduler.schedule(_txtScheduleName.getText().toString(), Message
				.create(destinations, message), schedule);
	}
}
