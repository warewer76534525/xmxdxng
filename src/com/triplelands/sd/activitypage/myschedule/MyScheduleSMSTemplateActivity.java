package com.triplelands.sd.activitypage.myschedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.smstemplate.SmsTemplate;
import com.triplelands.sd.storage.SmsTemplateRepository;
import com.triplelands.sd.view.SMSCategoryAdapter;
import com.triplelands.sd.view.SMSTemplatesAdapter;

public class MyScheduleSMSTemplateActivity extends Activity {

	private ListView _lvCategory, _lvTemplates;
	private ImageButton _btnBack;
	private SmsTemplateRepository _smsTemplateRepo;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Pilih Template SMS");
		_smsTemplateRepo = new SmsTemplateRepository(this);
	}

	protected void onResume() {
		super.onResume();
		showTemplateCategory();
	}

	private void showTemplateCategory() {
		setContentView(R.layout.sms_category_list);
		populateListCategory();
	}

	private void showSMSTemplateList(int idCategory) {
		setContentView(R.layout.sms_template_list);
		populateListTemplate(idCategory);
	}
	
	private class ItemOnClick implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
			if (parent == _lvCategory) {
				int idCategory = Integer.parseInt((String) v.getTag());
				showSMSTemplateList(idCategory);
			}
			if (parent == _lvTemplates) {
				int smsId = Integer.parseInt((String) v.getTag());
				showIsiSMS(smsId);
			}
		}
	}

	private void populateListCategory() {
		_lvCategory = (ListView) findViewById(R.id.listViewKategori);
		_smsTemplateRepo.open();

		Cursor cursor = _smsTemplateRepo.getAllCategories();
		Log.i("SD_Category", "Total: " + cursor.getCount());
		startManagingCursor(cursor);

		_lvCategory.setAdapter(new SMSCategoryAdapter(this, cursor));
		_smsTemplateRepo.close();
		_lvCategory.setOnItemClickListener(new ItemOnClick());
	}

	private void populateListTemplate(int idCategory) {
		TextView txtJlh = (TextView) findViewById(R.id.txtJumlahTemplate);
		_lvTemplates = (ListView) findViewById(R.id.listViewTemplate);
		_btnBack = (ImageButton) findViewById(R.id.btnBackTemplate);
		_btnBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showTemplateCategory();
			}
		});

		_smsTemplateRepo.open();

		Cursor cursor = _smsTemplateRepo
				.getAllSmsTemplateByCategory(idCategory);
		Log.i("SD_Category", "Total template: " + cursor.getCount());
		startManagingCursor(cursor);

		_lvTemplates.setAdapter(new SMSTemplatesAdapter(this, cursor));
		txtJlh.setText(cursor.getCount() + " SMS "
				+ _smsTemplateRepo.getCategoryName(idCategory));
		_smsTemplateRepo.close();
		_lvTemplates.setOnItemClickListener(new ItemOnClick());
	}

	private void showIsiSMS(int smsId) {
		_smsTemplateRepo.open();
		final SmsTemplate sms = _smsTemplateRepo.getSmsTemplate(smsId);
		final View smsView = LayoutInflater.from(
				MyScheduleSMSTemplateActivity.this).inflate(
				R.layout.isi_sms_dialog, null);
		((TextView) smsView.findViewById(R.id.txtTitleIsi)).setText(sms
				.getTitle());
		((TextView) smsView.findViewById(R.id.txtIsiSMS)).setText(sms.getIsi());
		new AlertDialog.Builder(MyScheduleSMSTemplateActivity.this).setIcon(
				android.R.drawable.ic_menu_send).setView(smsView)
				.setPositiveButton("Pilih",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								pilihIsiSms(sms.getIsi());
							}
						}).setNegativeButton("Tutup",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show();
		_smsTemplateRepo.close();
	}
	
	private void pilihIsiSms(String isi){
		Intent resultIntent = new Intent();
		resultIntent.putExtra("isiSms", isi);
		setResult(RESULT_OK, resultIntent);
		finish();
	}

}
