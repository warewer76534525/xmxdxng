package com.triplelands.sd.activitypage.group;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.triplelands.sd.activitypage.BaseCustomTitle;
import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.storage.GroupRepository;
import com.triplelands.sd.view.GroupAdapter;

public class GroupListActivity extends BaseCustomTitle {

	private ListView _listGroup;
	private GroupRepository _groupRepo;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_list);

		_groupRepo = new GroupRepository(this);
		_listGroup = (ListView) findViewById(R.id.listViewGroup);
		Button btnAddGroup = (Button) findViewById(R.id.btnAddGroup);
		btnAddGroup.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				 startActivity(new Intent(GroupListActivity.this, AddGroupActivity.class));
			}
		});
		_listGroup.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				int groupId = Integer.parseInt((String)view.getTag());
				Intent i = new Intent(GroupListActivity.this, GroupMemberListActivity.class);
				i.putExtra("groupId", groupId);
				startActivity(i);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		_groupRepo.open();

		Cursor cursor = _groupRepo.getAllGroup();
		Log.i("SD_Group", "Total: " + cursor.getCount());
		startManagingCursor(cursor);
		
		if(cursor.getCount() > 0) ((TextView)findViewById(R.id.txtEmpty)).setVisibility(View.GONE);
		else ((TextView)findViewById(R.id.txtEmpty)).setVisibility(View.VISIBLE);
		
		((TextView)findViewById(R.id.txtTotalGroup)).setText("(" + cursor.getCount() + ")");
		
		_listGroup.setAdapter(new GroupAdapter(this, cursor));
		_groupRepo.close();
	}
}
