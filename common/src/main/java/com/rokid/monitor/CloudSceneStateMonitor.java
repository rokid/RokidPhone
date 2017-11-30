package com.rokid.monitor;

import com.rokid.bean.response.responseinfo.action.ActionBean;
import com.rokid.logger.Logger;

/**
 * Created by fanfeng on 2017/6/14.
 */

public class CloudSceneStateMonitor extends BaseCloudStateMonitor {

    public static CloudSceneStateMonitor getInstance() {
        return AppStateManagerHolder.instance;
    }

    private static class AppStateManagerHolder {
        private static final CloudSceneStateMonitor instance = new CloudSceneStateMonitor();
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        mediaAction.pausePlay();
        voiceAction.pausePlay();
    }

    @Override
    public void onStop() {
        Logger.d("CloudSceneState onStop ");

    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        Logger.d("scene  onAppResume mediaType: " + currentMediaState + " voiceType : " + currentVoiceState + " userMediaControlType: " + userMediaControlType + " userVoiceControlType: " + userVoiceControlType);

        //应用onResume的时候要考虑到用户上次操作是否是暂停
        if (currentMediaState == MEDIA_STATE.PAUSED && !(userMediaControlType == USER_MEDIA_CONTROL_TYPE.PAUSED)) {
            Logger.d("scene: onAppResume resume play audio");
            mediaAction.resumePlay();
        }

        if (currentVoiceState == VOICE_STATE.PAUSED && !(userVoiceControlType == USER_VOICE_CONTROL_TYPE.PAUSED)) {
            Logger.d("scene onAppResume play voice");
            voiceAction.resumePlay();
        }
    }

    @Override
    public void onDestroy() {
        mediaAction.stopPlay();
        voiceAction.stopPlay();
        super.onDestroy();
    }

    @Override
    public String getFormType() {
        return ActionBean.FORM_SCENE;
    }

    @Override
    public void onSirenOpened() {

    }

}
