package com.jiayiyao.wxpay.core.params;

/**
 * Created by Jiayi Yao on 2015/7/29.
 */
public class QueryParam extends BaseParam{
    private static final String TAG = "QueryParam";

    private String mTradeNo = null;
    public QueryParam(UnifyOrderParam param){
        super(param.getAppId(), param.getMchId());
        mTradeNo = param.mTradeNo;
        paramMap.put("out_trade_no", mTradeNo);
        setAPIKey(param.getAPIKey());
    }
}
