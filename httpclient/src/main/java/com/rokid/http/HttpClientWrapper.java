package com.rokid.http;

import com.rokid.proto.SendEvent;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

//import com.android.okhttp.*;


/**
 * Created by fanfeng on 2017/5/11.
 */
public class HttpClientWrapper {

    private static OkHttpClient okHttpClient;
    private static final int CONNECTION_TIME_OUT = 3;
    private static final int READ_TIME_OUT = 3;
    private static final int WRITE_TIME_OUT = 3;

    private static final String CONTENT_TYPE = "application/octet-stream";
    private Response response;

    public HttpClientWrapper() {
        okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(READ_TIME_OUT, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS);

    }

    public static HttpClientWrapper getInstance() {
        return SingleHolder.instance;
    }

    public Response sendRequest(String url, SendEvent.SendEventRequest eventRequest) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        eventRequest.writeTo(byteArrayOutputStream);
        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "text/plain")
                .addHeader("Accept-Charset", "utf-8")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Authorization", BaseUrlConfig.getAuthorization())
                .post(RequestBody.create(MediaType.parse(CONTENT_TYPE)
                        , byteArrayOutputStream.toByteArray()))
                .build();
         response = okHttpClient.newCall(request).execute();

        return response;
    }

    public void close(){
        if (response != null && response.body() != null){
            try {
                response.body().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static class SingleHolder {
        private static final HttpClientWrapper instance = new HttpClientWrapper();
    }

}

