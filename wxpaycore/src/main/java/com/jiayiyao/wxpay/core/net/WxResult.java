package com.jiayiyao.wxpay.core.net;

import android.graphics.Bitmap;
import android.net.Uri;

import com.jiayiyao.wxpay.core.utils.XMLHelper;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import org.w3c.dom.Element;

import java.io.File;

/**
 * Wechat Result.
 * {@link "https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1"}
 * Created by Jiayi Yao on 2015/7/29.
 */
public class WxResult {
    private static final String TAG = "WxResult";
    /**
     * 支付状态
     */
    public enum PayState{SUCCESS, NOTPAY, REFUND, CLOSED, REVOKED, UNDERPAYING, OTHER}

    public String errMsg = null;

    /* return when resultCode and returnCode both equals Success*/
    private Element xml;
    public WxResult(){

    }

    public WxResult(String errMsg){
        this.errMsg = errMsg;
    }

    public boolean commSuccess(){
        return getValue("return_code").equals("SUCCESS");
    }

    public String getCommErrMsg(){
        return getValue("return_msg");
    }

    public boolean returnSuccess(){
        return getValue("result_code").equals("SUCCESS");
    }

    public String getReturnErrMsg(){
        return getValue("result_err_des");
    }

    public boolean success(){
        return commSuccess() && returnSuccess();
    }

    public void setContent(Element element){
        xml = element;
    }

    /**
     * You can get any value in return xml {@link "https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1"}
     * @param tagName tag name of xml
     * @return value of xml element.
     */
    public String getValue(String tagName){
        return XMLHelper.getValue(xml, tagName);
    }

    @Override
    public String toString() {
        return XMLHelper.string(xml);
    }

    /* Get QR Code of prepay url (for scan).*/
    public Uri getPayQRPicUri(){
        return Uri.fromFile(getPayQRPicFile());
    }

    public Bitmap getPayQRBitmap(){
        return ((QRCode) QRCode.from(getValue("code_url"))).bitmap();
    }

    public Bitmap getPayQRBitmap(int width, int height){
        return ((QRCode) QRCode.from(getValue("code_url")).withSize(width, height)).bitmap();
    }

    public File getPayQRPicFile(){
        return QRCode.from(getValue("code_url")).file();
    }

    public Uri getPayQRPicUri(int width, int height){
        return Uri.fromFile(getPayQRPicFile(width, height));
    }

    public File getPayQRPicFile(int width, int height){
        return QRCode.from(getValue("code_url")).withSize(width, height).file();
    }


    /* Get prepay ID */
    public String getPrepayId(){
        return getValue("prepay_id");
    }

    /* For query */
    public boolean tradeSuccess(){
        return getValue("trade_state").equals("SUCCESS");
    }

    public PayState getPayState(){
        switch(getValue("trade_staet")){
            case "SUCCESS":return PayState.SUCCESS;
            case "REFUND":return PayState.REFUND;
            case "NOTPAY":return PayState.NOTPAY;
            case "CLOSED":return PayState.CLOSED;
            case "REVOKED":return PayState.REVOKED;
            case "UNDERPAYING":return PayState.UNDERPAYING;
            case "PAYERROR":return PayState.OTHER;
            default:return PayState.NOTPAY;
        }
    }
}
