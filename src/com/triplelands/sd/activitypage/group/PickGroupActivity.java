package com.triplelands.sd.activitypage.group;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.sms.Contact;
import com.triplelands.sd.storage.GroupRepository;
import com.triplelands.sd.view.GroupAdapter;

public class PickGroupActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pick_group_list);
		setTitle("Pilih Group");

		ListView listGroup = (ListView) findViewById(R.id.listViewGroup);

		GroupRepository groupRepo = new GroupRepository(this);
		groupRepo.open();

		Cursor cursor = groupRepo.getAllGroup();
		Log.i("SD_Group", "Total: " + cursor.getCount());
		startManagingCursor(cursor);

		if (cursor.getCount() == 0)
			((TextView) findViewById(R.id.txtPickgroup))
					.setText("Belum ada group yang terdaftar. Silahkan tambah Group baru di menu 'Group'.");

		listGroup.setAdapter(new GroupAdapter(this, cursor));
		groupRepo.close();

		listGroup.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int idGroup = Integer.parseInt((String) view.getTag());
				pickGroup(idGroup);
			}
		});
	}

	private void pickGroup(int id) {
		GroupRepository groupRepo = new GroupRepository(this);
		groupRepo.open();
		Intent resultIntent = new Intent();
		LinkedList<Contact> listContact = (LinkedList<Contact>) groupRepo.getContacts(id);
		resultIntent.putExtra("contacts", listContact);
		setResult(RESULT_OK, resultIntent);
		groupRepo.close();

		finish();
	}
}
