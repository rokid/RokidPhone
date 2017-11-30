// MediaStateCallback.aidl
package com.rokid.media;

// media state callback interface

interface MediaStateCallback {

    void onStartPlay();

    void onTruckTimeout();

    void onPause(int position);

    void onStop();

    void onCompletion();

    void onError(int what, int extra);

}
