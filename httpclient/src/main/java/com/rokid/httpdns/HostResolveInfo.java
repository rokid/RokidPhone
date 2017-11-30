package com.rokid.httpdns;

/**
 * Created by showingcp on 10/10/16.
 */
public class HostResolveInfo {
    private boolean success;
    private String hostResovled;
    private String originHost;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getHostResovled() {
        return hostResovled;
    }

    public void setHostResovled(String hostResovled) {
        this.hostResovled = hostResovled;
    }

    public String getOriginHost() {
        return originHost;
    }

    public void setOriginHost(String originHost) {
        this.originHost = originHost;
    }
}
