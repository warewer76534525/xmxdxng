package com.triplelands.sd.activitypage.smstemplate;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.triplelands.sd.activitypage.BaseCustomTitle;
import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.storage.SmsTemplateRepository;
import com.triplelands.sd.view.SMSTemplatesAdapter;

public class SmsTemplatesListActivity extends BaseCustomTitle {

	private ListView _lvTemplates;
	private ImageButton _btnBack;
	private SmsTemplateRepository _smsTemplateRepo;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_template_list);
		_smsTemplateRepo = new SmsTemplateRepository(this);
		popoulateListTemplate();
	}

	private void popoulateListTemplate() {
		TextView txtJlh = (TextView) findViewById(R.id.txtJumlahTemplate);
		_lvTemplates = (ListView) findViewById(R.id.listViewTemplate);
		_btnBack = (ImageButton) findViewById(R.id.btnBackTemplate);
		_btnBack.setVisibility(View.GONE);

		_smsTemplateRepo.open();

		Cursor cursor = _smsTemplateRepo
				.getAllSmsTemplateByCategory(getIntent().getExtras().getInt(
						"idCategory"));
		Log.i("SD_Category", "Total template: " + cursor.getCount());
//		startManagingCursor(cursor);

		_lvTemplates.setAdapter(new SMSTemplatesAdapter(this, cursor));
		txtJlh.setText(cursor.getCount()
				+ " SMS "
				+ _smsTemplateRepo.getCategoryName(getIntent().getExtras()
						.getInt("idCategory")));
		_smsTemplateRepo.close();
		_lvTemplates.setOnItemClickListener(new ItemOnClick());
	}

	private class ItemOnClick implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int idSms = Integer.parseInt((String) view.getTag());

			Intent i = new Intent(SmsTemplatesListActivity.this, SmsContentActivity.class);
			i.putExtra("idSms", idSms);
			startActivity(i);
		}
	};
}
