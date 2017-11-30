// IRKAudioPlayer.aidl
package com.rokid.media;

import com.rokid.media.MediaStateCallback;

// RKAudioPlayer aidl interface

interface IRKMediaPlayer {

    void setVideoPath(in String url);

    void start();

    void seekTo(in int position);

    void pause();

    void stop();

    boolean isPlaying();

    boolean canPause();

    int getDuration();

    int getPosition();

    void setMediaStateCallback(in MediaStateCallback mediaStateCallback);

}
