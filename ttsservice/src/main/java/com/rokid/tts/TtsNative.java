package com.rokid.tts;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Binder;

import com.rokid.speech.OpusPlayer;
import com.rokid.speech.Tts;
import com.rokid.speech.TtsCallback;
import com.rokid.speech.TtsOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TtsNative extends ITts.Stub implements TtsCallback {
	public TtsNative(Context ctx) {
		_tts_sdk = new Tts();
		AssetManager am = ctx.getAssets();
		InputStream is = null;
		try {
			is = am.open("speech_config.json");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		_tts_sdk.prepare(is);
		try {
			is.close();
		} catch (IOException e) {
		}
		TtsOptions opt = new TtsOptions();
		opt.set_codec("opu2");
		_tts_sdk.config(opt);
		_player = new OpusPlayer();
		_requests = new LinkedList<RequestInfo>();
	}

	@Override
	public int speak(String content, ITtsCallback cb) {
		RequestInfo req = new RequestInfo();
		synchronized (_requests) {
			req.id = _tts_sdk.speak(content, this);
			if (req.id <= 0)
				return req.id;
			req.pid = Binder.getCallingPid();
			req.callback = cb;
			_requests.add(req);
		}
		return req.id;
	}

	@Override
	public void cancel(int id) {
		_tts_sdk.cancel(id);
	}

	@Override
	public void cancelByOwner() {
		RequestInfo req;
		int pid = Binder.getCallingPid();
		synchronized (_requests) {
			Iterator<RequestInfo> it = _requests.iterator();
			while (it.hasNext()) {
				req = it.next();
				if (req.pid == pid)
					_tts_sdk.cancel(req.id);
			}
		}
	}

	@Override
	public void pause() {
		_player.pause();
	}

	@Override
	public void resume() {
		_player.resume();
	}

	@Override
	public void onStart(int id) {
		try {
			RequestInfo req = findReqInfoById(id);
			if (req != null && req.callback != null) {
				req.callback.onStart(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onText(int id, String text) {
		// ignore it
	}

	@Override
	public void onVoice(int id, byte[] data) {
		_player.play(data);
	}

	@Override
	public void onCancel(int id) {
		try {
			RequestInfo req = findReqInfoById(id);
			if (req != null) {
				if (req.callback != null)
					req.callback.onCancel(id);
				removeReqInfo(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onComplete(int id) {
		try {
			RequestInfo req = findReqInfoById(id);
			if (req != null) {
				if (req.callback != null)
					req.callback.onComplete(id);
				removeReqInfo(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onError(int id, int err) {
		try {
			RequestInfo req = findReqInfoById(id);
			if (req != null) {
				if (req.callback != null)
					req.callback.onError(id, err);
				removeReqInfo(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private RequestInfo findReqInfoById(int id) {
		RequestInfo req;
		synchronized (_requests) {
			Iterator<RequestInfo> it = _requests.iterator();
			while (it.hasNext()) {
				req = it.next();
				if (req.id == id)
					return req;
			}
		}
		return null;
	}

	private void removeReqInfo(int id) {
		RequestInfo req;
		synchronized (_requests) {
			Iterator<RequestInfo> it = _requests.iterator();
			while (it.hasNext()) {
				req = it.next();
				if (req.id == id) {
					it.remove();
					break;
				}
			}
		}
	}

	private static class RequestInfo {
		public int id;
		public int pid;
		public ITtsCallback callback;
	}

	private Tts _tts_sdk;
	private OpusPlayer _player;
	private List<RequestInfo> _requests;
}

