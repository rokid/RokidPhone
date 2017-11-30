package com.rokid.bean.response.responseinfo.action.display;

import android.text.TextUtils;

import com.rokid.bean.response.responseinfo.action.BaseActionBean;

/**
 * Display is the Graphic User Interface for CloudApps.
 * <p>
 * Author: xupan.shi
 * Version: V0.1 2017/3/9
 */
public class DisplayBean<T> extends BaseActionBean{

    /**
     * SHOW means the previous display should be dismissed the current display should show up
     * with a valid template object.
     */
    public static final String ACTION_SHOW = "SHOW";
    /**
     * DISMISS means the current display should be dismissed.
     */
    public static final String ACTION_DISMISS = "DISMISS";

    private String action;
    private boolean needEventCallback;
    private int duration;
    private T template;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isNeedEventCallback() {
        return needEventCallback;
    }

    public void setNeedEventCallback(boolean needEventCallback) {
        this.needEventCallback = needEventCallback;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public T getTemplate() {
        return template;
    }

    public void setTemplate(T template) {
        this.template = template;
    }

    @Override
    public boolean isValid(){
        return !TextUtils.isEmpty(action) && (ACTION_SHOW.equals(action) || ACTION_DISMISS.equals(action));
    }


}
