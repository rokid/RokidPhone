package com.rokid.bean.response.responseinfo.card;

/**
 * Card is designed to illustrate extend information of CloudApps on RokidMobileSDK.
 * <p>
 * Author: xupan.shi
 * Version: V0.1 2017/3/7
 */
public class CardBean<T> {

    private String version;
    private String type;
    private T template;

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

    public T getTemplate() {
        return template;
    }

    public void setTemplate(T template) {
        this.template = template;
    }

}