package com.rokid.bean.request.requestinfo;

/**
 * Created by fanfeng on 2017/5/17.
 */

public class EventRequestBean extends ContentBean {
    /**
     * EventRequest is event based.
     * Event handling is an optional request for CloudApps.
     * CloudApps can make related response on demand.
     */
    private String event;
    private String extra;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

}
