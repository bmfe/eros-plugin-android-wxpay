package com.eros.erospluginwxpay;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by liuyuanxiao on 2018/4/17.
 */

public class WXApiModule {
    private static WXApiModule instans;
    private IWXAPI mWXApi;
    private String appId;
    private WXApiModule() {
    }

    public static WXApiModule getInstans() {
        if (instans == null) {
            synchronized (WXApiModule.class) {
                if (instans == null) {
                    instans = new WXApiModule();
                }
            }
        }
        return instans;
    }

    public void onCreateWXApi(Context context, String appId) {
        if (!TextUtils.isEmpty(appId)) {
            this.appId = appId;
            mWXApi = WXAPIFactory.createWXAPI(context, appId, true);
        }
    }

    public IWXAPI getWXApi() {
        return mWXApi;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
