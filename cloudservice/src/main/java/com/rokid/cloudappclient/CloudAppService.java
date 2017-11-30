package com.rokid.cloudappclient;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.rokid.bean.ActionNode;
import com.rokid.http.BaseUrlConfig;
import com.rokid.logger.Logger;
import com.rokid.media.IRKMediaPlayer;
import com.rokid.media.promoter.ErrorPromoter;
import com.rokid.monitor.BaseCloudStateMonitor;
import com.rokid.monitor.CloudSceneStateMonitor;
import com.rokid.parser.ResponseParser;
import com.rokid.tts.ITts;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class CloudAppService extends Service implements BaseCloudStateMonitor.TaskProcessCallback {

    private String action;

    public CloudAppService() {

    }

    public static final String ACTION_KEY = "action";

    public static final String TTS_SERVICE_NAME = "com.rokid.tts.TtsService";
    public static final String MEDIA_PACKAGE_NAME = "com.rokid.voicerec";
    public static final String MEDIA_SERVICE_NAME = "com.rokid.mediaservice.RKMediaService";

    @Override
    public void onCreate() {
        super.onCreate();
        WeakReference<Context> weakReference = new WeakReference<Context>(this);
        BaseUrlConfig.initEventParam(weakReference);
        CloudSceneStateMonitor.getInstance().registerContext(weakReference);
        CloudSceneStateMonitor.getInstance().setTaskProcessCallback(new WeakReference<BaseCloudStateMonitor.TaskProcessCallback>(this));

        ErrorPromoter.getInstance().registerContext(weakReference);

        bindRKMediaService();
        bindTTSService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Logger.d(" service start ");

        if (intent == null) {
            Logger.d("  intent is null!");
        } else {
            action = intent.getStringExtra(ACTION_KEY);
            if (tts != null && mediaPlayer != null) {
                actionExecute();
            } else {
                bindRKMediaService();
                bindTTSService();
            }
        }

        return START_STICKY;
    }

    private void actionExecute() {
        ActionNode actionNode = null;
        try {
            actionNode = ResponseParser.getInstance().parseAction(action);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CloudSceneStateMonitor.getInstance().setAudioPlayer(mediaPlayer);
        CloudSceneStateMonitor.getInstance().setTTS(tts);
        CloudSceneStateMonitor.getInstance().onNewIntentActionNode(actionNode);
    }

    private void bindTTSService() {
        Intent intent = new Intent().setAction(TTS_SERVICE_NAME);
        Intent ttsIntent = IntentUtil.createExplicitFromImplicitIntent(this,intent);
        if (ttsIntent == null){
            Logger.d(" ttsIntent is null !");
            return;
        }
        boolean b = bindService(ttsIntent, ttsConnection, Context.BIND_AUTO_CREATE);
        Logger.d(" bindTTSService return : " + b);
    }

    private ITts tts;
    private ServiceConnection ttsConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            tts = ITts.Stub.asInterface(service);
            Logger.d("bindTTSService success.");

            if (tts != null && mediaPlayer != null) {
                actionExecute();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bindTTSService();
        }
    };


    private void bindRKMediaService() {
        Intent intent = new Intent().setAction(MEDIA_SERVICE_NAME);
        Intent mediaIntent = IntentUtil.createExplicitFromImplicitIntent(this, intent);
        if (mediaIntent == null){
            Logger.d(" mediaIntent is null !");
            return;
        }
        boolean mediaBind = bindService(mediaIntent, mediaConnection, Context.BIND_AUTO_CREATE);
        Logger.d("bindMediaService return : " + mediaBind);
    }


    private IRKMediaPlayer mediaPlayer;
    private ServiceConnection mediaConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.d("bind RKAudioService success");
            mediaPlayer = IRKMediaPlayer.Stub.asInterface(service);
            if (tts != null && mediaPlayer != null){
                actionExecute();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.d("onDisconnect RKAudioService");
            bindRKMediaService();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        Logger.d(" onBind ");

//        EventParamUtils.setEventParamCreator(new SystemServiceHelper());

        return null;
    }

    @Override
    public void openSiren(String type, boolean pickupEnable, int durationInMilliseconds) {
        Intent intent = new Intent();
        ComponentName component = new ComponentName("com.rokid.activation", "com.rokid.activation.service.CoreService");
        intent.setComponent(component);
        intent.putExtra("FromType", type);
        intent.putExtra("InputAction", "confirmEvent");
        Bundle bundle = new Bundle();
        bundle.putBoolean("isConfirm", pickupEnable);//拾音打开或关闭
        bundle.putInt("durationInMilliseconds", durationInMilliseconds);//当enable=true时，在用户不说话的情况下，拾音打开持续时间
        intent.putExtra("intent", bundle);
        startService(intent);
    }

    @Override
    public void exitSessionToAppEngine() {

    }

    @Override
    public void onTaskFinished() {

    }

    @Override
    public void onExitCallback() {

    }
}
