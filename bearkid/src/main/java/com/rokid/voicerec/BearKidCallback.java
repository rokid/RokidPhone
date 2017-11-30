package com.rokid.voicerec;

// event, exception具体定义见BearKidAdapter类

public interface BearKidCallback {
	// 'event': EVENT_LOCATION   'arg1' 无意义  'arg2' 语音方向信息
	//          EVENT_VOICE_INFO 'arg1' 无意义  'arg2' 音强信息
	//          EVENT_ACTIVATION 'arg1' 接受激活或拒绝激活 'arg2' 无意义
	//          EVENT_DEACTIVATE 'arg1' 无意义  'arg2' 无意义
	void onVoiceEvent(int event, int arg1, double arg2);

	// 中间asr识别结果
	void onIntermediateResult(String asr);

	// 'asr'不为空  整句话的asr识别结果。此时'nlp', 'action'一定为空
	// 'nlp', 'action'不为空  语义理解结果。此时'asr'一定为空
	void onRecognizeResult(String asr, String nlp, String action);

	// 'exception' 异常类型
	void onException(int exception);
}
