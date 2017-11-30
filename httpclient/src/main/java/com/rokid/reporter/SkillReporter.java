package com.rokid.reporter;

/**
 * Created by fanfeng on 2017/10/18.
 */

public class SkillReporter extends BaseReporter {

    public static final String EXIT = "Skill.EXIT";

    public SkillReporter(String appId, String event, ReporterResponseCallback reporterResponseCallback) {
        super(appId, event, reporterResponseCallback);
    }

}