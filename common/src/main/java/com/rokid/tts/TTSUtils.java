package com.rokid.tts;

/**
 * Created by fanfeng on 2017/9/11.
 */

public class TTSUtils {

    private static TTSUtils ttsUtils;

    private BaseTTSHelper ttsHelper;

    public static TTSUtils getInstance(){
        if (ttsUtils == null){
            ttsUtils = new TTSUtils();
        }
        return ttsUtils;
    }

    public BaseTTSHelper getTtsHelper() {
        return this.ttsHelper;
    }

    public void setTtsHelper(BaseTTSHelper ttsHelper) {
        this.ttsHelper = ttsHelper;
    }
}
