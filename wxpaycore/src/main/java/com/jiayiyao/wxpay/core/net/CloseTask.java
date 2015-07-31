package com.jiayiyao.wxpay.core.net;

import com.jiayiyao.wxpay.core.URLs;
import com.jiayiyao.wxpay.core.params.CloseParam;
import com.jiayiyao.wxpay.core.params.UnifyOrderParam;
import com.jiayiyao.wxpay.core.utils.XMLHelper;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by Jiayi Yao on 2015/7/31.
 */
public class CloseTask extends BaseTask{
    private static final String TAG = "CloseTask";
    public static final String ERR_CLOSE = "Error in close";

    public CloseTask(UnifyOrderParam unifyOrderParam, HttpCallBack callBack){
        super(From.CLOSE);
        mParam = new CloseParam(unifyOrderParam);
        mURL = URLs.CLOSE_ORDER;
        mHttpCallBack = callBack;
    }

    @Override
    protected String doInBackground(Object... params) {
        if(params.length>0)return ERR_PARAM;

        state = State.RUNNING;

        try {
            xmlResult = post(mParam.generatePOSTXML());
            wxResult = XMLHelper.parseToWxResult(xmlResult);
            if(!wxResult.commSuccess())return wxResult.getCommErrMsg();
            if(!wxResult.returnSuccess())return wxResult.getReturnErrMsg();
            if(!wxResult.getValue("err_code").equals("NULL"))return wxResult.getValue("err_code_des");
            return SUCCESS;
        } catch (SocketTimeoutException ste) {
            return ERR_CONN_TIMEOUT;
        } catch (UnknownHostException uhe){
            return ERR_UNKNOWN_HOST;
        } catch (IOException e) {
            return ERR_IO;
        } finally {
            httpClient = null;
            typeXML = null;
        }
    }
}
