package com.rokid.bean.response.responseinfo.action;

import android.text.TextUtils;

import com.rokid.bean.base.BaseBean;
import com.rokid.logger.Logger;

/**
 * Created by fanfeng on 2017/6/24.
 */

public abstract class CommonActionBean extends BaseActionBean {

    public static final String ACTION_PLAY = "PLAY";
    public static final String ACTION_PAUSE = "PAUSE";
    public static final String ACTION_RESUME = "RESUME";
    public static final String ACTION_STOP = "STOP";
    public static final String ACTION_FORWARD = "FORWARD";
    public static final String ACTION_BACKWARD = "BACKWARD";
    public static final String ACTION_STATE = "GETSTATUS";

    public String action;
    /**
     * if need close EventRequest
     */
    private boolean disableEvent;

    /**
     * recorder error log to sendReporter
     */
    private String errorLog;


    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
        Logger.d("errorLog: " + errorLog);
    }

    public String getErrorLog() {
        return errorLog;
    }

    public boolean isDisableEvent() {
        return disableEvent;
    }

    public void setDisableEvent(boolean disableEvent) {
        this.disableEvent = disableEvent;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isValid(){
        if (TextUtils.isEmpty(action)){
            setErrorLog("The field of action in " + getType() + " is null , you must set an action in " + getType() + " !");
            return false;
        }
        //action 为其他操作不需要判断url/tts
        if (ACTION_PLAY.equals(action) && !canPlay()){
            return false;
        }
        return true;
    }

    public abstract boolean canPlay();

    public abstract String getType();
}
