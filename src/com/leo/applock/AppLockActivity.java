package com.leo.applock;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class AppLockActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		findViewById(R.id.unlock_button).setOnClickListener(this);
		findViewById(R.id.setting_gesture_unlock).setOnClickListener(this);
		findViewById(R.id.verify_gesture_unlock).setOnClickListener(this);

		if (!isServiceWork(this, "com.leo.applock.AppLockService")) {
			Intent intent = new Intent(this, AppLockService.class);
			startService(intent);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.unlock_button:
			finish();
			break;
		case R.id.setting_gesture_unlock:
			Intent settingIntent = new Intent(this, GestureEditActivity.class);
			startActivity(settingIntent);
			break;
		case R.id.verify_gesture_unlock:
			Intent verifyIntent = new Intent(this, GestureVerifyActivity.class);
			startActivity(verifyIntent);
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {

	}

	public static boolean isServiceWork(Context context, String serviceName) {
		boolean isWork = false;
		ActivityManager myAM = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> myList = myAM.getRunningServices(Integer.MAX_VALUE);
		if (myList.size() <= 0) {
			return false;
		}
		for (int i = 0; i < myList.size(); i++) {
			String mName = myList.get(i).service.getClassName().toString();
			if (mName.equals(serviceName)) {
				isWork = true;
				break;
			}
		}
		return isWork;
	}

}
