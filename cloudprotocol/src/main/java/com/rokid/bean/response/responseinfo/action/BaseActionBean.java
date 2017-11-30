package com.rokid.bean.response.responseinfo.action;

import com.rokid.bean.base.BaseBean;

/**
 * Created by fanfeng on 2017/9/13.
 */

public abstract class BaseActionBean extends BaseBean{
    public String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public abstract boolean isValid();
}
