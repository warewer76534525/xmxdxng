package com.triplelands.sd.activitypage.smstemplate;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.triplelands.sd.activitypage.BaseCustomTitle;
import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.storage.SmsTemplateRepository;
import com.triplelands.sd.view.SMSCategoryAdapter;

public class SmsCategoryListActivity extends BaseCustomTitle {
	private ListView _listCategory;
	private SmsTemplateRepository _smsTemplateRepo;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.sms_category_list);
		
		_listCategory = (ListView) findViewById(R.id.listViewKategori);
		_smsTemplateRepo = new SmsTemplateRepository(this);
		populateListCategory();
	}
	
	private void populateListCategory() {
		
		_smsTemplateRepo.open();

		Cursor cursor = _smsTemplateRepo.getAllCategories();
		Log.i("SD_Category", "Total: " + cursor.getCount());
//		startManagingCursor(cursor);

		_listCategory.setAdapter(new SMSCategoryAdapter(this, cursor));
		_smsTemplateRepo.close();
		_listCategory.setOnItemClickListener(new ItemOnClick());
	}
	
	private class ItemOnClick implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			int idCategory = Integer.parseInt((String) view.getTag());
			
			Intent i = new Intent(SmsCategoryListActivity.this, SmsTemplatesListActivity.class);
			i.putExtra("idCategory", idCategory);
			startActivity(i);
		}
	};
}
