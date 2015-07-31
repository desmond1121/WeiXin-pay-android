package com.jiayiyao.wxpay.core.params;


import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 统一下单参数
 * Created by Jiayi Yao on 2015/7/25.
 */
public class UnifyOrderParam extends BaseParam{
    private static final String TAG = "UnifyOrderParam";

    public static final String PARAM_BODY = "body";
    public static final String PARAM_TRADE_NO = "out_trade_no";
    public static final String PARAM_TOTAL_FEE = "total_fee";
    public static final String PARAM_IP = "spbill_create_ip";
    public static final String PARAM_TRADE_TYPE = "trade_type";
    public static final String PARAM_NOTIFY_URL = "notify_url";

    /* 非必须参数 */
    public static final String PARAM_DEVICE_INFO = "device_info";
    public static final String PARAM_DETAIL = "detail";
    public static final String PARAM_ATTACH = "attach";
    public static final String PARAM_FEE_TYPE = "fee_type";
    public static final String PARAM_TIME_START = "time_start";
    public static final String PARAM_TIME_EXPIRE = "time_expire";
    public static final String PARAM_GOODS_TAG = "goods_tag";
    public static final String PARAM_PRODUCT_ID = "product_id";
    public static final String PARAM_LIMIT_PAY = "limit_pay";
    public static final String PARAM_OPENID = "openid";

    public String mDescription;
    public int mTotalFee;
    public String mIp;
    public String mTradeType;
    public String mNotifyUrl;
    public String mTradeNo = null;

    private boolean isInit = false;
    private GenOrderRule mRule = null;

    public UnifyOrderParam(String mAppId, String mMchId) {
        super(mAppId, mMchId);
    }

    /**
     * 初始化[统一下单]必须传递的参数
     * 详见{@link "https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1"}.
     *
     * @param description 商品或支付单简要描述
     * @param totalFee 订单总金额,以分为度量
     * @param notifyUrl 接收微信支付异步通知回调地址
     * @param tradeType 取值如下：JSAPI，NATIVE，APP，WAP
     */
    public UnifyOrderParam initOrder(String description, int totalFee,
                                     String notifyUrl, String tradeType){
        return initOrder(description, totalFee, notifyUrl, tradeType, null);
    }

    /**
     * Initiate UNIFY ORDER
     * {@link "https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1"}.
     *
     * @param description 商品或支付单简要描述
     * @param totalFee 订单总金额,以分为度量
     * @param notifyUrl 接收微信支付异步通知回调地址
     * @param tradeType 取值如下：JSAPI，NATIVE，APP，WAP
     * @param productId 可为NULL
     */
    public UnifyOrderParam initOrder(String description, int totalFee,
                                     String notifyUrl, String tradeType, String productId){
        mDescription = description;
        mTotalFee = totalFee;
        mIp = "192.168.0.0";
        new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        mIp = InetAddress.getLocalHost().getHostAddress();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                }
            }
        }).start();

        mTradeType = tradeType;
        mNotifyUrl = notifyUrl;

        paramMap.put(PARAM_BODY, description);
        paramMap.put(PARAM_TOTAL_FEE, String.valueOf(totalFee));
        paramMap.put(PARAM_IP, mIp);
        paramMap.put(PARAM_NOTIFY_URL, notifyUrl);
        paramMap.put(PARAM_TRADE_TYPE, tradeType);

        if(productId == null)paramMap.put(PARAM_PRODUCT_ID, "0");
        else paramMap.put(PARAM_PRODUCT_ID, productId);

        return this;
    }

    public UnifyOrderParam build(String apiKey){
        setAPIKey(apiKey);
        generateOrder(mRule);
        return this;
    }

    public UnifyOrderParam setOrderGenerator(GenOrderRule rule){
        mRule = rule;
        return this;
    }

    public void generateOrder(GenOrderRule rule){
        if(rule != null){
            mTradeNo = rule.generateOrderNumber();
        }

        if(rule == null || mTradeNo == null){
            SimpleDateFormat format = new SimpleDateFormat("yyyymmddhhmmss");
            mTradeNo = format.format(new Date());
        }

        isInit = true;
        paramMap.put(PARAM_TRADE_NO, mTradeNo);
    }

    /**
     * You can put else params in your url package here.
     * {@link "https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1"}
     * @param key param name
     * @param value param value
     */
    @Override
    public void putExtraParam(String key, String value){
        if(isInit){
            Log.e(TAG, "UnifyOrderParam.putExtraParam()--Error-- You cannot put param after initiation.");
            return;
        }
        if(isPosted){
            Log.e(TAG, "UnifyOrderParam.putParam()--Error-- You cannot put param after the package is posted.");
            return;
        }
        paramMap.put(key, value);
    }

    public boolean isInit(){
        return isInit;
    }
}
