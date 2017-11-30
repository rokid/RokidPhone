package com.rokid.reporter;

/**
 * Created by fanfeng on 2017/5/9.
 */

public class VoiceReporter extends BaseReporter {

    public static String STARTED = "Voice.STARTED";
    public static String FINISHED = "Voice.FINISHED";
    public static String FAILED = "Voice.FAILED";

    public VoiceReporter(String appId, String event, ReporterResponseCallback reporterResponseCallback){
        super(appId, event, reporterResponseCallback);
    }

    public VoiceReporter(String appId, String event, String extra, ReporterResponseCallback reporterResponseCallback) {
        super(appId, event, extra, reporterResponseCallback);
    }
}
