package com.rokid.bean;

import com.rokid.bean.base.BaseBean;
import com.rokid.bean.response.responseinfo.action.confirm.ConfirmBean;
import com.rokid.bean.response.responseinfo.action.display.DisplayBean;
import com.rokid.bean.response.responseinfo.action.media.MediaBean;
import com.rokid.bean.response.responseinfo.action.pickup.PickupBean;
import com.rokid.bean.response.responseinfo.action.voice.VoiceBean;

/**
 * Created by fengfan on 3/16/17.
 */

public class ActionNode extends BaseBean {
    private String asr;
    private String respId;
    private String resType;
    private String appId;
    private String form;
    private String actionType;
    private boolean shouldEndSession;
    private VoiceBean voice;
    private MediaBean media;
    private DisplayBean displayBean;
    private ConfirmBean confirmBean;
    private PickupBean pickup;

    public String getAsr() {
        return asr;
    }

    public void setAsr(String asr) {
        this.asr = asr;
    }

    public String getRespId() {
        return respId;
    }

    public void setRespId(String respId) {
        this.respId = respId;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String respType) {
        this.resType = respType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public boolean isShouldEndSession() {
        return shouldEndSession;
    }

    public void setShouldEndSession(boolean shouldEndSession) {
        this.shouldEndSession = shouldEndSession;
    }

    public VoiceBean getVoice() {
        return voice;
    }

    public void setVoice(VoiceBean voice) {
        this.voice = voice;
    }

    public MediaBean getMedia() {
        return media;
    }

    public void setMedia(MediaBean media) {
        this.media = media;
    }

    public DisplayBean getDisplayBean() {
        return displayBean;
    }

    public void setDisplayBean(DisplayBean displayBean) {
        this.displayBean = displayBean;
    }

    public ConfirmBean getConfirmBean() {
        return confirmBean;
    }

    public void setConfirmBean(ConfirmBean confirmBean) {
        this.confirmBean = confirmBean;
    }

    public PickupBean getPickup() {
        return pickup;
    }

    public void setPickup(PickupBean pickup) {
        this.pickup = pickup;
    }
}
