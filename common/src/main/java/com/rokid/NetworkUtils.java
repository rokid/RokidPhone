package com.rokid;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

/**
 * Created by fanfeng on 2017/9/20.
 */

public class NetworkUtils {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isNetworkConnect(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);

        if (connectivityManager.isActiveNetworkMetered()){
            return true;
        }

        return false;
    }

}
