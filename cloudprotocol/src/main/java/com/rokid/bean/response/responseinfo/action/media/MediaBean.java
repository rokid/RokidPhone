package com.rokid.bean.response.responseinfo.action.media;

import android.text.TextUtils;

import com.rokid.bean.response.responseinfo.action.CommonActionBean;

/**
 * Media is used to play streaming media.
 * Both audio and video are supported. Action, behaviour and media item are defined in media section.
 * <p>
 * Author: xupan.shi
 * Version: V0.1 2017/3/7
 */
public class MediaBean extends CommonActionBean {

    private MediaItemBean item;

    public MediaItemBean getItem() {
        return item;
    }

    public void setItem(MediaItemBean item) {
        this.item = item;
    }

    private static final String ITEM_NULL = "The field of item in media is null, you must set it !";

    private static final String ITEM_URL_NULL = "The field of url in media is null , you must set it !";

    @Override
    public boolean canPlay() {

        if (item == null){
            setErrorLog(ITEM_NULL);
            return false;
        }

        if (TextUtils.isEmpty(item.getUrl())){
            setErrorLog(ITEM_URL_NULL);
            return false;
        }

        return true;
    }

    @Override
    public String getType() {
        return "media";
    }

}
