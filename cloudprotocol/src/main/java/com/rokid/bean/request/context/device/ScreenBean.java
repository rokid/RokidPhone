package com.rokid.bean.request.context.device;

/**
 * Screen information for current devices
 * <p>
 * Author: xupan.shi
 * Version: V0.1 2017/3/7
 */
public class ScreenBean {

    /**
     * the resolution in pixel for x-axis orientation.
     */
    private String x;
    /**
     * the resolution in pixel for y-axis orientation.
     */
    private String y;

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

}
