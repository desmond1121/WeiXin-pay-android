package com.jiayiyao.wxpay.core.net;


public interface HttpCallBack {

    void onSuccess(BaseTask.From msgFrom, WxResult result);

    void onFail(BaseTask.From msgFrom, String err);
}
