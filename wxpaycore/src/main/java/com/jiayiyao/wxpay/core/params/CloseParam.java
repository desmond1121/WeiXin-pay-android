package com.jiayiyao.wxpay.core.params;

/**
 * Created by Jiayi Yao on 2015/7/31.
 */
public class CloseParam extends BaseParam{
    private static final String TAG = "CloseParam";

    public CloseParam(UnifyOrderParam param) {
        super(param.getAppId(), param.getMchId());
        paramMap.put("out_trade_no", param.mTradeNo);
        setAPIKey(param.getAPIKey());
    }
}
