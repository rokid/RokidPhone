package com.rokid.bean;

import com.rokid.bean.base.BaseBean;

/**
 * Created by fanfeng on 2017/8/22.
 */

public class DeviceConfig extends BaseBean{
    private String event_req_host;
    private String key;
    private String device_type_id;
    private String device_id;
    private String api_version;
    private String secret;

    public String getEvent_req_host() {
        return event_req_host;
    }

    public void setEvent_req_host(String event_req_host) {
        this.event_req_host = event_req_host;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDevice_type_id() {
        return device_type_id;
    }

    public void setDevice_type_id(String device_type_id) {
        this.device_type_id = device_type_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getApi_version() {
        return api_version;
    }

    public void setApi_version(String api_version) {
        this.api_version = api_version;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
