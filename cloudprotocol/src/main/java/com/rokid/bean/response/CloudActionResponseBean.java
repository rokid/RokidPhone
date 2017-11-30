package com.rokid.bean.response;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.rokid.bean.base.BaseBean;
import com.rokid.bean.request.session.SessionBean;
import com.rokid.bean.response.responseinfo.ResponseBean;
import com.rokid.bean.response.responseinfo.action.ActionBean;
import com.rokid.bean.response.responseinfo.action.confirm.ConfirmBean;
import com.rokid.bean.response.responseinfo.action.display.DisplayBean;
import com.rokid.bean.response.responseinfo.action.media.MediaBean;
import com.rokid.bean.response.responseinfo.action.pickup.PickupBean;
import com.rokid.bean.response.responseinfo.action.voice.VoiceBean;
import com.rokid.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * The response should be replied by CloudApps for client side execution.
 * <p>
 * Modified: fan.feng
 * Version: 2017/08/24
 */
public class CloudActionResponseBean extends BaseBean{

    private static final String PROTOCOL_VERSION = "2.0.0";

    private String appId;
    private String version;
    private SessionBean session;
    private ResponseBean response;

    private MediaBean mediaBean;
    private VoiceBean voiceBean;
    private DisplayBean displayBean;
    private ConfirmBean confirmBean;
    private PickupBean pickupBean;

    private static final String PROTOCOL_ERROR = "The field of version is null or not the same with " + PROTOCOL_VERSION + " !";
    private static final String RESPONSE_ERROR = "The field of response is null !";
    private static final String APPID_ERROR = "The field of appId is null !";
    private static final String ACTION_ERROR ="The field of action is null !";
    private static final String FORM_ERROR_NULL ="The field of form is null !";
    private static final String FORM_ERROR_ILLEGAL="The field of form you set is illegal !";
    private static final String NO_EXECUTABLE_ACTION = "No executable action , you must set up an executable action !";

    private String errorLog;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public SessionBean getSession() {
        return session;
    }

    public void setSession(SessionBean session) {
        this.session = session;
    }

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public MediaBean getMediaBean() {
        return mediaBean;
    }

    public void setMediaBean(MediaBean mediaBean) {
        this.mediaBean = mediaBean;
    }

    public VoiceBean getVoiceBean() {
        return voiceBean;
    }

    public void setVoiceBean(VoiceBean voiceBean) {
        this.voiceBean = voiceBean;
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

    public PickupBean getPickupBean() {
        return pickupBean;
    }

    public void setPickupBean(PickupBean pickupBean) {
        this.pickupBean = pickupBean;
    }

    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
        Logger.d(errorLog);
    }

    public boolean isValid(){

        // check version
        String version = getVersion();
        Logger.d(" version : " + version);
        if (TextUtils.isEmpty(version) || !version.equals(PROTOCOL_VERSION)) {
            setErrorLog(PROTOCOL_ERROR);
            return false;
        }

        // check response
        if (getResponse() == null) {
            setErrorLog(RESPONSE_ERROR);
            return false;
        }

        if (TextUtils.isEmpty(getAppId())){
            setErrorLog(APPID_ERROR);
            return false;
        }

        if (getResponse().getAction() == null){
            setErrorLog(ACTION_ERROR);
            return false;
        }

        // check response form
        String form = getResponse().getAction().getForm();

        if (TextUtils.isEmpty(form)) {
            setErrorLog(FORM_ERROR_NULL);
            return false;
        }

        String formLow = form.toLowerCase();

        if (!formLow.equals(ActionBean.FORM_SCENE) && !formLow.equals(ActionBean.FORM_CUT) && !formLow.equals(ActionBean.FORM_SERVICE)) {
            setErrorLog(FORM_ERROR_ILLEGAL);
            return false;
        }

        // check response action
        ActionBean responseAction = getResponse().getAction();
        if (responseAction == null) {
            setErrorLog(NO_EXECUTABLE_ACTION);
            return false;
        }

        // check response action type
        String responseActionType = responseAction.getType();

        if (ActionBean.TYPE_EXIT.equals(responseActionType)){
            Logger.d("actionType is EXIT ");
            return true;
        }

        if (!isDataValid(getResponse())){
            setErrorLog(NO_EXECUTABLE_ACTION);
            return false;
        }

        return true;
    }

    /**
     * Private method to check voice, media and display
     *
     */
    private boolean isDataValid(ResponseBean responseBean) {
        ActionBean responseAction = responseBean.getAction();

        List<Object> directives = responseAction.getDirectives();
        if (directives == null || directives.isEmpty()){
            Logger.d("directives is null !");
            return false;
        }

        for (Object directive : directives) {
            String json = new Gson().toJson(directive);
            Logger.d("json str : " + json);
            JSONObject jsonObject;
            String type = null;
            try {
                jsonObject =new JSONObject(json);
                type = jsonObject.getString("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (TextUtils.isEmpty(type)){
                Logger.d("type is null ! ");
                continue;
            }

            switch (type){
                case ActionBean.MEDIA:
                    setMediaBean(new Gson().fromJson(json,MediaBean.class));
                    break;
                case ActionBean.VOICE:
                    setVoiceBean(new Gson().fromJson(json,VoiceBean.class));
                    break;
                case ActionBean.DISPLAY:
                    setDisplayBean(new Gson().fromJson(json,DisplayBean.class));
                    break;
                case ActionBean.CONFIRM:
                    setConfirmBean(new Gson().fromJson(json,ConfirmBean.class));
                    break;
                case ActionBean.PICKUP:
                    setPickupBean(new Gson().fromJson(json,PickupBean.class));
                    break;
            }

        }

        if (mediaBean != null){
            if (mediaBean.isValid()){
                Logger.d("media valid ");
                return true;
            }else {
                setErrorLog(mediaBean.getErrorLog());
                return false;
            }
        }

        if (voiceBean != null){
            if (voiceBean.isValid()){
                Logger.d("voice valid ");
                return true;
            }else {
                setErrorLog(voiceBean.getErrorLog());
                return false;
            }
        }

        if (confirmBean != null){
            Logger.d("confirm valid ");
            return true;
        }

        if (pickupBean != null){
            Logger.d("pickup valid ");
            return true;
        }

        Logger.d("data invalid !");
        return false;
    }

}
