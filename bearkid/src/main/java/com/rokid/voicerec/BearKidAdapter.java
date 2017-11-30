package com.rokid.voicerec;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class BearKidAdapter implements Runnable, ServiceConnection {
	public boolean initialize(Context ctx, String action, BearKidCallback cb) {
		working = true;
		context = ctx;
		callback = cb;
		bearKidAction = action;
		// start thread for poll BearKidResult
		new Thread(this).start();
		return true;
	}

	public void close() {
		synchronized (this) {
			working = false;
			context.unbindService(this);
		}
	}

	@Override
	public void run() {
		BearKid bk;
		BearKidResult result;

		while (working) {
			bk = getProxy();
			if (bk == null)
				continue;
			try {
				result = bk.poll();
			} catch (Exception e) {
				clearProxy();
				e.printStackTrace();
				continue;
			}

			handleResult(result);
		}
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder binder) {
		synchronized (this) {
			bearKid = BearKid.Stub.asInterface(binder);
			this.notify();
		}
	}

	public void onServiceDisconnected(ComponentName name) {
		clearProxy();
	}

	private BearKid getProxy() {
		synchronized (this) {
			if (bearKid != null)
				return bearKid;
			Intent intent = ActionToComponent.newIntent(context, bearKidAction);
			if (intent == null) {
				working = false;
				return null;
			}
			if (context.bindService(intent, this, Context.BIND_AUTO_CREATE)) {
				try {
					this.wait(BIND_SERVICE_TIMEOUT);
				} catch (Exception e) {
				}
			} else {
				working = false;
				context.unbindService(this);
			}
			return bearKid;
		}
	}

	private void clearProxy() {
		synchronized (this) {
			bearKid = null;
		}
	}

	private void handleResult(BearKidResult result) {
		if (callback == null)
			return;
		switch (result.type) {
			case BearKidResult.TYPE_LOCATION:
				callback.onVoiceEvent(EVENT_LOCATION, 0, result.location);
				break;
			case BearKidResult.TYPE_VOICE_INFO:
				callback.onVoiceEvent(EVENT_VOICE_INFO, 0, result.energy);
				break;
			case BearKidResult.TYPE_ACTIVATION:
				callback.onVoiceEvent(EVENT_ACTIVATION, result.activation, 0.0);
				break;
			case BearKidResult.TYPE_DEACTIVE:
				callback.onVoiceEvent(EVENT_DEACTIVE, 0, 0.0);
				break;
			case BearKidResult.TYPE_INTERMEDIATE:
				callback.onIntermediateResult(result.asr);
				break;
			case BearKidResult.TYPE_ASR:
				callback.onRecognizeResult(result.asr, null, null);
				break;
			case BearKidResult.TYPE_NLP:
				callback.onRecognizeResult(null, result.nlp, result.action);
				break;
			case BearKidResult.TYPE_EXCEPTION:
				callback.onException(result.extype);
				break;
		}
	}

	private Context context;
	private BearKidCallback callback;
	private BearKid bearKid;
	private String bearKidAction;
	private boolean working;

	private static final int BIND_SERVICE_TIMEOUT = 20000;

	// 声音方向信息
	public static final int EVENT_LOCATION = 1;
	// 声音信息：当前音强
	public static final int EVENT_VOICE_INFO = 2;
	// 语音激活确认: accept, reject
	public static final int EVENT_ACTIVATION = 3;
	// 退出激活状态
	public static final int EVENT_DEACTIVE = 4;

	public static final int ACTIVATION_ACCEPT = 0;
	public static final int ACTIVATION_REJECT = 1;

	// 未连网状态下语音激活
	public static final int EXCEPTION_ACTIVATE_NO_INET = 1;
	// 激活状态下，语音识别过程取消
	public static final int EXCEPTION_CANCEL = 2;
	// 语音识别超时
	public static final int EXCEPTION_TIMEOUT = 3;
	// 语音识别服务端错误
	public static final int EXCEPTION_SERVICE_ERROR = 4;
}
