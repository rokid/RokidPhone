package com.rokid.bean.request.context.device;

/**
 * DeviceInfo presents the main status of the device that created current request.
 * <p>
 * Author: xupan.shi
 * Version: V0.1 2017/3/7
 */
public class DeviceBean {

    private BasicBean basic;
    private ScreenBean screen;
    private MediaBean media;
    private LocationBean location;

    public BasicBean getBasic() {
        return basic;
    }

    public void setBasic(BasicBean basic) {
        this.basic = basic;
    }

    public ScreenBean getScreen() {
        return screen;
    }

    public void setScreen(ScreenBean screen) {
        this.screen = screen;
    }

    public MediaBean getMedia() {
        return media;
    }

    public void setMedia(MediaBean media) {
        this.media = media;
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

}
