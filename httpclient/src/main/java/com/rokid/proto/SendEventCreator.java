package com.rokid.proto;

/**
 * Created by fanfeng on 2017/5/11.
 */

public class SendEventCreator {

    public static SendEvent.SendEventRequest generateSendEventRequest(String appId, String event, String extra) {
        SendEvent.SendEventRequest sendEventRequest = SendEvent.SendEventRequest
                .newBuilder()
                .setAppId(appId)
                .setEvent(event)
                .setExtra(extra)
                .build();

        return sendEventRequest;
    }

}
