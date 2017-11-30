package com.rokid.bean.request.context.device;

/**
 * The location of a device can be generated in several ways.
 * The location shows the location or the device and also indicates how the information be generated.
 * LBS based CloudApps can fetch location information.
 * latitude and longitude are provided.
 * <p>
 * Author: xupan.shi
 * Version: V0.1 2017/3/7
 */
public class LocationBean {

    private String latitude;
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
