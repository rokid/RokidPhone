package com.rokid.bean.response.responseinfo.action;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rokid.bean.response.responseinfo.action.confirm.ConfirmBean;
import com.rokid.bean.response.responseinfo.action.display.DisplayBean;
import com.rokid.bean.response.responseinfo.action.media.MediaBean;
import com.rokid.bean.response.responseinfo.action.pickup.PickupBean;
import com.rokid.bean.response.responseinfo.action.voice.VoiceBean;

import java.lang.reflect.Type;

/**
 * Created by fanfeng on 2017/9/13.
 */

public class BaseActionBeanAdapter implements JsonDeserializer<BaseActionBean> {
    @Override
    public BaseActionBean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {

        JsonObject jsonObj = json.getAsJsonObject();

        String type = jsonObj.get("type").getAsString();

        switch (type){
            case ActionBean.MEDIA:
                return new Gson().fromJson(json,MediaBean.class);
            case ActionBean.VOICE:
                return new Gson().fromJson(json, VoiceBean.class);
            case ActionBean.DISPLAY:
                return new Gson().fromJson(json,DisplayBean.class);
            case ActionBean.PICKUP:
                return new Gson().fromJson(json, PickupBean.class);
            case ActionBean.CONFIRM:
                return new Gson().fromJson(json, ConfirmBean.class);
            default:
                break;
        }
        return null;
    }
}
