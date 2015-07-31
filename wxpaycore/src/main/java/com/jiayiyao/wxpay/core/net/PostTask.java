package com.jiayiyao.wxpay.core.net;

import android.util.Log;

import com.jiayiyao.wxpay.core.URLs;
import com.jiayiyao.wxpay.core.params.BaseParam;
import com.jiayiyao.wxpay.core.utils.XMLHelper;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 *
 * Created by Jiayi Yao on 2015/7/24.
 */
public class PostTask extends BaseTask{
    private static final String TAG = "PostTask";

    /**
     * Init a Http task
     * @param param params input.
     * @param httpCallBack callback when http connect success.
     */
    public PostTask(BaseParam param, HttpCallBack httpCallBack) {
        super(From.POST);
        this.mURL = URLs.UNIFIED_ORDER;
        this.mParam = param;
        this.mHttpCallBack = httpCallBack;
    }

    @Override
    protected String doInBackground(Object... xmlString) {
        if(mParam == null)return ERR_PARAM;
        state = State.RUNNING;
        try {
            xmlResult = post(mParam.generatePOSTXML());
            wxResult = XMLHelper.parseToWxResult(xmlResult);
            if(!wxResult.commSuccess())return wxResult.getCommErrMsg();
            if(!wxResult.returnSuccess())return wxResult.getReturnErrMsg();
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

    @Override
    protected void onCancelled() {
        super.onCancelled();
        state = State.FINISH;
    }
}

