package com.triplelands.sd.activitypage.myschedule;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

import com.triplelands.sd.activitypage.R;

public class MyScheduleActivity extends TabActivity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.my_schedule);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        
        Resources res = getResources();
    	TabHost tabHost = getTabHost();
    	TabHost.TabSpec tabSpec;
    	Intent intent;
    	
        intent = new Intent().setClass(this, MyScheduleListActivity.class);
        tabSpec = tabHost.newTabSpec("Schedule").
        setIndicator("Schedule", res.getDrawable(R.drawable.schedule_icon)).
        setContent(intent);
        tabHost.addTab(tabSpec);
        
        intent = new Intent().setClass(this, MyScheduleSettingActivity.class);
        tabSpec = tabHost.newTabSpec("Pengaturan").
        setIndicator("Pengaturan", res.getDrawable(R.drawable.setting)).
        setContent(intent);
        tabHost.addTab(tabSpec);
        
        tabHost.setCurrentTab(0);
    }	
}
