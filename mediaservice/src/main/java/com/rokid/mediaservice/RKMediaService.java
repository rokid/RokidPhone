package com.rokid.mediaservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.rokid.media.IRKMediaPlayer;
import com.rokid.media.MediaStateCallback;
import com.rokid.media.RKAudioPlayer;

/**
 * Created by fanfeng on 2017/10/19.
 */

public class RKMediaService extends Service{

    private RKAudioPlayer rkAudioPlayer;

    @Override
    public void onCreate() {
        rkAudioPlayer = new RKAudioPlayer(this);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private IBinder binder = new IRKMediaPlayer.Stub()

    {

        @Override
        public void setVideoPath(String url) throws RemoteException {
            rkAudioPlayer.setVideoPath(url);
        }

        @Override
        public void start() throws RemoteException {
            rkAudioPlayer.start();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            rkAudioPlayer.seekTo(position);
        }

        @Override
        public void pause() throws RemoteException {
            rkAudioPlayer.pause();
        }

        @Override
        public void stop() throws RemoteException {
            rkAudioPlayer.stop();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return rkAudioPlayer.isPlaying();
        }

        @Override
        public boolean canPause() throws RemoteException {
            return rkAudioPlayer.canPause();
        }

        @Override
        public int getDuration() throws RemoteException {
            return rkAudioPlayer.getDuration();
        }

        @Override
        public int getPosition() throws RemoteException {
            return rkAudioPlayer.getCurrentPosition();
        }

        @Override
        public void setMediaStateCallback(MediaStateCallback mediaStateCallback) throws RemoteException {
            rkAudioPlayer.setMediaStateCallback(mediaStateCallback);
        }

    };
}
