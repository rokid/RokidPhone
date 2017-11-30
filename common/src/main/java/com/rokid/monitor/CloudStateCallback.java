package com.rokid.monitor;

import com.rokid.bean.ActionNode;

/**
 * Created by fanfeng on 2017/6/14.
 */

public interface CloudStateCallback {

    void onNewIntentActionNode(ActionNode actionNode);

    void onNewEventActionNode(ActionNode actionNode);

    void onCreate();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
