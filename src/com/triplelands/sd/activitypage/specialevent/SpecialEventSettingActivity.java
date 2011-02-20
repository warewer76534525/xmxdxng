package com.triplelands.sd.activitypage.specialevent;

import android.app.Activity;
import android.os.Bundle;

import com.triplelands.sd.activitypage.R;

public class SpecialEventSettingActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		getListView().setBackgroundColor(Color.TRANSPARENT);
//        getListView().setCacheColorHint(Color.TRANSPARENT);
//        getListView().setDivider(this.getResources().getDrawable(R.drawable.divider));
		setContentView(R.layout.special_event_setting);

//		Preference customPref = (Preference) findPreference("customPref");
//		customPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			public boolean onPreferenceClick(Preference preference) {
//				Toast.makeText(getBaseContext(),
//						"The custom preference has been clicked",
//						Toast.LENGTH_LONG).show();
//				SharedPreferences customSharedPreference = getSharedPreferences(
//						"myCustomSharedPrefs", Activity.MODE_PRIVATE);
//				SharedPreferences.Editor editor = customSharedPreference
//						.edit();
//				editor.putString("myCustomPref",
//						"The preference has been clicked");
//				editor.commit();
//				return true;
//			}
//		});
	}
}
