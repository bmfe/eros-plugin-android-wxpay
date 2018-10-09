package com.eros.erospluginwxpay;

import android.content.Context;
import android.text.TextUtils;

import com.eros.erospluginwxpay.model.WeChatPayModel;
import com.eros.erospluginwxpay.model.WeChatPayResultModel;
import com.eros.framework.manager.ManagerFactory;
import com.eros.framework.manager.impl.ParseManager;
import com.eros.framework.manager.impl.dispatcher.DispatchEventManager;
import com.eros.framework.model.WeexEventBean;
import com.eros.framework.utils.JsPoster;
import com.eros.wxbase.EventGate;
import com.squareup.otto.Subscribe;
import com.taobao.weex.bridge.JSCallback;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Carry on 2017/9/26.
 */

public class EventPay extends EventGate {
    private JSCallback mCallback;

    @Override
    public void perform(Context context, WeexEventBean weexEventBean) {
        String params = weexEventBean.getJsParams();
        if (TextUtils.isEmpty(params)) return;
        pay(params, context, weexEventBean.getJscallback());
    }

    public void pay(String params, Context context, JSCallback callback) {
        this.mCallback = callback;
        ParseManager parseManager = ManagerFactory.getManagerService(ParseManager.class);
        WeChatPayModel weChatPayModal = parseManager.parseObject(params, WeChatPayModel.class);
        IWXAPI wxapi = WXAPIFactory.createWXAPI(context, weChatPayModal.getAppid(), true);
        if (!wxapi.isWXAppInstalled()) {
            JsPoster.postFailed("请先安装微信客户端", mCallback);
//            BaseResultBean result = new BaseResultBean();
//            result.setResCode(9);
//            result.setMsg("请先安装微信客户端");
//            if (mCallback != null) {
//                mCallback.invokeAndKeepAlive(result);
//            }
            return;
        }
        PayReq request = new PayReq();
        request.appId = weChatPayModal.getAppid();
        request.partnerId = weChatPayModal.getPartnerid();
        request.nonceStr = weChatPayModal.getNoncestr();
        request.packageValue = weChatPayModal.getPackageValue();
        request.prepayId = weChatPayModal.getPrepayid();
        request.timeStamp = weChatPayModal.getTimestamp();
        request.sign = weChatPayModal.getSign();
        wxapi.registerApp(weChatPayModal.getAppid());
        ManagerFactory.getManagerService(DispatchEventManager.class).getBus().register(this);
        wxapi.sendReq(request);
    }

    @Subscribe
    public void onPayResult(WeChatPayResultModel result) {
        result.errorMsg = result.msg;
        if (mCallback != null) {
            mCallback.invoke(result);
        }
        ManagerFactory.getManagerService(DispatchEventManager.class).getBus().unregister(this);
    }

}
