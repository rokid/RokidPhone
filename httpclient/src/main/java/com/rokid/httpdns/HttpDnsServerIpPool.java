package com.rokid.httpdns;

import android.text.TextUtils;

import java.util.Random;

/**
 * Created by showingcp on 3/28/17.
 */

public class HttpDnsServerIpPool {
    private static final String[] ipPool = {"203.107.1.65", "203.107.1.34", "203.107.1.1"};
    public static String getRandomServerIp() {
        int index = new Random().nextInt(ipPool.length);

        String ip = "203.107.1.34";

        try {
            ip = ipPool[index];
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(ip)) {
            ip = "203.107.1.34";
        }

        return ip;
    }
}
