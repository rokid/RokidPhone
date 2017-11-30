package com.rokid.md5;

import com.rokid.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Map;

/**
 * Created by fanfeng on 2017/5/12.
 * <p>
 * 签名算法工具类
 */

public class MD5Utils {

    private static final String CHARSET_UTF8 = "UTF-8";

    private static final String SECRET_KEY = "secret";

    public static String generateMD5(Map<String, String> params, String secretValue) {

        String[] keys = params.keySet().toArray(new String[0]);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();

        for (String key : keys) {
            String value = params.get(key);
            query.append(key).append("=").append(value).append("&");
        }

        query.append(SECRET_KEY).append("=").append(secretValue);

        Logger.d("query str " + query.toString());
        // 第三步：使用MD5加密
        byte[] bytes;
        bytes = encryptMD5(query.toString());

        // 第四步：把二进制转化为大写的十六进制
        return byte2hex(bytes);
    }

    public static byte[] encryptMD5(String data) {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytes = md.digest(data.getBytes(CHARSET_UTF8));
        } catch (GeneralSecurityException gse) {
            gse.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        Logger.d("generate sign is " + sign.toString());
        return sign.toString();
    }

}
