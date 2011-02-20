package com.triplelands.sd.activitypage.group;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.triplelands.sd.activitypage.BaseCustomTitle;
import com.triplelands.sd.activitypage.ContactListActivity;
import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.sms.Contact;
import com.triplelands.sd.sms.Group;
import com.triplelands.sd.storage.GroupRepository;

public class AddGroupActivity extends BaseCustomTitle {

	private TextView _txtContactList;
	private EditText _txtGroupName;
	private ImageButton _btnPickMember;
	private int PICK_CONTACT_CODE = 0;
	private List<Contact> _listMember;
	private Button _btnSaveGroup;
	private GroupRepository _groupRepository;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_group);
		
		_txtGroupName = (EditText) findViewById(R.id.txtGroupName);
		_txtContactList = (TextView) findViewById(R.id.txtContactList);
		_btnPickMember = (ImageButton) findViewById(R.id.btnPickMember);
		_btnSaveGroup = (Button) findViewById(R.id.btnSaveGroup);
		_groupRepository = new GroupRepository(this);
		
		_btnPickMember.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivityForResult(new Intent(AddGroupActivity.this,
						ContactListActivity.class), PICK_CONTACT_CODE);
			}
		});
		_btnSaveGroup.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(_txtGroupName.getText().toString().equals("")){
					Toast.makeText(AddGroupActivity.this, "Nama group masih kosong.", Toast.LENGTH_SHORT).show();
				}else if(_listMember == null || _listMember.size() == 0){
					Toast.makeText(AddGroupActivity.this, "Silakan pilih anggota group.", Toast.LENGTH_SHORT).show();
				}else{
					_groupRepository.open();
					_groupRepository.save(new Group(_txtGroupName.getText().toString().trim(), _listMember));
					_groupRepository.close();
					finish();
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(RESULT_OK == resultCode){
			if(PICK_CONTACT_CODE == requestCode){
				_listMember = (ArrayList<Contact>)data.getExtras().getSerializable("contacts");
				StringBuilder sb = new StringBuilder();
				for (Contact contact : _listMember) {
					sb.append("* " + contact.getName() + "\n");
				}
				_txtContactList.setText(sb.toString());
			}
		}
	}
}
