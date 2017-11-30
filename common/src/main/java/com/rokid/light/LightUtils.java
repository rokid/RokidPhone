package com.rokid.light;

/**
 * Created by fanfeng on 2017/9/21.
 */

public class LightUtils {

    private static LightUtils lightUtils;

    private BaseLightHelper lightHelper;

    public static LightUtils getInstance(){
        if (lightUtils == null){
            lightUtils = new LightUtils();
        }
        return lightUtils;
    }

    public void setLightHelper(BaseLightHelper lightHelper){
        this.lightHelper = lightHelper;
    }


    public BaseLightHelper getLightHelper() {
        return lightHelper;
    }
}
