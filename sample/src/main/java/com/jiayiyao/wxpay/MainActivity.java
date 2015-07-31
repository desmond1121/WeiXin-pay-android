package com.jiayiyao.wxpay;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jiayiyao.wxpay.core.UnifyOrder;
import com.jiayiyao.wxpay.core.net.*;
import com.jiayiyao.wxpay.core.net.BaseTask.From;
import com.jiayiyao.wxpay.core.params.GenOrderRule;
import com.jiayiyao.wxpay.core.params.UnifyOrderParam;


public class MainActivity extends Activity implements HttpCallBack{
    private static final String TAG = "MainActivity";
    //appid
    public static final String APP_ID = "appid";

    //商户号
    public static final String MCH_ID = "mchid";

    //API密钥，在商户平台设置
    public static final  String API_KEY="appkey";

//    private SimpleDraweeView draweeView = null;
    private UnifyOrder order = null;
    private ImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fresco.initialize(MainActivity.this);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.qr_view);

//        draweeView = (SimpleDraweeView) findViewById(R.id.drawee_view);
        UnifyOrderParam param = new UnifyOrderParam(APP_ID, MCH_ID)
                .initOrder("body", 1, "http://notify_url", "NATIVE")
                .setOrderGenerator(new GenOrderRule() {
                    @Override
                    public String generateOrderNumber() {
                        return null;
                    }
                }).build(API_KEY);

        order = new UnifyOrder(param);
        order.postOrder(this);

    }

    @Override
    public void onSuccess(From msgFrom, WxResult result) {
        switch (msgFrom){
            case POST:
                imageView.setImageBitmap(result.getPayQRBitmap(500, 500));
                order.queryOrder(1000, 60, this);
                break;
            case QUERY:
                Log.i(TAG, "MainActivity.onSuccess()--info-- " + result.getValue("trade_state"));
                order.closeOrder(this);
                break;
        }
    }

    @Override
    public void onFail(From msgFrom, String err) {
        Log.e(TAG, "MainActivity.onFail()--Error-- from " + msgFrom + " " + err);
        if(msgFrom == From.CLOSE && err.equals("NULL")){
            Log.e(TAG, "MainActivity.onFail()--Error-- close from payed order");
        }
    }
}
