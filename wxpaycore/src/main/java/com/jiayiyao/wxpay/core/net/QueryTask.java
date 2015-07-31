package com.jiayiyao.wxpay.core.net;

import android.util.Log;

import com.jiayiyao.wxpay.core.URLs;
import com.jiayiyao.wxpay.core.params.QueryParam;
import com.jiayiyao.wxpay.core.params.UnifyOrderParam;
import com.jiayiyao.wxpay.core.utils.XMLHelper;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * 查询下单
 * {@link "https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_2"}
 * Created by Jiayi Yao on 2015/7/31.
 */
public class QueryTask extends BaseTask{
    private static final String TAG = "QueryTask";

    public static final String ERR_INTERRUPT = "Interrupt error";
    private int timeInterval;
    private int queryTimes;

    public QueryTask(UnifyOrderParam param, int timeInterval, int queryTimes, HttpCallBack callBack) {
        super(From.QUERY);
        if(queryTimes == 0) this.queryTimes = 1;
        else this.queryTimes = queryTimes;

        if(timeInterval < 100) this.timeInterval = 100;
        else this.timeInterval = timeInterval/100*100;

        mParam = new QueryParam(param);
        mURL = URLs.QUERY;
        mHttpCallBack = callBack;
    }

    @Override
    protected String doInBackground(Object... params) {
        if(params.length>0)return ERR_PARAM;

        state = State.RUNNING;

        Request query = initPost(mParam.generatePOSTXML());
        try {
            for(int i=0; i<queryTimes; i++){
                xmlResult = post(query);
                wxResult = XMLHelper.parseToWxResult(xmlResult);
                if(!wxResult.commSuccess())return wxResult.getCommErrMsg();
                if(!wxResult.returnSuccess())return wxResult.getReturnErrMsg();
                if(wxResult.tradeSuccess())return SUCCESS;
                Thread.sleep(timeInterval);
            }
            return FAIL;
        } catch (SocketTimeoutException ste) {
            return ERR_CONN_TIMEOUT;
        } catch (UnknownHostException uhe){
            return ERR_UNKNOWN_HOST;
        } catch (IOException e) {
            return ERR_IO;
        } catch (InterruptedException e) {
            return ERR_INTERRUPT;
        } finally {
            httpClient = null;
            typeXML = null;
        }
    }
}
