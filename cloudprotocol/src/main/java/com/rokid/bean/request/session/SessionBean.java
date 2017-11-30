package com.rokid.bean.request.session;

import java.util.Map;

/**
 * Session indicates the session for the CloudApp that currently be requested.
 * Whenever a request is created, the session information will be updated by system.
 * <p>
 * Author: xupan.shi
 * Version: V0.1 2017/3/7
 */
public class SessionBean {

    private String sessionId;
    /**
     * indicates whether it is a new session
     */
    private boolean newSession;
    private String applicationId;
    /**
     * session attributes set by CloudApp in Response
     */
    private Map<String, Object> attributes;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isNewSession() {
        return newSession;
    }

    public void setNewSession(boolean newSession) {
        this.newSession = newSession;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
