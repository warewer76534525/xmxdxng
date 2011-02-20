package com.triplelands.sd.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.triplelands.sd.app.SmsScheduler;
import com.triplelands.sd.app.SpecialEventScheduler;

/**
 * Kelas ini akan dijalankan pada saat android device startup. 
 * Pada saat startup maka alarm langsung di schedule utk task yang terdekat  
 * @author Welly
 *
 */
public class StartupReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) return;
		
		Log.i("SmsScheduler", "scheduler.scheduleNextIncomingTask()");
		
		SmsScheduler scheduler = new SmsScheduler(context);
		scheduler.scheduleNextIncomingTask();
		
		SpecialEventScheduler specialScheduler = new SpecialEventScheduler(context);
		specialScheduler.prepareForStartUp();
	}
}
