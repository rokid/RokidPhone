package com.rokid.httpdns;

import android.content.Context;
import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by showingcp on 10/10/16.
 */
public class HTTPDNSHelper {
    private static final String TAG = "RKMusic" + HTTPDNSHelper.class.getSimpleName();

    private Context mContext;

    private static HttpDNS mHttpDnsService;

    public HTTPDNSHelper (Context context) {
        mContext = context;

        mHttpDnsService = HttpDNS.getInstance();
        mHttpDnsService.setExpiredIpAvailable(true);
        HttpDNS.HttpDNSLog.enableLog(true);
    }

    public HostResolveInfo getResolvedHost(String url) {
        HostResolveInfo hostResolveInfo = new HostResolveInfo();
        try {
            URI uri = new URI(url);

            String scheme = uri.getScheme();
            String scheme2 = uri.getSchemeSpecificPart();
            String host = uri.getHost();
            String path = uri.getPath();
            String query = uri.getRawQuery();

            Log.i(TAG, "get scheme part of given url: " + scheme2);
            Log.i(TAG, "get scheme of given url: " + scheme);
            Log.i(TAG, "get host of given url: " + host);
            Log.i(TAG, "get path of given url: " + path);
            Log.i(TAG, "get query of given url: " + query);

            if (mHttpDnsService == null) {
                hostResolveInfo.setSuccess(false);
                hostResolveInfo.setHostResovled(url);
                hostResolveInfo.setOriginHost(url);
                return hostResolveInfo;
            }

            String resolvedHost = mHttpDnsService.getIpByHost(host);

            if (!validIP(resolvedHost)) {
                Log.i(TAG, "host resolved is not a valid host");

                hostResolveInfo.setSuccess(false);
                hostResolveInfo.setHostResovled(url);
                hostResolveInfo.setOriginHost(url);

                return hostResolveInfo;
            }

            String resolvedUrl = scheme + "://" + resolvedHost + path + "?" + query;

            hostResolveInfo.setSuccess(true);
            hostResolveInfo.setOriginHost(url);
            hostResolveInfo.setHostResovled(resolvedUrl);


        } catch (URISyntaxException e) {
            e.printStackTrace();
            hostResolveInfo.setSuccess(false);
            hostResolveInfo.setHostResovled(url);
            hostResolveInfo.setOriginHost(url);
        }

        return hostResolveInfo;
    }

    public static boolean validIP (String ip) {
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

}
