package com.jiayiyao.wxpay.core.utils;

import android.util.Log;

import com.jiayiyao.wxpay.core.params.BaseParam;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 使用MD5算法生成签名。
 * Created by Jiayi Yao on 2015/7/24.
 */
public class Signer {
    private static final String TAG = "Signer";

    public static String generateSignature(Map<String, String> paramMap, String apiKey){
        try {
            return md5(getSortedParamStr(paramMap, apiKey));
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "generateSignature()--Error-- No Such Algorithm Exception: MD5");
            return null;
        }
    }

    private static String getSortedParamStr(Map<String, String> paramMap, String apiKey){
        List<String> sortedParams = new ArrayList<>(paramMap.keySet());
        Collections.sort(sortedParams);
        StringBuilder xmlStr = new StringBuilder();
        for(String key:sortedParams){
            xmlStr.append(key);
            xmlStr.append("=");
            xmlStr.append(paramMap.get(key));
            xmlStr.append("&");
        }
        xmlStr.append("key=");
        xmlStr.append(apiKey);
        return xmlStr.toString();
    }

    private static String md5(String forSign) throws NoSuchAlgorithmException {
        Log.i(TAG, "Signer.md5()--info-- " + forSign);
        StringBuilder hex = new StringBuilder();
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] md5bytes = digest.digest(forSign.getBytes(Charset.forName("UTF-8")));
        for (byte md5byte : md5bytes) {
            int val = ((int) md5byte) & 0xff;
            if (val < 16) hex.append("0");
            hex.append(Integer.toHexString(val).toUpperCase());
        }
        return hex.toString();
    }
}
