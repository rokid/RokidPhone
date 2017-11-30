package com.rokid.monitor;

/**
 * Created by fanfeng on 2017/6/14.
 */

public interface MediaStateCallback {

    void onMediaStarted();

    void onTruckTimeout();

    void onMediaPaused(int position);

    void onMediaResumed();

    void onMediaStopped();

    void onMediaFinished();

    void onMediaFailed(int errorCode);
}
