package com.rokid.phone;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.rokid.media.IRKMediaPlayer;
import com.rokid.tts.ITts;
import com.rokid.voicerec.BearKidNative;
import com.rokid.voicerec.MessageTypes;
import com.rokid.voicerec.NlpAction;


public class MainActivity extends Activity implements OnTouchListener, Handler.Callback, ActivityCompat.OnRequestPermissionsResultCallback {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Context ctx = getApplicationContext();
		bearKidNative = BearKidNative.instance(ctx);
		bearKidNative.setMsgHandler(new Handler(this));
		
		View v = findViewById(R.id.RecognizeTrigger);
		v.setOnTouchListener(this);
		asrView = (TextView)findViewById(R.id.AsrDisplay);
		nlpView = (TextView)findViewById(R.id.NlpDisplay);
		actionView = (TextView)findViewById(R.id.ActionDisplay);
		errorView = (TextView)findViewById(R.id.SpeechError);
		initErrorMessages(ctx);
		
		grantPermission(ctx);
	}

	@Override
	public boolean onTouch(View view, MotionEvent ev) {
		switch (view.getId()) {
		case R.id.RecognizeTrigger:
			triggerRecognize(view, ev);
			break;
		}
		return false;
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
			case MessageTypes.UPDATE_ASR:
				displayAsr((String)msg.obj);
				break;
			case MessageTypes.UPDATE_NLP_ACTION: {
				NlpAction data = (NlpAction)msg.obj;
				displayNlp(data.nlp);
				displayAction(data.action);
				break;
			}
			case MessageTypes.UPDATE_ERROR:
				displayError(msg.arg1);
				break;
			default:
				return false;
		}
		return true;
	}
	
	@Override
	public void onRequestPermissionsResult(int reqcode, String[] permissions, int[] grantResults) {
		if (reqcode == 0) {
			if (grantResults[0] == PackageManager.PERMISSION_DENIED)
				finish();
			else {
				initServices(getApplicationContext());
			}
		}
	}

	private void triggerRecognize(View view, MotionEvent ev) {
		TextView tv = (TextView)view;
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			Log.d(TAG, "action down");
			pauseMedia();
			bearKidNative.control(BearKidNative.ACTION_START_REC);
			tv.setText(R.string.StopRecognize);
		} else if (ev.getAction() == MotionEvent.ACTION_UP) {
			Log.d(TAG, "action up");
			bearKidNative.control(BearKidNative.ACTION_STOP_REC);
			tv.setText(R.string.StartRecognize);
			resumeMedia();
		}
	}

	private void displayAsr(String asr) {
		if (asr == null)
			return;
		asrView.setText(asr);
	}

	private void displayNlp(String nlp) {
		if (nlp == null)
			return;
		nlpView.setText(nlp);
	}

	private void displayAction(String action) {
		if (action == null)
			return;
		actionView.setText(action);
	}

	private void displayError(int err) {
		if (err == 0) {
			errorView.setText("");
			return;
		}
		String m = errorMessages.get(err);
		if (m == null)
			m = getApplicationContext().getString(R.string.err_unknown);
		String dm = getApplicationContext().getString(R.string.err) + err + "\n" + m;
		errorView.setText(dm);
	}

	private void initErrorMessages(Context ctx) {
		errorMessages = new SparseArray<String>();
		errorMessages.put(2, ctx.getString(R.string.err_unauth));
		errorMessages.put(3, ctx.getString(R.string.err_conn_exceed));
		errorMessages.put(4, ctx.getString(R.string.err_res_exhausted));
		errorMessages.put(5, ctx.getString(R.string.err_svr_busy));
		errorMessages.put(6, ctx.getString(R.string.err_svr_internal));
		errorMessages.put(7, ctx.getString(R.string.err_vad_timeout));
		errorMessages.put(101, ctx.getString(R.string.err_svc_unavail));
		errorMessages.put(102, ctx.getString(R.string.err_sdk_closed));
		errorMessages.put(103, ctx.getString(R.string.err_req_timeout));
	}
	
	private void grantPermission(Context ctx) {
		if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.RECORD_AUDIO)
				== PackageManager.PERMISSION_DENIED) {
			String[] ps = new String[1];
			ps[0] = Manifest.permission.RECORD_AUDIO;
			ActivityCompat.requestPermissions(this, ps, 0);
		} else {
			initServices(ctx);
		}
	}

	private void initServices(Context ctx) {
		bearKidNative.init();

		Intent intent = new Intent();
		ComponentName comp = new ComponentName(EXECUTOR_PACKAGE, EXECUTOR_CLASS_NAME);
		intent.setComponent(comp);
		ctx.startService(intent);

		bindServices(ctx);
	}

	private void bindServices(Context ctx) {
		ComponentName comp;
		Intent intent = new Intent();

		if (ttsProxy == null && !bindingTts) {
			comp = new ComponentName(TTS_PACKAGE, TTS_CLASS_NAME);
			intent.setComponent(comp);
			if (ttsConnection != null)
				ctx.unbindService(ttsConnection);
			ttsConnection = new MyServiceConnection();
			if (!ctx.bindService(intent, ttsConnection, Context.BIND_AUTO_CREATE))
				ctx.unbindService(ttsConnection);
			else
				bindingTts = true;
		}

		if (mediaProxy == null && !bindingMedia) {
			comp = new ComponentName(MEDIA_PACKAGE, MEDIA_CLASS_NAME);
			intent.setComponent(comp);
			if (mediaConnection != null)
				ctx.unbindService(mediaConnection);
			mediaConnection = new MyServiceConnection();
			if (!ctx.bindService(intent, mediaConnection, Context.BIND_AUTO_CREATE))
				ctx.unbindService(mediaConnection);
			else
				bindingMedia = true;
		}
	}

	private void pauseMedia() {
		boolean rebind = false;

		try {
			if (ttsProxy != null)
				ttsProxy.pause();
		} catch (Exception e) {
			e.printStackTrace();
			ttsProxy = null;
			rebind = true;
		}

		try {
			if (mediaProxy != null)
				mediaProxy.pause();
		} catch (Exception e) {
			e.printStackTrace();
			mediaProxy = null;
			rebind = true;
		}

		if (rebind)
			bindServices(getApplicationContext());
	}

	private void resumeMedia() {
		boolean rebind = false;

		try {
			if (ttsProxy != null)
				ttsProxy.resume();
		} catch (Exception e) {
			e.printStackTrace();
			ttsProxy = null;
			rebind = true;
		}

		try {
			if (mediaProxy != null)
				mediaProxy.start();
		} catch (Exception e) {
			e.printStackTrace();
			mediaProxy = null;
			rebind = true;
		}

		if (rebind)
			bindServices(getApplicationContext());
	}

	private class MyServiceConnection implements ServiceConnection {
		public void onServiceConnected(ComponentName name, IBinder binder) {
			String cname = name.getClassName();
			if (cname.equals(TTS_CLASS_NAME)) {
				ttsProxy = ITts.Stub.asInterface(binder);
				bindingTts = false;
			} else if(cname.equals(MEDIA_CLASS_NAME)) {
				mediaProxy = IRKMediaPlayer.Stub.asInterface(binder);
				bindingMedia = false;
			}
		}

		public void onServiceDisconnected(ComponentName name) {
		}
	}

	private BearKidNative bearKidNative;
	private TextView asrView;
	private TextView nlpView;
	private TextView actionView;
	private TextView errorView;
	private SparseArray<String> errorMessages;
	private ITts ttsProxy;
	private IRKMediaPlayer mediaProxy;
	private MyServiceConnection ttsConnection;
	private MyServiceConnection mediaConnection;
	private boolean bindingTts;
	private boolean bindingMedia;

	private static final String TAG = "SpeechDemo.MainActivity";
	private static final String EXECUTOR_PACKAGE = "com.rokid.speechexec";
	private static final String EXECUTOR_CLASS_NAME = "com.rokid.speechexec.SpeechExecutor";
	private static final String TTS_PACKAGE = "com.rokid.tts";
	private static final String TTS_CLASS_NAME = "com.rokid.tts.TtsService";
	private static final String MEDIA_PACKAGE = "com.rokid.mediaservice";
	private static final String MEDIA_CLASS_NAME = "com.rokid.mediaservice.RKMediaService";
}
