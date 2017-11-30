package com.rokid.reporter;

/**
 * Created by fanfeng on 2017/9/17.
 */

public class EventParamUtils {

    private static BaseRuntimeCreator mEventParamCreator;

    public static void setEventParamCreator(BaseRuntimeCreator eventParamCreator){
        mEventParamCreator = eventParamCreator;
    }

    public static BaseRuntimeCreator getEventParamCreator(){
        return mEventParamCreator;
    }
}
