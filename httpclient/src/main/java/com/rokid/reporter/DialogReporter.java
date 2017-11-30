package com.rokid.reporter;

/**
 * Created by fanfeng on 2017/9/27.
 */

public class DialogReporter extends BaseReporter {

    public static final String DIALOG_DISMISS = "DIALOG_DISMISS";

    public DialogReporter(String appId, String event, ReporterResponseCallback reporterResponseCallback) {
        super(appId, event, reporterResponseCallback);
    }
}
