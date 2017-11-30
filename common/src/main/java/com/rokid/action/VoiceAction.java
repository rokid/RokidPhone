package com.rokid.action;

import android.os.RemoteException;

import com.rokid.bean.response.responseinfo.action.voice.VoiceBean;
import com.rokid.bean.response.responseinfo.action.voice.VoiceItemBean;
import com.rokid.logger.Logger;
import com.rokid.monitor.BaseCloudStateMonitor;
import com.rokid.tts.ITts;
import com.rokid.tts.ITtsCallback;

public class VoiceAction extends BaseAction<VoiceBean> {

    private VoiceBean voiceBean;
    private ITts mTts;

    public VoiceAction(BaseCloudStateMonitor cloudStateMonitor) {
        super(cloudStateMonitor);
    }

    public void setTTS(ITts tts){
        this.mTts = tts;
    }

    @Override
    public void userStartPlay(VoiceBean actionBean) {
        if (actionBean.isValid()) {
            cloudStateMonitor.setCurrentVoiceState(BaseCloudStateMonitor.VOICE_STATE.STARTED);
            cloudStateMonitor.setUserVoiceControlType(BaseCloudStateMonitor.USER_VOICE_CONTROL_TYPE.STARTED);
            this.voiceBean = actionBean;
            VoiceItemBean voiceItemBean = actionBean.getItem();
            String ttsContent;
            ttsContent = voiceItemBean.getTts();
            try {
                mTts.cancelByOwner();
                mTts.speak(ttsContent, ttsCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private ITtsCallback ttsCallback = new ITtsCallback.Stub() {
        @Override
        public void onStart(int id) throws RemoteException {
            cloudStateMonitor.onVoiceStarted();
        }

        @Override
        public void onComplete(int id) throws RemoteException {
            cloudStateMonitor.onVoiceFinished();
        }

        @Override
        public void onCancel(int id) throws RemoteException {
            cloudStateMonitor.onVoiceStopped();
        }

        @Override
        public void onError(int id, int err) throws RemoteException {
            cloudStateMonitor.onVoiceFailed();
        }
    };

    @Override
    public synchronized void pausePlay() {
        Logger.d("pause play voice");
        try {
            mTts.pause();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void resumePlay() {
        Logger.d("resume play voiceBean " + voiceBean);
        if (voiceBean != null) {
            userStartPlay(voiceBean);
        }
    }

    @Override
    public synchronized void stopPlay() {
        Logger.d("stop play voice");
        voiceBean = null;
        try {
            mTts.cancelByOwner();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void userPausedPlay() {
        pausePlay();
        cloudStateMonitor.setUserVoiceControlType(BaseCloudStateMonitor.USER_VOICE_CONTROL_TYPE.PAUSED);
    }

    @Override
    public void userResumePlay() {
        resumePlay();
        cloudStateMonitor.setUserVoiceControlType(BaseCloudStateMonitor.USER_VOICE_CONTROL_TYPE.RESUMED);
    }

    @Override
    public synchronized void userStopPlay() {
        stopPlay();
        cloudStateMonitor.setUserVoiceControlType(BaseCloudStateMonitor.USER_VOICE_CONTROL_TYPE.STOPPED);
    }

    @Override
    public void getStatus() {

    }

    @Override
    public void forward() {

    }

    @Override
    public void backward() {

    }

    @Override
    public ACTION_TYPE getActionType() {
        return ACTION_TYPE.VOICE;
    }

    public VoiceBean getVoiceBean() {
        return voiceBean;
    }
}
