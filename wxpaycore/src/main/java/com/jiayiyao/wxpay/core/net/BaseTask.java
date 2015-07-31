package com.jiayiyao.wxpay.core.net;

import android.os.AsyncTask;
import android.util.Log;

import com.jiayiyao.wxpay.core.params.BaseParam;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Jiayi Yao on 2015/7/31.
 */
public class BaseTask extends AsyncTask<Object, Object, String>{
    private static final String TAG = "BaseTask";

    public static final String FROM_POST = "POST";
    public static final String FROM_QUERY = "QUERY";
    public static final String FROM_CLOSE = "CLOSE";

    public enum From{POST, QUERY, CLOSE}
    public enum State{READY, RUNNING, FINISH}

    public static final String ERR_PARAM = "Input param error";
    public static final String ERR_COMM = "Communication error";
    public static final String ERR_UNKNOWN_HOST = "Host error";
    public static final String ERR_CONN_TIMEOUT = "Post connection time out";
    public static final String ERR_IO = "IO error";
    public static final String SUCCESS = "Success";
    public static final String FAIL = "Fail";

    protected State state = State.READY;
    protected OkHttpClient httpClient = new OkHttpClient();
    protected MediaType typeXML = MediaType.parse("application/xml; charset=utf-8");
    protected String mURL = null;
    protected HttpCallBack mHttpCallBack = null;
    protected BaseParam mParam = null;
    protected String xmlResult = null;
    protected WxResult wxResult = null;
    protected From msgFrom = null;

    public BaseTask(From msgFrom) {
        this.msgFrom = msgFrom;
    }

    @Override
    protected String doInBackground(Object... params) {
        return null;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        state = State.FINISH;

        if(mHttpCallBack == null){
            Log.e(TAG, "Task.onPostExecute()--Error-- NULL CALLBACK");
            return;
        }

        if(!result.equals(SUCCESS)){
            Log.e(TAG, "Task--Error-- " + result);
            mHttpCallBack.onFail(msgFrom, result);
        }else {
            mHttpCallBack.onSuccess(msgFrom, wxResult);
        }
    }

    protected Request initPost(String xmlPost){
        RequestBody body = RequestBody.create(typeXML, xmlPost);
        return new Request.Builder()
                .url(mURL)
                .post(body)
                .build();
    }

    protected String post(String xmlPost) throws IOException {
        mParam.setIsPosted(true);
        Request request = initPost(xmlPost);
        return post(request);
    }

    protected String post(Request request) throws IOException {
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    protected State getState() {
        return state;
    }
}
