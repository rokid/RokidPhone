package com.rokid.bean.response.responseinfo.action;

import android.text.TextUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * There are two kinds of action.
 * One is interaction and another is media .
 * interaction is for human-machine interaction including TTS and display.
 * media is for media streaming. Besides,action includes following properties.
 * <p>
 * Author: xupan.shi
 * Version: V0.1 2017/3/7
 */
public class ActionBean {

    /**
     * When type is NORMAL , voice , display and media will be executed concurrently
     */
    public static final String TYPE_NORMAL = "NORMAL";
    /**
     * When type is EXIT , the action will be shut down immediately.
     * In this case, voice , display and media will be ignored.
     */
    public static final String TYPE_EXIT = "EXIT";

    public static final String FORM_SCENE = "scene";
    public static final String FORM_CUT = "cut";
    public static final String FORM_SERVICE = "service";

    /**
     * 表明 action 协议版本，当前版本为: 2.0.0.
     */
    private String version;
    /**
     * 前action的类型：NORMAL 或 EXIT。 当 type 是 NORMAL 时，voice 和 media 会同时执行；当 type      * 是 EXIT 时，action会立即退出，并且在这种情况下，voice 和 media 将会被会被忽略
     */
    private String type;
    /**
     * 表明当此次返回的action执行完后 CloudAppClient 是否要退出，同时，当 shouldEndSession 为 true     * 时，CloudAppClient 将会忽略 EventRequests，即在action执行过程中不会产生 EventRequest。
     */
    private boolean shouldEndSession;

    /**
     * 当前action的展现形式：scene、cut、service。scene的action会在被打断后压栈，cut的action会在被打    * 断后直接结束，service会在后台执行，但没有任何界面。该字段在技能创建时被确定，无法由cloud app更改。
     */
    private String form;


    /**
     * directive type ： media/voice/display/pickup/confirm
     */
    public static final String MEDIA = "media";
    public static final String VOICE = "voice";
    public static final String DISPLAY = "display";
    public static final String PICKUP = "pickup";
    public static final String CONFIRM = "confirm";

    private List<Object> directives;

    public String getForm() {

        if (!TextUtils.isEmpty(form))
            return form.toLowerCase();

        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isShouldEndSession() {
        return shouldEndSession;
    }

    public void setShouldEndSession(boolean shouldEndSession) {
        this.shouldEndSession = shouldEndSession;
    }

    public boolean isTypeValid() {
        return !TextUtils.isEmpty(type) && (TYPE_NORMAL.equals(type) || TYPE_EXIT.equals(type));
    }

    public boolean isShotValid() {
        return !TextUtils.isEmpty(form) && (FORM_SCENE.equals(form) || FORM_CUT.equals(form));
    }

    public List<Object> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Object> directives) {
        this.directives = directives;
    }
}
