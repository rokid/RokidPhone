package com.rokid.tts;

import com.rokid.tts.ITtsCallback;

interface ITts {
	int speak(String content, ITtsCallback cb);

	void cancel(int id);

	void cancelByOwner();

	void pause();

	void resume();
}
