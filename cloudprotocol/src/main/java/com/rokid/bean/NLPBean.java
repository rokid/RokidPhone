package com.rokid.bean;

import com.rokid.bean.base.BaseBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by showingcp on 3/13/17.
 */

public class NLPBean extends BaseBean {

    /**
     * asr content
     */
    private String asr;

    /**
     * action content
     */
    private String action;

    /**
     * intent cloudAppId
     */
    private String appId;
    /**
     * intent
     */
    private String intent;
    /**
     * indicate whether the action should execute: YES or NO
     */
    private String confirm;
    /**
     * parameters for the intent key value string pairs
     */
    private Map<String, String> slots = new HashMap<String, String>();
    /**
     * nlp parse start position
     */
    private int posStart = 0;
    /**
     * nlp parse end position
     */
    private int posEnd = 0;
    /**
     * parse confidence 1.0f is the max value
     */
    private float confidence = 0.0f;

    /**
     * the pattern that the sentence matched
     */
    private String pattern;

    /**
     * the version of the hit xml
     */
    private String version;

    /**
     * the original asr
     */
    private String voice;

    /**
     * forward content
     */
    private String forwardContent;

    /**
     * indicates the nlp is a cloud call
     */
    private boolean cloud;

    public String getAsr() {
        return asr;
    }

    public void setAsr(String asr) {
        this.asr = asr;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getIntent() {
        return intent;
    }
    public void setIntent(String intent) {
        this.intent = intent;
    }
    public String getConfirm() {
        return confirm;
    }
    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
    public Map<String, String> getSlots() {
        return slots;
    }
    public void setSlots(Map<String, String> slots) {
        this.slots = slots;
    }

    public int getPosStart() {
        return posStart;
    }
    public void setPosStart(int posStart) {
        this.posStart = posStart;
    }
    public int getPosEnd() {
        return posEnd;
    }
    public void setPosEnd(int posEnd) {
        this.posEnd = posEnd;
    }
    public float getConfidence() {
        return confidence;
    }
    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public String getPattern() {
        return pattern;
    }
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    public void addSlot(String key, String value){
        this.slots.put(key, value);
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getForwardContent() {
        return forwardContent;
    }

    public void setForwardContent(String forwardContent) {
        this.forwardContent = forwardContent;
    }

    public boolean isCloud() {
        return cloud;
    }

    public void setCloud(boolean cloud) {
        this.cloud = cloud;
    }
}
