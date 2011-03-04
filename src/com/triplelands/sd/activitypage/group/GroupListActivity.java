package com.triplelands.sd.activitypage.group;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.triplelands.sd.activitypage.BaseCustomTitle;
import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.storage.GroupRepository;
import com.triplelands.sd.view.GroupAdapter;

public class GroupListActivity extends BaseCustomTitle {

	private ListView _listGroup;
	private GroupRepository _groupRepo;
	private int _contextItemId;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_list);

		_groupRepo = new GroupRepository(this);
		_listGroup = (ListView) findViewById(R.id.listViewGroup);
		Button btnAddGroup = (Button) findViewById(R.id.btnAddGroup);
		btnAddGroup.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(GroupListActivity.this,
						AddGroupActivity.class));
			}
		});
		_listGroup.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int groupId = Integer.parseInt((String) view.getTag());
				Intent i = new Intent(GroupListActivity.this,
						GroupMemberListActivity.class);
				i.putExtra("groupId", groupId);
				startActivity(i);
			}
		});
		_listGroup.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int pos, long id) {
				_contextItemId = Integer.parseInt((String) view.getTag());
				return false;
			}
		});
		registerForContextMenu(_listGroup);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Group Menu");
		menu.add(0, _contextItemId, 0, "Ubah Nama Group");
		menu.add(0, _contextItemId, 1, "Hapus Group");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getOrder() == 0) {
			initRenameGroupDialog(item.getItemId());
		} else if (item.getOrder() == 1) {
			showDeleteDialog(item.getItemId());
		} else {
			return false;
		}
		return true;
	}

	private void showDeleteDialog(final int id) {
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		alertbox.setTitle("Hapus Group");
		alertbox.setMessage("Kamu yakin ingin hapus group ini?");
		alertbox.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				_groupRepo.open();
				_groupRepo.deleteGroup(id);
				_groupRepo.close();
				onResume();
				Toast.makeText(getApplicationContext(), "Group sudah dihapus.",
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

	@Override
	protected void onResume() {
		super.onResume();
		_groupRepo.open();

		Cursor cursor = _groupRepo.getAllGroup();
		Log.i("SD_Group", "Total: " + cursor.getCount());
		startManagingCursor(cursor);

		if (cursor.getCount() > 0)
			((TextView) findViewById(R.id.txtEmpty)).setVisibility(View.GONE);
		else
			((TextView) findViewById(R.id.txtEmpty))
					.setVisibility(View.VISIBLE);

		((TextView) findViewById(R.id.txtTotalGroup)).setText("("
				+ cursor.getCount() + ")");

		_listGroup.setAdapter(new GroupAdapter(this, cursor));
		_groupRepo.close();
	}

	private void initRenameGroupDialog(final int id) {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.textbox_dialog);
		dialog.setTitle("Ubah Nama Group");
		dialog.setCancelable(true);
		dialog.show();
		
		TextView tv = (TextView)dialog.findViewById(R.id.txtMessage);
		tv.setText("Silakan masukkan nama Group yang baru:");
		final EditText txtGroupBaru = (EditText) dialog.findViewById(R.id.txtDialog);
		final Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
		btnSubmit.setText("Ubah Nama");
		btnSubmit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(txtGroupBaru.getText().toString().trim().equals("")){
					Toast.makeText(GroupListActivity.this, "Nama group tidak valid", Toast.LENGTH_SHORT).show();
				} else {
					_groupRepo.open();
					_groupRepo.renameGroup(id, txtGroupBaru.getText().toString().trim());
					_groupRepo.close();
					onResume();
					Toast.makeText(getApplicationContext(), "Nama Group sudah diperbaharui.",
							Toast.LENGTH_LONG).show();
					dialog.dismiss();
				}
			}
		});
	}
}
