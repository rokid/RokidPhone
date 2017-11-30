package com.rokid.media;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.MediaController;

import com.rokid.logger.Logger;
import com.rokid.media.misc.IMediaDataSource;

import java.io.File;
import java.io.IOException;
import java.util.Map;


/**
 * Created by fanfeng on 2017/5/4.
 * <p>
 * RKAudioPlayer is a special player, it doesn't need surfaceView to display video , like an AudioPlayer .
 */

public class RKAudioPlayer implements MediaController.MediaPlayerControl {

    private String TAG = this.getClass().getSimpleName();

    private Uri mUri;
    private Map<String, String> mHeaders;

    // all possible internal states
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private static final int STATE_STOPPED = 6;

    //all media error state
    public static final int MEDIA_ERROR_TIME_OUT = -110;
    public static final int MEDIA_ERROR_IO = -1004;
    public static final int MEDIA_ERROR_MALFORMED = -1007;

    public static final int MEDIA_ERROR_UNSUPPORTED = -1010;


    public static final int MEDIA_STATE_CHECK_PERIOD = 1000;

    // mCurrentState is a VideoView object's current state.
    // mTargetState is the state that a method caller intends to reach.
    // For instance, regardless the VideoView object's current state,
    // calling pause() intends to bring the object to a target state
    // of STATE_PAUSED.
    private int mCurrentState = STATE_IDLE;
    private int mTargetState = STATE_IDLE;

    private IMediaPlayer mMediaPlayer = null;

    private OnLoadingListener mOnLoadingListener;
    private OnTruckListener mOnTruckListener;
    private OnStoppedListener mOnStoppedListener;
    private IMediaPlayer.OnCompletionListener mOnCompletionListener;
    private IMediaPlayer.OnPausedListener mOnPausedListener;
    private IMediaPlayer.OnPreparedListener mOnPreparedListener;
    private int mCurrentBufferPercentage;
    private IMediaPlayer.OnErrorListener mOnErrorListener;
    private IMediaPlayer.OnInfoListener mOnInfoListener;
    private int mSeekWhenPrepared;  // recording the seek position while preparing
    private boolean mCanPause = true;
    private boolean mCanSeekBack = true;
    private boolean mCanSeekForward = true;

    private Context mAppContext;

    private long mPrepareStartTime = 0;
    private long mPrepareEndTime = 0;

    private long mSeekStartTime = 0;
    private long mSeekEndTime = 0;

    private int truckTime = 0;
    private MediaStateCallback mMediaStateCallback;

    public RKAudioPlayer(Context mAppContext) {
        initPlayer(mAppContext);
    }

    private void initPlayer(Context mAppContext) {
        this.mAppContext = mAppContext;
        initPlayer();
    }


    private void initPlayer() {
        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release(false);

        AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        mMediaPlayer = createPlayer();

        // REMOVED: mAudioSession
        mMediaPlayer.setOnPreparedListener(mPreparedListener);
        mMediaPlayer.setOnCompletionListener(mCompletionListener);
        mMediaPlayer.setOnErrorListener(mErrorListener);
        mMediaPlayer.setOnInfoListener(mInfoListener);
        mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
        mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
        mCurrentBufferPercentage = 0;
    }

    /**
     * Sets video asset path using assetFileDescriptor
     *
     * @param assetFileDescriptor the filePath of the video.
     */
    public void setAssetVideo(AssetFileDescriptor assetFileDescriptor) throws IOException {
        if (assetFileDescriptor == null) {
            return;
        }
        initPlayer();
        Log.d(TAG, " fileDescriptor : " + assetFileDescriptor);
        mMediaPlayer.setDataSource(assetFileDescriptor);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setScreenOnWhilePlaying(true);
        mPrepareStartTime = System.currentTimeMillis();
        mMediaPlayer.prepareAsync();

        mCurrentState = STATE_PREPARING;
    }

    /**
     * Sets video path.
     *
     * @param path the path of the video.
     */
    public void setVideoPath(String path) {
        Uri uri = Uri.parse(path);
        Log.d(TAG, "uri parse result: " + uri.toString());
        setVideoURI(uri);
    }

    /**
     * Sets video URI.
     *
     * @param uri the URI of the video.
     */
    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    /**
     * Sets video URI using specific headers.
     *
     * @param uri     the URI of the video.
     * @param headers the headers for the URI request.
     *                Note that the cross cloudAppId redirection is allowed by default, but that can be
     *                changed with key/value pairs through the headers parameter with
     *                "android-allow-cross-cloudAppId-redirect" as the key and "0" or "1" as the value
     *                to disallow or allow cross cloudAppId redirection.
     */
    private void setVideoURI(Uri uri, Map<String, String> headers) {
        mUri = uri;
        mHeaders = headers;
        openVideo();
    }



    @TargetApi(Build.VERSION_CODES.M)
    private void openVideo() {
        if (mUri == null) {
            // not ready for playback just yet, will try again later
            return;
        }
        initPlayer();

        String scheme = mUri.getScheme();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    (TextUtils.isEmpty(scheme) || scheme.equalsIgnoreCase("file"))) {
                IMediaDataSource dataSource = new FileMediaDataSource(new File(mUri.toString()));
                mMediaPlayer.setDataSource(dataSource);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mMediaPlayer.setDataSource(mAppContext, mUri, mHeaders);
            } else {
                mMediaPlayer.setDataSource(mUri.toString());
            }
        } catch (IOException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
        }

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setScreenOnWhilePlaying(false);
        mPrepareStartTime = System.currentTimeMillis();
        mMediaPlayer.prepareAsync();

        mCurrentState = STATE_PREPARING;
        if (mOnLoadingListener != null) {
            Logger.d(" onLoadingStart ");
            mOnLoadingListener.onPreparing();
        }

    }

    private IMediaPlayer createPlayer() {

        return new AndroidMediaPlayer();
    }

    /*
  * release the media player in any state
  */
    public void release(boolean cleartargetstate) {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            // REMOVED: mPendingSubtitleTracks.clear();
            mCurrentState = STATE_IDLE;
            if (cleartargetstate) {
                mTargetState = STATE_IDLE;
            }
            AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
    }

    @Override
    public void start() {
        if (isInPlaybackState()) {
            mMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
            if (mOnLoadingListener != null) {
                mOnLoadingListener.onStartPlay();
            }
            if (mMediaStateCallback != null){
                try {
                    mMediaStateCallback.onStartPlay();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            startMonitorState();
        }
        mTargetState = STATE_PLAYING;
    }

    @Override
    public void pause() {
        if (isInPlaybackState()) {
            Logger.d(" mediaPlayer isPlaying " + mMediaPlayer.isPlaying());
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mCurrentState = STATE_PAUSED;
                if (mOnPausedListener != null) {
                    mOnPausedListener.onPaused(mMediaPlayer);
                }
                if (mMediaStateCallback != null){
                    try {
                        mMediaStateCallback.onPause(getCurrentPosition());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        mTargetState = STATE_PAUSED;
    }

    public void stop() {
        if (isInPlaybackState()) {
            mMediaPlayer.stop();
            mCurrentState = STATE_STOPPED;
            if (mOnStoppedListener != null) {
                Logger.d("player stopPlay");
                mOnStoppedListener.onStopped();
            }
            if (mMediaStateCallback != null) {
                try {
                    mMediaStateCallback.onStop();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            Logger.d(" onStop timer cancel");
            truckMonitorHandler.removeMessages(TRUCK_TAG);
        }
    }

    @Override
    public int getDuration() {
        if (isInPlaybackState()) {
            return (int) mMediaPlayer.getDuration();
        }
        return -1;
    }

    @Override
    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return (int) mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        if (isInPlaybackState()) {
            mMediaPlayer.seekTo(pos);
            mSeekWhenPrepared = 0;
        } else {
            mSeekWhenPrepared = pos;
        }
    }

    @Override
    public boolean isPlaying() {
        return isInPlaybackState() && mMediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        if (mMediaPlayer != null) {
            return mCurrentBufferPercentage;
        }
        return 0;
    }

    @Override
    public boolean canPause() {
        return mCanPause;
    }

    @Override
    public boolean canSeekBackward() {
        return mCanSeekBack;
    }

    @Override
    public boolean canSeekForward() {
        return mCanSeekForward;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    private boolean isInPlaybackState() {
        return (mMediaPlayer != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_IDLE &&
                mCurrentState != STATE_PREPARING);
    }

    public void setMediaStateCallback(MediaStateCallback mediaStateCallback){
        this.mMediaStateCallback = mediaStateCallback;
    }

    public void setmOnCompletionListener(IMediaPlayer.OnCompletionListener mOnCompletionListener) {
        this.mOnCompletionListener = mOnCompletionListener;
    }

    public void setmOnPausedListener(IMediaPlayer.OnPausedListener mOnPausedListener) {
        this.mOnPausedListener = mOnPausedListener;
    }

    public void setmOnStoppedListener(OnStoppedListener mOnStoppedListener) {
        this.mOnStoppedListener = mOnStoppedListener;
    }

    public void setmOnLoadingListener(OnLoadingListener mOnLoadingListener) {
        this.mOnLoadingListener = mOnLoadingListener;
    }

    public void setmOnPreparedListener(IMediaPlayer.OnPreparedListener mOnPreparedListener) {
        this.mOnPreparedListener = mOnPreparedListener;
    }

    public void setmOnTruckListener(OnTruckListener mOnTruckListener) {
        this.mOnTruckListener = mOnTruckListener;
    }

    public void setmOnErrorListener(IMediaPlayer.OnErrorListener mOnErrorListener) {
        this.mOnErrorListener = mOnErrorListener;
    }

    public void setmOnInfoListener(IMediaPlayer.OnInfoListener mOnInfoListener) {
        this.mOnInfoListener = mOnInfoListener;
    }

    IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
        public void onPrepared(IMediaPlayer mp) {
            mPrepareEndTime = System.currentTimeMillis();
            mCurrentState = STATE_PREPARED;
            truckTime = 0;
            // Get the capabilities of the player for this stream
            // REMOVED: Metadata

            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(mMediaPlayer);
            }

            if (mOnLoadingListener != null) {
                Logger.d(" loadingEnd");
                mOnLoadingListener.onPrepared();
            }

            int seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
            if (seekToPosition >= 0) {
                seekTo(seekToPosition);
            }

            if (mTargetState == STATE_PLAYING) {
                start();
            }
        }
    };

    private int startLoadPosition;

    private static final int TRUCK_TAG = 0;

    private boolean isStartTruck = false;

    private Handler truckMonitorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Logger.d(" check audio status " + mCurrentState);
            Logger.d(" currentPosition : " + getCurrentPosition());

            if (mCurrentState == STATE_PLAYING) {
                Logger.d(" truckTime : " + truckTime);
                if (getCurrentPosition() == startLoadPosition) {
                    isStartTruck = true;
                    if (mOnTruckListener != null) {
                        mOnTruckListener.onStartTruck();
                    }
                } else if (isStartTruck) {
                    truckTime = 0;
                    isStartTruck = false;
                    if (mOnTruckListener != null) {
                        mOnTruckListener.onStartPlay();
                    }
                    if (mMediaStateCallback != null){
                        try {
                            mMediaStateCallback.onStartPlay();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (isStartTruck) {
                    truckTime++;
                }

                if (truckTime >= 5) {
                    if (mOnTruckListener != null) {
                        mOnTruckListener.onTruckTimeout();
                    }
                    if (mMediaStateCallback != null){
                        mOnTruckListener.onTruckTimeout();
                    }
                    truckMonitorHandler.removeMessages(TRUCK_TAG);
                }
                truckMonitorHandler.sendEmptyMessageDelayed(TRUCK_TAG, MEDIA_STATE_CHECK_PERIOD);
            }

        }
    };

    private void startMonitorState() {

        startLoadPosition = getCurrentPosition();

        truckMonitorHandler.sendEmptyMessageDelayed(TRUCK_TAG, MEDIA_STATE_CHECK_PERIOD);

    }

    private IMediaPlayer.OnCompletionListener mCompletionListener =
            new IMediaPlayer.OnCompletionListener() {
                public void onCompletion(IMediaPlayer mp) {
                    mCurrentState = STATE_PLAYBACK_COMPLETED;
                    mTargetState = STATE_PLAYBACK_COMPLETED;
                    Logger.d("onComplete timer cancel");
                    truckMonitorHandler.removeMessages(TRUCK_TAG);
                    if (mOnCompletionListener != null) {
                        mOnCompletionListener.onCompletion(mMediaPlayer);
                    }
                    if (mMediaStateCallback != null){
                        try {
                            mMediaStateCallback.onCompletion();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };


    private IMediaPlayer.OnErrorListener mErrorListener =
            new IMediaPlayer.OnErrorListener() {
                public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                    Log.d(TAG, "Error: " + framework_err + "," + impl_err);
                    mCurrentState = STATE_ERROR;
                    mTargetState = STATE_ERROR;
                    Logger.d(" onError timer cancel");
                    truckMonitorHandler.removeMessages(TRUCK_TAG);

//                     If an error handler has been supplied, use it and finish.
                    if (mOnErrorListener != null) {
                        if (mOnErrorListener.onError(mMediaPlayer, framework_err, impl_err)) {
                            return true;
                        }
                    }

                    if (mMediaStateCallback != null){
                        try {
                            mMediaStateCallback.onError(framework_err, impl_err);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    return true;
                }
            };

    private IMediaPlayer.OnInfoListener mInfoListener =
            new IMediaPlayer.OnInfoListener() {
                public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                    if (mOnInfoListener != null) {
                        mOnInfoListener.onInfo(mp, arg1, arg2);
                    }
                    switch (arg1) {
                        case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                            Log.d(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                            Log.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                            Log.d(TAG, "MEDIA_INFO_BUFFERING_START:");
                            Logger.d("MEDIA_INFO_BUFFERING_START");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                            Log.d(TAG, "MEDIA_INFO_BUFFERING_END:");
                            Logger.d("MEDIA_INFO_BUFFERING_END");
                            break;
                        case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                            Log.d(TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + arg2);
                            break;
                        case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                            Log.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                            Log.d(TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                            Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                            Log.d(TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                            Log.d(TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                            Log.d(TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                            break;
                    }
                    return true;
                }
            };

    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
            new IMediaPlayer.OnBufferingUpdateListener() {
                public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                    Logger.d(" onBufferingUpdate percent : " + percent);
                    mCurrentBufferPercentage = percent;
                }
            };

    private IMediaPlayer.OnSeekCompleteListener mSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() {

        @Override
        public void onSeekComplete(IMediaPlayer mp) {
            mSeekEndTime = System.currentTimeMillis();
        }
    };


    public interface OnStoppedListener {

        void onStopped();

    }

    public interface OnLoadingListener {

        void onPreparing();

        void onPrepared();

        void onStartPlay();
    }


    public interface OnTruckListener {

        void onStartTruck();

        void onTruckTimeout();

        void onStartPlay();
    }

}
