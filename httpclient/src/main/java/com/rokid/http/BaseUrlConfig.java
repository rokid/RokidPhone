package com.rokid.http;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.rokid.bean.DeviceConfig;
import com.rokid.logger.Logger;
import com.rokid.md5.MD5Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by fanfeng on 2017/5/11.
 */
public class BaseUrlConfig {

    private static final String BASE_HTTP = "https://";

    private static final String DEFAULT_HOST = "apigwrest.open.rokid.com";

    private static final String SEND_EVENT_PATH = "/v1/skill/dispatch/sendEvent";

    private static String mHost;

    private static DeviceConfig deviceConfig;

    private static final String KEY_HOST = "event_req_host";

    private static final String PARAM_KEY_KEY = "key";
    private static final String PARAM_KEY_DEVICE_TYPE_ID = "device_type_id";
    private static final String PARAM_KEY_DEVICE_ID = "device_id";
    private static final String PARAM_KEY_SERVICE = "service";
    private static final String PARAM_VALUE_SERVICE = "rest";
    private static final String PARAM_KEY_VERSION = "version";
    private static final String PARAM_KEY_TIME = "time";
    private static final String PARAM_KEY_SIGN = "sign";
    private static final String PARAM_KEY_SECRET = "secret";
    private static Map<String, String> params;


    private static void putUnEmptyParam(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            Logger.d("param invalidate ! key " + key + " value : " + value);
            return;
        }
        params.put(key, value);
    }

    public static void initEventParam(WeakReference<Context> contextWeakReference){
        AssetManager assetManager = contextWeakReference.get().getAssets();

        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("openvoice_profile.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String deviceConfigStr = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            String readLine;
            StringBuilder sb = new StringBuilder();
            while ((readLine = reader.readLine()) != null) {
                sb.append(readLine);
            }
            deviceConfigStr = sb.toString();
            Logger.d(" configStr : " + sb.toString());
            reader.close();
        } catch (FileNotFoundException e) {
            Logger.e(" openvoice_profile.json not found !");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(deviceConfigStr)){
            Logger.d(" deviceConfig is null !");
            return;
        }

        deviceConfig = new Gson().fromJson(deviceConfigStr,DeviceConfig.class);

        params = new LinkedHashMap<>();

        mHost = deviceConfig.getEvent_req_host();

        putUnEmptyParam(PARAM_KEY_KEY, deviceConfig.getKey());
        putUnEmptyParam(PARAM_KEY_DEVICE_TYPE_ID, deviceConfig.getDevice_type_id());
        putUnEmptyParam(PARAM_KEY_DEVICE_ID, deviceConfig.getDevice_id());

        putUnEmptyParam(PARAM_KEY_SERVICE, PARAM_VALUE_SERVICE);
        putUnEmptyParam(PARAM_KEY_VERSION, deviceConfig.getApi_version());
        putUnEmptyParam(PARAM_KEY_TIME, String.valueOf(System.currentTimeMillis()));
        putUnEmptyParam(PARAM_KEY_SIGN, MD5Utils.generateMD5(params, deviceConfig.getSecret()));

    }

    public static String getUrl() {

        if (mHost == null || mHost.isEmpty()) {
            mHost = DEFAULT_HOST;
        }

        return BASE_HTTP + mHost + SEND_EVENT_PATH;
    }

    public static String getAuthorization() {

        if (params.isEmpty()) {
            Logger.d("param is null !");
            return null;
        }

        String authorization = params.toString()
                .replace("{", "").replace("}", "").replace(",", ";").replace(" ", "");

        Logger.d(" authorization: " + authorization);
        return authorization;
    }


}
