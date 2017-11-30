package com.rokid.light;

/**
 * Created by fanfeng on 2017/9/21.
 */

public abstract class BaseLightHelper {

    public LightStateCallback lightStateCallback;

    public void registerLightStateCallback(LightStateCallback lightStateCallback){
        this.lightStateCallback = lightStateCallback;
    }

    public abstract void openLoadingLight();

    public abstract void closeLight();

    public interface LightStateCallback{

        void onLightOpened();

        void onLightClosed();
    }

}
