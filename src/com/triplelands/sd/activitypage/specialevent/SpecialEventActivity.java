package com.triplelands.sd.activitypage.specialevent;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

import com.triplelands.sd.activitypage.R;

public class SpecialEventActivity extends TabActivity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.special_event);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        
        Resources res = getResources();
    	TabHost tabHost = getTabHost();
    	TabHost.TabSpec tabSpec;
    	Intent intent;
    	
        intent = new Intent().setClass(this, SpecialEventListActivity.class);
        tabSpec = tabHost.newTabSpec("Special Event Schedule").
        setIndicator("Special Event", res.getDrawable(R.drawable.event)).
        setContent(intent);
        tabHost.addTab(tabSpec);
        
//        intent = new Intent().setClass(this, EventTemplateListActivity.class);
//        tabSpec = tabHost.newTabSpec("Event Templates").
//        setIndicator("Events", res.getDrawable(R.drawable.schedule_icon)).
//        setContent(intent);
//        tabHost.addTab(tabSpec);
        
        intent = new Intent().setClass(this, SpecialEventSettingActivity.class);
        tabSpec = tabHost.newTabSpec("Pengaturan").
        setIndicator("Pengaturan", res.getDrawable(R.drawable.setting)).
        setContent(intent);
        tabHost.addTab(tabSpec);
        
        tabHost.setCurrentTab(0);
	}
}
