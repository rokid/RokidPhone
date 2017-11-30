package com.rokid.reporter;

/**
 * Created by fanfeng on 2017/5/9.
 */

public class MediaReporter extends BaseReporter {

    public static final String STARTED = "Media.STARTED";
    public static final String PAUSED = "Media.PAUSED";
    public static final String STOPPED = "Media.STOPPED";
    public static final String FINISHED = "Media.FINISHED";
    public static final String FAILED = "Media.FAILED";
    public static final String TIMEOUT = "Media.TIMEOUT";
    public static final String STATE = "Media.STATUS";

    public MediaReporter(String appId, String event,ReporterResponseCallback reporterResponseCallback){
        super(appId, event, reporterResponseCallback);
    }

    public MediaReporter(String appId, String event, String extra, ReporterResponseCallback reporterResponseCallback) {
        super(appId, event, extra, reporterResponseCallback);
    }
}
