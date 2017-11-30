package com.rokid.phone;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.rokid.voicerec.BearKidNative;


public class BearKidService extends Service {
	public void onCreate() {
		bearKidNative = BearKidNative.instance(getApplicationContext());
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_NOT_STICKY;
	}

	public void onDestroy() {
	}

	public IBinder onBind(Intent intent) {
		return  bearKidNative;
	}

	private BearKidNative bearKidNative;
}
