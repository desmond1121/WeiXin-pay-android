package com.jiayiyao.wxpay.core;

import android.content.Context;
import android.os.AsyncTask;

import com.jiayiyao.wxpay.core.net.CloseTask;
import com.jiayiyao.wxpay.core.net.HttpCallBack;
import com.jiayiyao.wxpay.core.net.PostTask;
import com.jiayiyao.wxpay.core.net.QueryTask;
import com.jiayiyao.wxpay.core.params.UnifyOrderParam;
import com.jiayiyao.wxpay.core.utils.Signer;
import com.tencent.mm.sdk.modelpay.PayReq;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Jiayi Yao on 2015/7/29.
 */
public class UnifyOrder {
    private static final String TAG = "UnifyOrder";
    private Random random = new Random();
    private UnifyOrderParam mParam;
    private String orderNumber;
    private Context mContext;

    private QueryTask queryTask = null;
    private PostTask postTask = null;
    private CloseTask closeTask = null;

    /**
     * For NATIVE Order to use.
     * @param param
     */
    public UnifyOrder(UnifyOrderParam param){
        this(null, param);
    }

    /**
     * For APP Order to use.
     * @param context
     * @param param
     */
    public UnifyOrder(Context context, UnifyOrderParam param){
        this.mParam = param;
        this.mContext = context;
        if(postTask != null) postTask.finish();
        if(queryTask != null) queryTask.finish();
        if(closeTask != null) closeTask.finish();
    }

    /**
     * post a NATIVE order.
     * If you post a NATIVE order, every thing is ok.
     * If you post a APP order, you should read {@link "http://pay.weixin.qq.com/wiki/doc/api/app.php?chapter=8_5"}.
     * @param callBack
     */
    public void postOrder(HttpCallBack callBack){
        postTask = new PostTask(mParam, callBack);
        postTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * For APP order use.
     * see {@link "http://pay.weixin.qq.com/wiki/doc/api/app.php?chapter=8_5"}
     * @param prepayId
     * @return
     */
    private PayReq generatePayReq(String prepayId){
        PayReq request = new PayReq();
        request.appId = mParam.getAppId();
        request.partnerId = mParam.getMchId();
        request.prepayId = prepayId;
        request.packageValue = "Sign=WXPay";
        request.nonceStr = randomAlphaInt(8);
        request.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        getReqSign(request);
        return request;
    }

    /**
     * Generate a PayReq signature.
     * see {@link "http://pay.weixin.qq.com/wiki/doc/api/app.php?chapter=8_5"}
     * @param req
     */
    private void getReqSign(PayReq req){
        Map<String, String> map = new HashMap<>();
        map.put("appid", req.appId);
        map.put("partnerid", req.partnerId);
        map.put("package", req.packageValue);
        map.put("noncestr", req.nonceStr);
        map.put("prepayid", req.prepayId);
        map.put("timestamp", req.timeStamp);
        req.sign = Signer.generateSignature(map, mParam.getAPIKey());
    }


    public void queryOrder(int timeInterval, int times, HttpCallBack callback){
        queryTask = new QueryTask(mParam, timeInterval, times, callback);
        queryTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void closeOrder(HttpCallBack callback){
        closeTask = new CloseTask(mParam, callback);
        closeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public String randomAlphaInt(){
        random.setSeed(System.currentTimeMillis());
        int r = random.nextInt(10000)%36;
        if(r<10){
            return String.valueOf(r);
        }else{
            return String.valueOf((char)(55+r));
        }
    }

    public String randomAlphaInt(int length){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<length; i++){
            stringBuilder.append(randomAlphaInt());
        }
        return stringBuilder.toString();
    }
}
