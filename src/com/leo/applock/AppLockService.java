package com.leo.applock;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class AppLockService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		TimerTask task = new TimerTask() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				ComponentName topActivity = activityManager.getRunningTasks(1).get(0).topActivity;
				String packageName = topActivity.getPackageName();
				if (packageName.equals("com.android.settings")) {
					// 这里只对“设置”加锁
					Intent intent = new Intent();
					intent.setClass(AppLockService.this, AppLockActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}

		};

		Timer timer = new Timer();
		timer.schedule(task, 0, 100);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

}
