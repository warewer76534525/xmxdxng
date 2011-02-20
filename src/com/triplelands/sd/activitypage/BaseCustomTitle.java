package com.triplelands.sd.activitypage;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;

import com.triplelands.sd.app.UncaughtExceptionHandler;

public class BaseCustomTitle extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(this));
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		FrameLayout layout = new FrameLayout(this);
        setContentView(layout);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
	}
}
