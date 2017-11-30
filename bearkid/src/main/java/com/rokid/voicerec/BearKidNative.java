package com.rokid.voicerec;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.rokid.speech.SpeechCallback;

public class BearKidNative extends BearKid.Stub implements SpeechCallback {
	public static BearKidNative instance(Context ctx) {
		if (bearKidNative == null)
			bearKidNative = new BearKidNative(ctx);
		return bearKidNative;
	}

	private BearKidNative(Context ctx) {
		curState = STATE_UNINIT;
		context = ctx;
		pollCondition = new Object();
	}
	
	public void init() {
		voiceRecognize = new VoiceRecognize();
		voiceRecognize.initialize(context, null, this);
		curState = STATE_IDLE;
	}

	public void setMsgHandler(Handler h) {
		uiMsgHandler = h;
	}

	/** implementations of BearKid */
	public void control(int action) {
		if (action == ACTION_START_REC) {
			if (curState == STATE_IDLE) {
				voiceRecognize.startRecognize();
				curState = STATE_ACTIVE;
			}
		} else if (action == ACTION_STOP_REC) {
			if (curState == STATE_ACTIVE) {
				voiceRecognize.stopRecognize();
				curState = STATE_IDLE;
			}
		}
	}

	public int getState() {
		return curState;
	}

	public BearKidResult poll() {
		BearKidResult result = new BearKidResult();

		synchronized (pollCondition) {
			while (true) {
				if (lastestInterAsr != null) {
					result.type = BearKidResult.TYPE_INTERMEDIATE;
					result.asr = lastestInterAsr;
					lastestInterAsr = null;
					break;
				} else if (lastestFinalAsr != null) {
					result.type = BearKidResult.TYPE_ASR;
					result.asr = lastestFinalAsr;
					lastestFinalAsr = null;
					break;
				} else if (lastestNlp != null) {
					result.type = BearKidResult.TYPE_NLP;
					result.nlp = lastestNlp;
					result.action = lastestAction;
					lastestNlp = null;
					lastestAction = null;
					break;
				} else if (lastestError != 0) {
					result.type = BearKidResult.TYPE_EXCEPTION;
					result.extype = EXCEPTION_SERVICE_ERROR;
					lastestError = 0;
					break;
				} else {
					try {
						pollCondition.wait();
					} catch (Exception e) {
					}
				}
			}
		}
		return result;
	}

	/** implementations of SpeechCallback */
	public void onStart(int id) {
	}

	public void onIntermediateResult(int id, String asr, String extra) {
		// 近场识音extra字段不需处理
		synchronized (pollCondition) {
			lastestInterAsr = asr;
			pollCondition.notify();
		}
		if (uiMsgHandler != null) {
			Message msg = uiMsgHandler.obtainMessage(MessageTypes.UPDATE_ASR, asr);
			msg.sendToTarget();
		}
	}

	public void onAsrComplete(int id, String asr) {
		synchronized (pollCondition) {
			lastestFinalAsr = asr;
			pollCondition.notify();
		}
		if (uiMsgHandler != null) {
			Message msg = uiMsgHandler.obtainMessage(MessageTypes.UPDATE_ASR, asr);
			msg.sendToTarget();
		}
	}

	public void onComplete(int id, String nlp, String action) {
		synchronized (pollCondition) {
			lastestNlp = nlp;
			lastestAction = action;
			pollCondition.notify();
		}
		if (uiMsgHandler != null) {
			NlpAction data = new NlpAction();
			data.nlp = nlp;
			data.action = action;
			Message msg = uiMsgHandler.obtainMessage(MessageTypes.UPDATE_NLP_ACTION, data);
			msg.sendToTarget();
		}
	}

	public void onCancel(int id) {
		// 近场识音没有cancel
	}

	public void onError(int id, int err) {
		synchronized (pollCondition) {
			lastestError = err;
			pollCondition.notify();
		}
		if (uiMsgHandler != null) {
			Message msg = uiMsgHandler.obtainMessage(MessageTypes.UPDATE_ERROR, err, 0);
			msg.sendToTarget();
		}
	}

	private Context context;
	private VoiceRecognize voiceRecognize;
	private Handler uiMsgHandler;
	private int curState;
	private Object pollCondition;
	private String lastestInterAsr;
	private String lastestFinalAsr;
	private String lastestNlp;
	private String lastestAction;
	private int lastestError;
	private static BearKidNative bearKidNative = null;

	public static final int ACTION_START_REC = 5;
	public static final int ACTION_STOP_REC = 6;
	private static final int STATE_UNINIT = 0;
	private static final int STATE_IDLE = 1;
	private static final int STATE_ACTIVE = 2;
	private static final int EXCEPTION_SERVICE_ERROR = 4;
}
