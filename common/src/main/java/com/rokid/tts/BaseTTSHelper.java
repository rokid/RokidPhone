package com.rokid.tts;

/**
 * Created by fanfeng on 2017/9/11.
 */

public abstract class BaseTTSHelper {

    public static final int STOP = -1;
    public boolean isPaused = false;
    public volatile int ttsId = STOP;

    public VoiceStateCallback mVoiceStateCallback;

    public BaseTTSHelper registerVoiceStateCallback(VoiceStateCallback voiceStateCallback){
        mVoiceStateCallback = voiceStateCallback;
        return this;
    }

    public abstract void speakTTS(String ttsContent);

    public abstract void pauseTTS();

    public abstract void stopTTS();

    public interface VoiceStateCallback {

        void onVoiceStarted();

        void onVoiceFinished();

        void onVoicePaused();

        void onVoiceStopped();

        void onVoiceFailed();

    }

}
