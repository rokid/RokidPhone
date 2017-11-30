package com.rokid.bean.request.context.device;

/**
 * Current media player status is offered by MediaStatus
 * <p>
 * Author: xupan.shi
 * Version: V0.1 2017/3/7
 */
public class MediaBean {

    /**
     * media player state.
     * ONLY state PLAYING and PAUSED are available currently
     */
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
