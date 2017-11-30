package com.rokid.voicerec;

import com.rokid.voicerec.BearKidResult;

interface BearKid {
	void control(int action);

	int getState();

	BearKidResult poll();
}
