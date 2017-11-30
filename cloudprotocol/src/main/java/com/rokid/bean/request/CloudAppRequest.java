package com.rokid.bean.request;

import com.rokid.bean.request.context.ContextBean;
import com.rokid.bean.request.requestinfo.RequestBean;
import com.rokid.bean.request.session.SessionBean;

/**
 * The Request is used to fetch response from CloudApps,which is sent by CloudDispatcher.
 * Currently IntentRequest and EventRequest are available.
 * IntentRequest is created according an NLP intent.
 * And EventRequest is created when an event occurs.
 * <p>
 * Author: xupan.shi
 * Version: V0.1 2017/3/7
 */
public class CloudAppRequest {

    private String version;
    private SessionBean session;
    private ContextBean context;
    private RequestBean request;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public SessionBean getSession() {
        return session;
    }

    public void setSession(SessionBean session) {
        this.session = session;
    }

    public ContextBean getContext() {
        return context;
    }

    public void setContext(ContextBean context) {
        this.context = context;
    }

    public RequestBean getRequest() {
        return request;
    }

    public void setRequest(RequestBean request) {
        this.request = request;
    }
}
