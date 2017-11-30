package com.rokid.bean.request.requestinfo;

/**
 * Created by fanfeng on 2017/5/17.
 */
public class IntentRequestBean extends ContentBean {

    /**
     * IntentRequest is based on the result of NLP result.
     * Domain, Intent and Slots are indicated.
     * CloudApps with the same appId should handle the intent with slots to make a response.
     */
    private String intent;
    private String slots;

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getSlots() {
        return slots;
    }

    public void setSlots(String slots) {
        this.slots = slots;
    }


}
