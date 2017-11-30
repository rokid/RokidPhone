package com.rokid.bean.base;

import com.google.gson.Gson;

/**
 * Author: xupan.shi
 * Version: V0.1 2017/3/14
 */
public abstract class BaseBean {

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
