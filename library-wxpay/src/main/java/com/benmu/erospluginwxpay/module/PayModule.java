package com.benmu.erospluginwxpay.module;

import com.alibaba.weex.plugin.annotation.WeexModule;
import com.benmu.erospluginwxpay.WXApiModule;
import com.benmu.framework.constant.WXEventCenter;
import com.benmu.framework.manager.ManagerFactory;
import com.benmu.framework.manager.impl.dispatcher.DispatchEventManager;
import com.benmu.framework.model.WeexEventBean;
import com.benmu.widget.utils.BaseCommonUtil;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

/**
 * Created by Carry on 17/2/8.
 */
@WeexModule(name = "bmWXPay", lazyLoad = true)
public class PayModule extends WXModule {

    private JSCallback mCallback;

    @JSMethod(uiThread = true)
    public void pay(String params, JSCallback callback) {
        WeexEventBean eventBean = new WeexEventBean();
        eventBean.setContext(mWXSDKInstance.getContext());
        eventBean.setKey(WXEventCenter.EVENT_PAYBYWECHAT);
        eventBean.setJsParams(params);
        eventBean.setJscallback(callback);
        ManagerFactory.getManagerService(DispatchEventManager.class).getBus().post(eventBean);
    }

    @JSMethod(uiThread = true)
    public void initWX(String appid) {
        WXApiModule.getInstans().onCreateWXApi(mWXSDKInstance.getContext(), appid);
    }

    /**
     * 获取是否安装WeChat
     */
    @JSMethod(uiThread = false)
    public Object isInstallWXApp() {
        return BaseCommonUtil.isWeChatInstall(mWXSDKInstance.getContext());

    }
}

