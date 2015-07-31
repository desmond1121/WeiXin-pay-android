package com.jiayiyao.wxpay.core.params;

import android.util.Log;

import com.jiayiyao.wxpay.core.utils.Signer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 封装HTTP请求的参数, 只包含appId, mchId, 封装了签名生成和随机字符串生成函数。
 * Created by Jiayi Yao on 2015/7/24.
 */
public class BaseParam {
    private static final String TAG = "BaseParam";

    /* 固有的参数 */
    protected static final String PARAM_APPID = "appid";
    protected static final String PARAM_MCHID = "mch_id";
    protected static final String PARAM_SIGN = "sign";
    protected static final String PARAM_NONCE = "nonce_str";

    protected Map<String, String> paramMap = new HashMap<>();
    protected Random random = new Random();

    private String mAppId;
    private String mMchId;
    private String mSign;
    private StringBuilder mNonceStr;
    private String mAPIKey;

    protected boolean isPosted = false;

    /**
     * 初始化appid 和 mchId
     * @param appId 微信分配的公众账号ID（企业号corpid即为此appId）
     * @param mchId 微信支付分配的商户号
     */
    BaseParam(String appId, String mchId){
        mNonceStr = new StringBuilder();
        paramMap.put(PARAM_APPID, appId);
        paramMap.put(PARAM_MCHID, mchId);
        mAppId = appId;
        mMchId = mchId;
    }

    /**
     * 生成XMlString
     */
    public String generatePOSTXML(){
        generateNonceStr();
        generateSignature();

        StringBuilder xmlStr = new StringBuilder("<xml>");
        for(Map.Entry<String, String> entry:paramMap.entrySet()){
            xmlStr.append("<");
            xmlStr.append(entry.getKey());
            xmlStr.append(">");
            xmlStr.append(entry.getValue());
            xmlStr.append("</");
            xmlStr.append(entry.getKey());
            xmlStr.append(">");
        }
        xmlStr.append("</xml>");

        Log.i(TAG, "generatePOSTXML()--Info-- " + xmlStr);
        return xmlStr.toString();
    }

    /**
     * 生成随机数
     */
    public void generateNonceStr(){
        try {
            mNonceStr.delete(0, mNonceStr.length());
        } catch (Exception e) {}

        mNonceStr.append(randomAlphaInt(32));
        paramMap.put(PARAM_NONCE, mNonceStr.toString());
    }

    /**
     * 生成签名
     */
    public void generateSignature(){
        mSign = Signer.generateSignature(paramMap, mAPIKey);
        paramMap.put(PARAM_SIGN, mSign);
    }

    /**
     * 重置参数
     * @return
     */
    public void reset(){
        mSign = null;
    }

    public String getAppId() {
        return mAppId;
    }


    public String getMchId() {
        return mMchId;
    }

    public StringBuilder getNonceStr() {
        return mNonceStr;
    }

    public String getSign() {
        return mSign;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setAPIKey(String apiKey){
        mAPIKey = apiKey;
    }

    public String getAPIKey() {
        return mAPIKey;
    }

    public void setIsPosted(boolean isPosted) {
        this.isPosted = isPosted;
    }

    /**
     * You can put else params in your url package here.
     * {@link "https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1"}
     * @param key 参数名
     * @param value 参数值
     */
    public void putExtraParam(String key, String value){
        paramMap.put(key, value);
    }

    /**
     * 随机生成数字或字母
     * @return
     */

    public String randomAlphaInt(){
        int r = random.nextInt(36);
        if(r<10){
            return String.valueOf(r);
        }else{
            return String.valueOf((char)(55+r));
        }
    }

    public String randomAlphaInt(int length){
        random.setSeed(System.currentTimeMillis());
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<length; i++){
            stringBuilder.append(randomAlphaInt());
        }
        return stringBuilder.toString();
    }
}

