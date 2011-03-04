package com.triplelands.sd.activitypage.smstemplate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.triplelands.sd.activitypage.BaseCustomTitle;
import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.storage.SmsTemplateRepository;
import com.triplelands.sd.view.SMSTemplatesAdapter;

public class SmsTemplatesListActivity extends BaseCustomTitle {

	private ListView _lvTemplates;
	private ImageButton _btnBack;
	private SmsTemplateRepository _smsTemplateRepo;
	private int _idCategory;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_template_list);
		_smsTemplateRepo = new SmsTemplateRepository(this);
		_idCategory = getIntent().getExtras().getInt("idCategory");
	}
	
	protected void onResume() {
		super.onResume();
		popoulateListTemplate();
	}

	private void popoulateListTemplate() {
		TextView txtJlh = (TextView) findViewById(R.id.txtJumlahTemplate);
		_lvTemplates = (ListView) findViewById(R.id.listViewTemplate);
		_btnBack = (ImageButton) findViewById(R.id.btnBackTemplate);
		_btnBack.setVisibility(View.GONE);

		_smsTemplateRepo.open();

		Cursor cursor = _smsTemplateRepo
				.getAllSmsTemplateByCategory(_idCategory);
		Log.i("SD_Category", "Total template: " + cursor.getCount());
//		startManagingCursor(cursor);

		_lvTemplates.setAdapter(new SMSTemplatesAdapter(this, cursor));
		txtJlh.setText(cursor.getCount()
				+ " SMS "
				+ _smsTemplateRepo.getCategoryName(getIntent().getExtras()
						.getInt("idCategory")));
		_smsTemplateRepo.close();
		_lvTemplates.setOnItemClickListener(new ItemOnClick());
		_lvTemplates.setOnItemLongClickListener(new ItemOnLongClick());
	}

	private class ItemOnLongClick implements OnItemLongClickListener {
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int pos, long id) {
				showDeleteDialog(Integer.parseInt((String) view.getTag()));
			return false;
		}
	}
	
	private void showDeleteDialog(final int id) {
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		alertbox.setTitle("Hapus Template");
		alertbox.setMessage("Kamu yakin ingin hapus template ini?");
		alertbox.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				_smsTemplateRepo.open();
				_smsTemplateRepo.deleteTemplate(id);
				_smsTemplateRepo.close();
				onResume();
				Toast.makeText(getApplicationContext(), "Template sms sudah dihapus.",
						Toast.LENGTH_LONG).show();
			}
		});
		alertbox.setNegativeButton("Tidak",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
		alertbox.show();
	}
	
	private class ItemOnClick implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int idSms = Integer.parseInt((String) view.getTag());

			Intent i = new Intent(SmsTemplatesListActivity.this, SmsContentActivity.class);
			i.putExtra("idSms", idSms);
			startActivity(i);
		}
	};
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuaddTemplate:
			Intent i = new Intent(this, NewTemplateActivity.class);
			i.putExtra("idCategory", _idCategory);
			startActivity(i);
			break;
		}
		return false;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.template_menu, menu);
		setMenuBackground();
		return true;
	}

	private void setMenuBackground() {
		getLayoutInflater().setFactory(new Factory() {
			public View onCreateView(String name, Context context,
					AttributeSet attrs) {
				if (name
						.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
					try {
						LayoutInflater f = getLayoutInflater();
						final View view = f.createView(name, null, attrs);
						new Handler().post(new Runnable() {
							public void run() {
								view
										.setBackgroundResource(R.drawable.brown_background);
							}
						});
						return view;
					} catch (InflateException e) {
					} catch (ClassNotFoundException e) {
					}
				}
				return null;
			}
		});
	}
}
