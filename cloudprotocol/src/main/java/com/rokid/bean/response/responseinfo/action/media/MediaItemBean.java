package com.rokid.bean.response.responseinfo.action.media;

/**
 * Defines the media item information which is required by the media player.
 * Here comes the definition.
 * <p>
 * Author: xupan.shi
 * Version: V0.1 2017/3/7
 */
public class MediaItemBean {

    private String itemId;
    private int offsetInMilliseconds;
    private String token;
    private String type;
    private String url;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getOffsetInMilliseconds() {
        return offsetInMilliseconds;
    }

    public void setOffsetInMilliseconds(int offsetInMilliseconds) {
        this.offsetInMilliseconds = offsetInMilliseconds;
    }

}
