package com.rokid.bean.response.responseinfo.action.voice;

import com.rokid.bean.base.BaseBean;

/**
 * Voice's Item indicates the voice interaction content detail including TTS and Confirm.
 * <p>
 * Author: xupan.shi
 * Version: V0.1 2017/3/9
 */
public class VoiceItemBean extends BaseBean {

    private String itemId;
    private String tts;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTts() {
        return tts;
    }

    public void setTts(String tts) {
        this.tts = tts;
    }
}
