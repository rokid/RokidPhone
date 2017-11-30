package com.rokid.parser;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.rokid.bean.ActionNode;
import com.rokid.bean.ActionNodeHelper;
import com.rokid.bean.response.CloudActionResponseBean;
import com.rokid.logger.Logger;
import com.rokid.media.promoter.ErrorPromoter;
import com.rokid.monitor.BaseCloudStateMonitor;
import com.rokid.proto.SendEvent;
import com.rokid.reporter.BaseReporter;
import com.squareup.okhttp.Response;

import java.io.IOException;

//import com.android.okhttp.Response;

/**
 * Created by fanfeng on 2017/6/1.
 */

public class ResponseParser {

    private static ResponseParser parser;

    public static ResponseParser getInstance() {
        if (parser == null) {
            synchronized (ResponseParser.class) {
                if (parser == null)
                    parser = new ResponseParser();
            }
        }
        return parser;
    }

    public ActionNode parseAction(String actionStr) throws IOException {
        CloudActionResponseBean actionBean = null;

        try {
            actionBean = new Gson().fromJson(actionStr, CloudActionResponseBean.class);
            Logger.d(" json parse action: " + actionBean);
        } catch (JsonParseException jsonException) {
            Logger.e(" json exception ! " + jsonException.getMessage());
            ErrorPromoter.getInstance().speakErrorPromote(ErrorPromoter.ERROR_TYPE.DATA_INVALID, null);
            jsonException.printStackTrace();
        }

        ActionNode actionNode = ActionNodeHelper.generateActionNode(actionBean);

        Logger.d("actionNode : " + actionNode);
        return actionNode;
    }

    public void parseSendEventResponse(String event, Response response, BaseCloudStateMonitor cloudStateMonitor) {

        SendEvent.SendEventResponse eventResponse = null;

        try {
            eventResponse = SendEvent.SendEventResponse.parseFrom(response.body().source().readByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            cloudStateMonitor.onEventErrorCallback(event, BaseReporter.ReporterResponseCallback.ERROR_CODE.ERROR_PARSE_EXCEPTION);
            return;
        }

        if (eventResponse == null) {
            Logger.d(" eventResponse is null");
            cloudStateMonitor.onEventErrorCallback(event, BaseReporter.ReporterResponseCallback.ERROR_CODE.ERROR_RESPONSE_NULL);
            return;
        }

        Logger.d(" eventResponse.response : " + eventResponse.getResponse());

        if (eventResponse.getResponse() == null) {
            Logger.d("eventResponse is null !");
            cloudStateMonitor.onEventErrorCallback(event, BaseReporter.ReporterResponseCallback.ERROR_CODE.ERROR_RESPONSE_NULL);
            return;
        }

        String actionStr = eventResponse.getResponse();

        CloudActionResponseBean actionBean = null;
        try {
            actionBean = new Gson().fromJson(actionStr, CloudActionResponseBean.class);
        } catch (JsonParseException jsonException) {
            Logger.e(" json exception ! " + jsonException.getMessage());
            jsonException.printStackTrace();
        }

        ActionNode actionNode = ActionNodeHelper.generateActionNode(actionBean);

        //update appState
        cloudStateMonitor.onNewEventActionNode(actionNode);

    }

}
