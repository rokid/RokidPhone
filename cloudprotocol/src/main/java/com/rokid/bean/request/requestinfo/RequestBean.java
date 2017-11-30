package com.rokid.bean.request.requestinfo;

/**
 * In Request section,
 * the real request action will be illustrated including the request type and content.
 * <p>
 * Author: xupan.shi
 * Version: V0.1 2017/3/7
 */
public class RequestBean {

    public static final String TYPE_INTENT = "INTENT";
    public static final String TYPE_EVENT = "EVENT";

    /**
     * ONLY type INTENT and EVENT are available currently
     */
    private String reqType;
    private String reqId;
    /**
     * indicates the last corresponding response id for the request.
     * It is *ONLY in effect when the reqType is EVENT.
     */
    private String currentReqId;
    private ContentBean content;

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getCurrentReqId() {
        return currentReqId;
    }

    public void setCurrentReqId(String currentReqId) {
        this.currentReqId = currentReqId;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

}
