package com.rokid.bean.response.responseinfo;

import android.text.TextUtils;

import com.rokid.bean.response.responseinfo.action.ActionBean;
import com.rokid.bean.response.responseinfo.card.CardBean;


/**
 * There are two sections in response .
 * card is the response for mobile card display. And action is for CloudAppClient.
 * <p>
 * Author: xupan.shi
 * Version: V0.1 2017/3/7
 */

public class ResponseBean {

    public static final String TYPE_INTENT = "INTENT";
    public static final String TYPE_EVENT = "EVENT";

    private String respId;
    /**
     * ONLY type INTENT and EVENT are available currently
     */
    private String resType;

    private CardBean card;
    private ActionBean action;

    public String getRespId() {
        return respId;
    }

    public void setRespId(String respId) {
        this.respId = respId;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }


    public CardBean getCard() {
        return card;
    }

    public void setCard(CardBean card) {
        this.card = card;
    }

    public ActionBean getAction() {
        return action;
    }

    public void setAction(ActionBean action) {
        this.action = action;
    }

    public boolean isResTypeValid() {
        return !TextUtils.isEmpty(resType) && (TYPE_INTENT.equals(resType) || TYPE_EVENT.equals(resType));
    }

}
