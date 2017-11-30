package com.rokid.bean.response.responseinfo.action.voice;

import android.text.TextUtils;

import com.rokid.bean.response.responseinfo.action.CommonActionBean;

/**
 * Defines the voice interaction of CloudApps, including TTS and Confirmation.
 * <p>
 * Author: fanfeng
 * Version: V0.1 2017/08/24
 */
public class VoiceBean extends CommonActionBean {

    private VoiceItemBean item;

    private static final String ITEM_NULL = "The field of item in voice is null, you must set it !";

    private static final String ITEM_TTS_NULL = "The field of tts in item is null , you must set it !";

    public VoiceItemBean getItem() {
        return item;
    }

    public void setItem(VoiceItemBean item) {
        this.item = item;
    }

    @Override
    public boolean canPlay() {

        if (item == null) {
            setErrorLog(ITEM_NULL);
            return false;
        }

        if (TextUtils.isEmpty(item.getTts())) {
            setErrorLog(ITEM_TTS_NULL);
            return false;
        }

        return true;
    }

    @Override
    public String getType() {
        return "voice";
    }
}
