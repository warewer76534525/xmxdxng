package com.triplelands.sd.activitypage.group;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.triplelands.sd.activitypage.BaseCustomTitle;
import com.triplelands.sd.activitypage.ContactListActivity;
import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.sms.Contact;
import com.triplelands.sd.storage.GroupRepository;
import com.triplelands.sd.view.GroupMemberAdapter;
import com.triplelands.sd.view.GroupMemberAdapter.ViewHolder;

public class GroupMemberListActivity extends BaseCustomTitle {

	private int _groupId;
	private GroupRepository _groupRepository;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_member_list);
	}

	@Override
	protected void onResume() {
		super.onResume();

		_groupId = getIntent().getExtras().getInt("groupId");

		ListView listMember = (ListView) findViewById(R.id.listViewMember);
		_groupRepository = new GroupRepository(this);
		_groupRepository.open();
		listMember.setAdapter(new GroupMemberAdapter(this, _groupRepository
				.getContacts(_groupId)));
		_groupRepository.close();
		
		listMember.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int pos, long id) {
				showDeleteDialog(_groupId, ((ViewHolder)view.getTag()).txtNumber.getText().toString());
				return false;
			}
		});
	}
	
	private void showDeleteDialog(final int id, final String number) {
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		alertbox.setTitle("Hapus Contact");
		alertbox.setMessage("Kamu yakin ingin hapus contact ini?");
		alertbox.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				_groupRepository.open();
				_groupRepository.deleteContact(_groupId, number);
				_groupRepository.close();
				onResume();
				Toast.makeText(getApplicationContext(), "Contact sudah dihapus.",
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

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuaddGroupMember:
			startActivityForResult(new Intent(this, ContactListActivity.class),
					0);
			break;
		}
		return false;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contact_menu, menu);
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

	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			List<Contact> contacts = (ArrayList<Contact>) data.getExtras()
					.getSerializable("contacts");
			_groupRepository.open();
			_groupRepository.addContacts(_groupId, contacts);
			_groupRepository.close();
			Toast.makeText(this, "Anggota group sudah ditambah.", Toast.LENGTH_SHORT).show();
			onResume();
		}
	}
}
