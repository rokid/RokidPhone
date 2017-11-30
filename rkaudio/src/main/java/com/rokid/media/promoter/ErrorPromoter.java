package com.rokid.media.promoter;

import android.content.Context;
import android.content.res.AssetManager;

import com.rokid.logger.Logger;

import java.io.IOException;
import java.lang.ref.WeakReference;

import com.rokid.media.IMediaPlayer;
import com.rokid.media.RKAudioPlayer;

/**
 * Created by fanfeng on 2017/7/12.
 */

public class ErrorPromoter {

    private static volatile ErrorPromoter errorPromoteHelper;
    private RKAudioPlayer rkAudioPlayer;
    private AssetManager assetManager;
    private WeakReference<Context> mContextRef;
    private ErrorPromoteCallback errorPromoteCallback;

    public ErrorPromoter() {
        initAudioPlayer();
    }

    public void registerContext(WeakReference<Context> reference) {
        this.mContextRef = reference;
        initAudioPlayer();
    }

    private void initAudioPlayer() {
        if (mContextRef == null || mContextRef.get() == null){
            Logger.d("mContext is null!  you should set context !");
            return;
        }
        assetManager = mContextRef.get().getAssets();
        rkAudioPlayer = new RKAudioPlayer(mContextRef.get());
        rkAudioPlayer.setmOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                if (errorPromoteCallback != null){
                    errorPromoteCallback.onPromoteStarted();
                }
            }
        });
        rkAudioPlayer.setmOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                if (errorPromoteCallback != null){
                    errorPromoteCallback.onPromoteFinished();
                }
                rkAudioPlayer.release(true);
            }
        });
    }

    public static ErrorPromoter getInstance() {
        if (errorPromoteHelper == null) {
            synchronized (ErrorPromoter.class) {
                if (errorPromoteHelper == null)
                    errorPromoteHelper = new ErrorPromoter();
            }
        }
        return errorPromoteHelper;
    }

    public void speakErrorPromote(ERROR_TYPE errorType, ErrorPromoteCallback errorPromoteCallback) throws IOException {
        Logger.d(" speakErrorPromote errorType is " + errorType);
        this.errorPromoteCallback = errorPromoteCallback;
        switch (errorType) {
            case NET_BAD:
                //网络情况较差，请稍后再试
                rkAudioPlayer.setAssetVideo(assetManager.openFd("network_bad.wav"));
            case DATA_INVALID:
                //遇到了一点小问题，稍后再试一下吧
                rkAudioPlayer.setAssetVideo(assetManager.openFd("common_error.mp3"));
                break;
        }
        rkAudioPlayer.start();
    }

    public interface ErrorPromoteCallback {
        void onPromoteStarted();
        void onPromoteFinished();
    }

    public enum ERROR_TYPE {
        DATA_INVALID,
        NET_BAD,
        MEDIA_TIME_OUT,
        MEDIA_ERROR,
        TTS_ERROR
    }

}
