package com.rokid.speechexec;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.rokid.voicerec.ActionToComponent;
import com.rokid.voicerec.BearKidAdapter;
import com.rokid.voicerec.BearKidCallback;

public class SpeechExecutor extends Service implements BearKidCallback {
	public void onCreate() {
		context = getApplicationContext();
		bearKidAdapter = new BearKidAdapter();
		bearKidAdapter.initialize(context, BEARKID_ACTION, this);
	}

	public void onDestroy() {
		bearKidAdapter.close();
	}

	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onVoiceEvent(int event, int arg1, double arg2) {
	}

	public void onIntermediateResult(String asr) {
	}

	public void onRecognizeResult(String asr, String nlp, String action) {
		if (action != null) {
			Intent intent = ActionToComponent.newIntent(context, CLOUDAPP_ACTION);
			if (intent != null) {
				intent.putExtra("action", action);
				Log.d(TAG, "speech exec start service " + intent.getComponent());
				context.startService(intent);
			}
		}
	}

	public void onException(int extype) {
	}

	/**
	private boolean loadConfig() {
		AssetManager am = context.getAssets();
		InputStream is = null;
		int c;
		byte[] data;

		try {
			is = am.open("exec_config.json");
			c = is.available();
			data = new byte[c];
			is.read(data);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception e) {
			}
		}
		String jsonstr = new String(data);
		try {
			JSONObject jo = new JSONObject(jsonstr);
			configValues = new ConfigValues();
			configValues.bearKidPackage = jo.getString("bearKidPackage");
			configValues.bearKidClassName = jo.getString("bearKidClassName");
			configValues.cloudappPackage = jo.getString("cloudappPackage");
			configValues.cloudappClassName = jo.getString("cloudappClassName");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static class ConfigValues {
		public String bearKidPackage;
		public String bearKidClassName;
		public String cloudappPackage;
		public String cloudappClassName;
	}
	*/

	private BearKidAdapter bearKidAdapter;
	private Context context;
	private static final String TAG = "SpeechExecutor";
	private static final String BEARKID_ACTION = "com.rokid.openvoice.VoiceService";
	private static final String CLOUDAPP_ACTION = "com.rokid.cloudappclient.CloudAppService";
}
