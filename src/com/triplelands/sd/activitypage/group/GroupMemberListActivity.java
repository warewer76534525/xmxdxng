package com.triplelands.sd.activitypage.group;

import android.os.Bundle;
import android.widget.ListView;

import com.triplelands.sd.activitypage.BaseCustomTitle;
import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.storage.GroupRepository;
import com.triplelands.sd.view.GroupMemberAdapter;

public class GroupMemberListActivity extends BaseCustomTitle {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_member_list);
		
		int groupId = getIntent().getExtras().getInt("groupId");
		
		ListView listMember = (ListView) findViewById(R.id.listViewMember);
		GroupRepository groupRepository = new GroupRepository(this);
		groupRepository.open();
		listMember.setAdapter(new GroupMemberAdapter(this, groupRepository.getContacts(groupId)));
		groupRepository.close();
	}
}
