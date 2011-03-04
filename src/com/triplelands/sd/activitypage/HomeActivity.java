package com.triplelands.sd.activitypage;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.triplelands.sd.activitypage.group.GroupListActivity;
import com.triplelands.sd.activitypage.group.PickGroupActivity;
import com.triplelands.sd.activitypage.myschedule.MyScheduleActivity;
import com.triplelands.sd.activitypage.smstemplate.SmsCategoryListActivity;
import com.triplelands.sd.activitypage.specialevent.SpecialEventActivity;
import com.triplelands.sd.app.AppNotification;
import com.triplelands.sd.app.SpecialEventScheduler;
import com.triplelands.sd.sms.Contact;
import com.triplelands.sd.sms.Message;
import com.triplelands.sd.sms.SmsSender;
import com.triplelands.sd.storage.LogRepository;
import com.triplelands.sd.storage.Repository;
import com.triplelands.sd.view.LogAdapter;

public class HomeActivity extends BaseCustomTitle {
	
	private final int PICK_CONTACT_CODE = 0;
	private final int PICK_GROUP_CODE = 1;
	
	private ListView _listLog;
	private LogRepository _logRepo;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.main);
		_listLog = (ListView) findViewById(R.id.listViewLog);
		_logRepo = new LogRepository(this);
		
		Repository repository = new Repository(this);
		repository.init();
		
//		SmsTemplateRetriever smsRetriever = new SmsTemplateRetriever(this);
//		smsRetriever.initializeForTheFirstTimeUse();
		
//		SpecialEventRetriever eventRetriever = new SpecialEventRetriever(this);
//		eventRetriever.initializeForTheFirstTimeUse();
		
		SpecialEventScheduler scheduler = new SpecialEventScheduler(this);
		scheduler.init();
	}
	
	protected void onResume() {
		super.onResume();
		AppNotification.getInstance().clear();
		_logRepo.open();

		Cursor cursor = _logRepo.getAllLog();
		Log.i("SD_LOG", "Total: " + cursor.getCount());
		startManagingCursor(cursor);
		
		if(cursor.getCount() > 0) ((TextView)findViewById(R.id.txtEmpty)).setVisibility(View.GONE);
		else ((TextView)findViewById(R.id.txtEmpty)).setVisibility(View.VISIBLE);
		
		_listLog.setAdapter(new LogAdapter(this, cursor));
		_logRepo.close();
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
	    setMenuBackground();
	    return true;
	}
	
	private void setMenuBackground(){
        getLayoutInflater().setFactory( new Factory() {
            public View onCreateView ( String name, Context context, AttributeSet attrs ) {
                if ( name.equalsIgnoreCase( "com.android.internal.view.menu.IconMenuItemView" ) ) {
                    try {
                        LayoutInflater f = getLayoutInflater();
                        final View view = f.createView( name, null, attrs );
                        new Handler().post( new Runnable() {
                            public void run () {
                                view.setBackgroundResource(R.drawable.brown_background);
                            }
                        } );
                        return view;
                    }
                    catch ( InflateException e ) {}
                    catch ( ClassNotFoundException e ) {}
                }
                return null;
            }
        });
    }
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menuMySchedule:
				startActivity(new Intent(this, MyScheduleActivity.class));
				break;
			case R.id.menuSpecialEvent:
				startActivity(new Intent(this, SpecialEventActivity.class));
				break;
			case R.id.menuGroup:
				startActivity(new Intent(this, GroupListActivity.class));
				break;
			case R.id.menuTemplate:
				startActivity(new Intent(this, SmsCategoryListActivity.class));
				break;
			case R.id.menuTellFriend:
				showTellFriendDialog();
				break;
			case R.id.menuFeedback:
				startActivity(new Intent(this, FeedBackActivity.class));
				break;
			case R.id.menuClearLog:
				clearLog();
				break;
			case R.id.menuSmsBomber:
				startActivity(new Intent(this, SMSBomberActivity.class));
				break;
		}
		return false;
	}
	
	private void clearLog(){
		_logRepo.open();
		_logRepo.clearLog();
		_logRepo.close();
		onResume();
		Toast.makeText(this, "Log History sudah dihapus.", Toast.LENGTH_SHORT).show();
	}
	
	private void showTellFriendDialog(){
		new AlertDialog.Builder(HomeActivity.this)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setMessage("SMS Ajakan untuk gabung menggunakan aplikasi ini akan dikirimkan kepada teman-teman Anda. Silakan pilih teman yang ingin kamu ajak.")
			.setPositiveButton("Pilih Contact",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int sumthin) {
						startActivityForResult(new Intent(HomeActivity.this, ContactListActivity.class), PICK_CONTACT_CODE);
					}
				})
			.setNegativeButton("Pilih Group",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int sumthin) {
						startActivityForResult(new Intent(HomeActivity.this, PickGroupActivity.class), PICK_GROUP_CODE);
					}
				})
			.show();
	}
	
	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == PICK_CONTACT_CODE
					|| requestCode == PICK_GROUP_CODE) {
				List<Contact> destinationList = (ArrayList<Contact>) data
						.getExtras().getSerializable("contacts");
				showSendingConfirmation(destinationList);
			}
		}
	}
	
	private void showSendingConfirmation(final List<Contact> listContact) {
		StringBuilder sb = new StringBuilder();
		for (Contact contact : listContact) {
			sb.append("\n* " + contact.getName());
		}
		new AlertDialog.Builder(HomeActivity.this).setIcon(
				android.R.drawable.ic_dialog_info).setMessage(
				"Kirim SMS ajakan ke:" + sb.toString()).setPositiveButton("Ya",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int sumthin) {
						SmsSender sender = new SmsSender();
						sender.send(new Message(listContact, "Ayo gabung gunakan aplikasi SMSDong di Android kmu! Ketik 'SMSDong' di Android market lgsg dr hp kmu! Keren!"));
						Toast.makeText(HomeActivity.this,
								"SMS Terkirim.", Toast.LENGTH_SHORT).show();
					}
				}).setNegativeButton("Tidak",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int sumthin) {
					}
				}).show();
	}
}