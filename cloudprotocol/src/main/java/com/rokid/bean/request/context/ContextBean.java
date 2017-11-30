package com.rokid.bean.request.context;

import com.rokid.bean.request.context.device.DeviceBean;

/**
 * Context indicates the main status of current device, the user bound and the application.
 * Information in Context may help CloudApps to determin further actions and to choose proper version of response.
 * <p>
 * Author: xupan.shi
 * Version: V0.1 2017/3/7
 */
public class ContextBean {

    /**
     * The application info
     */
    private ApplicationBean application;
    /**
     * The device info
     */
    private DeviceBean device;
    /**
     * User info
     */
    private UserBean user;

    public ApplicationBean getApplication() {
        return application;
    }

    public void setApplication(ApplicationBean application) {
        this.application = application;
    }

    public DeviceBean getDevice() {
        return device;
    }

    public void setDevice(DeviceBean device) {
        this.device = device;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

}
