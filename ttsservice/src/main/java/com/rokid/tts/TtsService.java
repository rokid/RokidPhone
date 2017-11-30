package com.rokid.tts;

import android.app.Service;
import android.os.IBinder;
import android.content.Intent;

public class TtsService extends Service {
	public void onCreate() {
		_ttsNative = new TtsNative(getApplicationContext());
	}

	@Override
	public IBinder onBind(Intent intent) {
		return _ttsNative;
	}

	private TtsNative _ttsNative;
}
