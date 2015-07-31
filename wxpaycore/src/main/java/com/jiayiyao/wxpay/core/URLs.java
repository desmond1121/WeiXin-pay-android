package com.jiayiyao.wxpay.core;

/**
 * 所有请求的URL
 * Created by Jiayi Yao on 2015/7/24.
 */
public class URLs {

    public static final String URL_HEAD = "https://api.mch.weixin.qq.com/";
    public static final String URL_PAY_HEAD = URL_HEAD+"pay/";
    public static final String URL_TOOLS_HEAD = URL_HEAD+"tools/";

    /**
     * 统一下单
     * {@link "https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1"}
     */
    public static final String UNIFIED_ORDER = URL_PAY_HEAD+"unifiedorder";

    /**
     * 查询订单
     * {@link "https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_2"}
     */
    public static final String QUERY = URL_PAY_HEAD+"orderquery ";

    /**
     * 关闭订单
     * {@link "https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_3"}
     */
    public static final String CLOSE_ORDER = URL_PAY_HEAD+"closeorder";

    /**
     * 申请退款
     * {@link "https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_4"}
     */
    public static final String REFUND = URL_PAY_HEAD+"refund";

    /**
     * 查询退款
     * {@link "https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_5"}
     */
    public static final String REFUND_QUERY = URL_PAY_HEAD+"refundquery";

    /**
     * 下载对账单
     * {@link "https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_6"}
     */
    public static final String DOWNLOAD_BILL = URL_PAY_HEAD+"downloadbill";

    /**
     * 转换短链接
     * {@link "https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_9"}
     */
    public static final String SHORT_URL = URL_TOOLS_HEAD+"shorturl";
}
