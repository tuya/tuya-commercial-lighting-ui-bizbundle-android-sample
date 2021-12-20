/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2021 Tuya Inc.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.tuya.smart.commercial.lighting.demo.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tuya.commerciallighting.commonbiz.bizbundle.project.api.AbsBizBundleProjectService;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.api.MicroContext;
import com.tuya.smart.api.router.UrlBuilder;
import com.tuya.smart.api.service.RedirectService;
import com.tuya.smart.api.service.RouteEventListener;
import com.tuya.smart.api.service.ServiceEventListener;
import com.tuya.smart.commercial.lighting.demo.pages.login.view.LoginWithUidActivity;
import com.tuya.smart.optimus.sdk.TuyaOptimusSdk;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.sdk.api.INeedLoginListener;
import com.tuya.smart.wrapper.api.TuyaWrapper;


public class TuyaSmartApp extends MultiDexApplication {

    private static final String TAG = "TuyaSmartApp";

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        L.d(TAG, "onCreate " + getProcessName(this));

        init();
    }

    private void init() {
        TuyaSdk.setDebugMode(true);
        TuyaSdk.init(this);

        Fresco.initialize(this);
        TuyaSdk.setOnNeedLoginListener(new INeedLoginListener() {
            @Override
            public void onNeedLogin(Context context) {
                Intent intent = new Intent(context, LoginWithUidActivity.class);
                if (!(context instanceof Activity)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }

                startActivity(intent);
            }
        });

        TuyaWrapper.init(this, new RouteEventListener() {
            @Override
            public void onFaild(int errorCode, UrlBuilder urlBuilder) {
                Log.e("router not implement", urlBuilder.target + urlBuilder.params.toString());
            }
        }, new ServiceEventListener() {
            @Override
            public void onFaild(String serviceName) {
                Log.e("service not implement", serviceName);
            }
        });
        TuyaOptimusSdk.init(this);

        TuyaWrapper.registerService(AbsBizBundleProjectService.class, new BizBundleProjectServiceImpl());
        RedirectService service = MicroContext.getServiceManager().findServiceByInterface(RedirectService.class.getName());
        service.registerUrlInterceptor(new RedirectService.UrlInterceptor() {
            @Override
            public void forUrlBuilder(UrlBuilder urlBuilder, RedirectService.InterceptorCallback interceptorCallback) {
                //Such as:
                //Intercept the event of clicking the panel right menu and jump to the custom page with the parameters of urlBuilder
                if (urlBuilder.target.equals("panelAction") && urlBuilder.params.getString("action").equals("gotoPanelMore")) {
                    interceptorCallback.interceptor("interceptor");
                    Log.e("interceptor", urlBuilder.params.toString());
                } else {
                    interceptorCallback.onContinue(urlBuilder);
                }
            }
        });
    }

    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    private static Context context;

    public static Context getAppContext() {
        return context;
    }
}