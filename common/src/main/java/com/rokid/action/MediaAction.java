package com.rokid.action;

import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;

import com.rokid.bean.response.responseinfo.action.media.MediaBean;
import com.rokid.bean.response.responseinfo.action.media.MediaItemBean;
import com.rokid.logger.Logger;
import com.rokid.media.IRKMediaPlayer;
import com.rokid.media.MediaStateCallback;
import com.rokid.monitor.BaseCloudStateMonitor;
import com.rokid.reporter.MediaReporter;

import java.lang.ref.WeakReference;


public class MediaAction extends BaseAction<MediaBean> {

//    private mRKMediaPlayer mRKMediaPlayer;
    private IRKMediaPlayer mRKMediaPlayer;
    private MediaItemBean mediaBeanItem;

    public MediaAction(BaseCloudStateMonitor cloudStateMonitor) {
        super(cloudStateMonitor);
    }

    public void setRKMediaPlayer(IRKMediaPlayer rkMediaPlayer){
        this.mRKMediaPlayer = rkMediaPlayer;
        initmRKMediaPlayer();
    }

    @Override
    public void registerContext(WeakReference<Context> contextWeakReference) {
        super.registerContext(contextWeakReference);
    }

    private void initmRKMediaPlayer() {

        try {
            mRKMediaPlayer.setMediaStateCallback(new MediaStateCallback.Stub() {
                @Override
                public void onStartPlay() throws RemoteException {
                    Logger.d("onStartPlay");
                    cloudStateMonitor.onMediaStarted();
                }

                @Override
                public void onTruckTimeout() throws RemoteException {
                    Logger.d("onTruckTimeout");
                    cloudStateMonitor.onTruckTimeout();
                }

                @Override
                public void onPause(int position) throws RemoteException {
                    Logger.d("onPause position: " + position);
                    cloudStateMonitor.onMediaPaused(position);
                }

                @Override
                public void onStop() throws RemoteException {
                    Logger.d("onStop");
                    cloudStateMonitor.onMediaStopped();
                }

                @Override
                public void onCompletion() throws RemoteException {
                    Logger.d("onCompletion");
                    cloudStateMonitor.onMediaFinished();
                }

                @Override
                public void onError(int what, int extra) throws RemoteException {
                    Logger.d(" onMediaFailed what : " + what + " extra :" + extra);
                    cloudStateMonitor.onMediaFailed(extra);
                }

            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }

/*        mRKMediaPlayer.setmOnLoadingListener(new mRKMediaPlayer.OnLoadingListener() {
            @Override
            public void onPreparing() {
                Logger.d("onLoadStart");
                LightUtils.getInstance().getLightHelper().openLoadingLight();
            }

            @Override
            public void onPrepared() {

            }

            @Override
            public void onStartPlay() {
                Logger.d("onStartPlay");
                cloudStateMonitor.onMediaStarted();
                LightUtils.getInstance().getLightHelper().closeLight();
            }
        });

        mRKMediaPlayer.setmOnTruckListener(new mRKMediaPlayer.OnTruckListener() {

            @Override
            public void onStartTruck() {
                Logger.d("onStartTruck");
                LightUtils.getInstance().getLightHelper().openLoadingLight();
            }

            @Override
            public void onStartPlay() {
                Logger.d("onPlaying");
                LightUtils.getInstance().getLightHelper().closeLight();
            }

            @Override
            public void onTruckTimeout() {
                Logger.d("onTruckTimeout");
                cloudStateMonitor.onTruckTimeout();
            }
        });

        mRKMediaPlayer.setmOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                Logger.d(" onMediaFailed what : " + what + " extra :" + extra);
                cloudStateMonitor.onMediaFailed(extra);
                LightUtils.getInstance().getLightHelper().closeLight();
                return false;
            }
        });

        mRKMediaPlayer.setmOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                Logger.d("onMediaInfo what : " + what + " extra: " + extra);
                return false;
            }
        });

        mRKMediaPlayer.setmOnPausedListener(new IMediaPlayer.OnPausedListener() {
            @Override
            public void onPaused(IMediaPlayer mp) {
                cloudStateMonitor.onMediaPaused((int) mp.getCurrentPosition());
            }
        });

        mRKMediaPlayer.setmOnStoppedListener(new mRKMediaPlayer.OnStoppedListener() {
            @Override
            public void onStopped() {
                cloudStateMonitor.onMediaStopped();

            }
        });

        mRKMediaPlayer.setmOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                cloudStateMonitor.onMediaFinished();
            }
        });*/
    }

    public synchronized void userStartPlay(MediaBean mediaBean) {
        Logger.d(" mRKMediaPlayer is null ? " + (mRKMediaPlayer == null));
        if (mediaBean == null) {
            Logger.d(" userStartPlay mediaBean is null ");
            return;
        }

        if (mRKMediaPlayer == null) {
            Logger.d("mRKMediaPlayer is null , please check mediaPlayerService isConnect !");
            return;
        }

        if (mediaBean.getItem() == null) {
            Logger.d("start play media mediaBeanItem null!");
            return;
        }

        mediaBeanItem = mediaBean.getItem();

        Logger.d("play mediaBean : " + mediaBean);

        String url = mediaBeanItem.getUrl();
        if (!TextUtils.isEmpty(mediaBeanItem.getToken())) {
            Logger.d("token not null ! token: " + mediaBeanItem.getToken());
            if (url.contains("?")) {
                url = url + "&token=" + mediaBeanItem.getToken();
            } else {
                url = url + "?token=" + mediaBeanItem.getToken();
            }
        } else {
            Logger.d("token is null! ");
        }

        if (TextUtils.isEmpty(url)) {
            Logger.d("media url invalidate!");
            return;
        }

        Logger.d("start play media url : " + url);
        cloudStateMonitor.setUserMediaControlType(BaseCloudStateMonitor.USER_MEDIA_CONTROL_TYPE.STARTED);
        cloudStateMonitor.setCurrentMediaState(BaseCloudStateMonitor.MEDIA_STATE.STARTED);
        try {
            mRKMediaPlayer.setVideoPath(url);
            Logger.d("audio seekTo " + mediaBeanItem.getOffsetInMilliseconds());
            mRKMediaPlayer.seekTo(mediaBeanItem.getOffsetInMilliseconds());
            mRKMediaPlayer.start();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
//        mRKMediaPlayer.setVideoURI(Uri.parse(url));

    }

    @Override
    public synchronized void pausePlay() {
        Logger.d(" pausePlay mRKMediaPlayer " + (mRKMediaPlayer != null));
        if (mRKMediaPlayer != null ) {
            Logger.d(" pause rkAudio");
            try {
                mRKMediaPlayer.pause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void stopPlay() {
        if (mRKMediaPlayer != null) {
            try {
                mRKMediaPlayer.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void resumePlay() {
        try {
            if (mRKMediaPlayer != null && !mRKMediaPlayer.isPlaying()) {
                mRKMediaPlayer.start();
                cloudStateMonitor.onMediaResumed();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void userPausedPlay() {
        pausePlay();
        cloudStateMonitor.setUserMediaControlType(BaseCloudStateMonitor.USER_MEDIA_CONTROL_TYPE.PAUSED);
    }

    @Override
    public synchronized void userStopPlay() {
        stopPlay();
        cloudStateMonitor.setUserMediaControlType(BaseCloudStateMonitor.USER_MEDIA_CONTROL_TYPE.STOPPED);
    }

    @Override
    public synchronized void userResumePlay() {
        resumePlay();
        cloudStateMonitor.setUserMediaControlType(BaseCloudStateMonitor.USER_MEDIA_CONTROL_TYPE.RESUMED);
    }

    @Override
    public synchronized void forward() {
        /*if (mRKMediaPlayer != null && !mRKMediaPlayer.canSeekForward()) {
            int totalTime = mRKMediaPlayer.getDuration();
            int currentTime = mRKMediaPlayer.getCurrentPosition();
            int seekTime = currentTime + totalTime / 10;
            if (seekTime > totalTime) {
                seekTime = totalTime;
            }
            mRKMediaPlayer.seekTo(seekTime);
        }*/
    }

    @Override
    public synchronized void backward() {
      /*  if (mRKMediaPlayer != null && !mRKMediaPlayer.canSeekForward()) {
            int totalTime = mRKMediaPlayer.getDuration();
            int currentTime = mRKMediaPlayer.getCurrentPosition();
            int seekTime = currentTime - totalTime / 10;
            if (seekTime <= 0) {
                seekTime = 0;
            }
            mRKMediaPlayer.seekTo(seekTime);
        }*/
    }

    @Override
    public void getStatus() {
        cloudStateMonitor.sendMediaReporter(MediaReporter.STATE);
    }

    public int getMediaDuration() {
        if (mRKMediaPlayer == null) {
            return 0;
        }
        try {
            return mRKMediaPlayer.getDuration();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getMediaPosition() {
        if (mRKMediaPlayer == null) {
            return 0;
        }
        try {
            return mRKMediaPlayer.getPosition();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public MediaItemBean getMediaBeanItem() {
        return mediaBeanItem;
    }

    public void releasePlayer() {
        /*if (mRKMediaPlayer != null) {
            mRKMediaPlayer.release(true);
            try {
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mRKMediaPlayer = null;
        }*/
    }

    @Override
    public ACTION_TYPE getActionType() {
        return ACTION_TYPE.MEDIA;
    }

}
