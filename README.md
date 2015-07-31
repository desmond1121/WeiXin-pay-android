# WeiXin-pay-android
微信官方的API太难用了，为微信NATIVE支付提供了简单实现，目前可通过简单的代码实现统一下单、生成二维码、查询订单、关闭订单。

a simple generation api of WeiXin pay on android.
more details, see: [WeiXin Pay API](https://pay.weixin.qq.com/wiki/doc/api/index.html)

#Dependencies:
QRGen, link:[https://github.com/kenglxn/QRGen](https://github.com/kenglxn/QRGen)

OkHttp, link:[https://github.com/kenglxn/QRGen](https://github.com/square/okhttp)



#only implements posting NATIVE UnifyOrder now.

#Post a UnifiedOrder

        UnifyOrderParam param = new UnifyOrderParam(APPID, MCHID)
                .initOrder("description", total_Fee, "http://notify_url", "NATIVE")
                .setOrderGenerator(new GenOrderRule() {
                                    @Override
                                    public String generateOrderNumber() {
                                        return "123456789";
                                    }
                                }).build(APIKEY);

        UnifyOrder order = new UnifyOrder(param);
        /* post a unified order */
        order.postOrder(callback);

        HttpCallBack callback = new HttpCallBack(){

            @Override
            void onSuccess(BaseTask.From msgFrom, WxResult result){
                switch(msgFrom){
                    case POST:
                        /* get QR Bitmap (can also get uri and file) */
                        imageView.setImageBitmap(result.getPayQRBitmap(500, 500));
                        /* query order status */
                        order.queryOrder(1000, 60, this);
                        break;

                    case QUERY:
                        /* close order */
                        order.closeOrder(this);
                        break;
                }
            }

            @Override
            void onFail(BaseTask.From msgFrom, String err){
                switch(msgFrom){
                    handle error.
                }
            }
        }

