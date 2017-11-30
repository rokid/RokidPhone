package com.rokid.tts;

interface ITtsCallback {
	void onStart(int id);

	void onComplete(int id);

	void onCancel(int id);

	void onError(int id, int err);
}
