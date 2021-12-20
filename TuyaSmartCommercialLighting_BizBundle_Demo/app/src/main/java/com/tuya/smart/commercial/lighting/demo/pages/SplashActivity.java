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

package com.tuya.smart.commercial.lighting.demo.pages;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.tuya.sdk.os.TuyaOSUser;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.common.utils.TuyaUtil;
import com.tuya.smart.commercial.lighting.demo.pages.login.view.LoginWithUidActivity;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;
import com.tuya.smart.commercial.lighting.demo.utils.DialogUtil;
import com.tuya.smart.commercial.lighting.demo.utils.LoginHelper;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d("splash", "tuyaTime: " + TuyaUtil.formatDate(System.currentTimeMillis(), "yyyy-mm-dd hh:mm:ss"));

        if (isInitAppkey()) {
            gotoLogin();
        } else {
            showTipDialog();
        }
    }

    public void gotoLogin() {
        if (TuyaOSUser.getUserInstance().isLogin()) {//已登录，跳主页
            LoginHelper.afterLogin();
            ActivityUtils.gotoHomeActivity(this);
        } else {
            ActivityUtils.gotoActivity(this, LoginWithUidActivity.class, ActivityUtils.ANIMATE_FORWARD, true);
        }
    }


    private void showTipDialog() {
        DialogUtil.simpleConfirmDialog(this, "appkey or appsecret is empty. \nPlease check your configuration", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }

    private boolean isInitAppkey() {
        String appkey = getInfo("TUYA_SMART_APPKEY", this);
        String appSecret = getInfo("TUYA_SMART_SECRET", this);
        if (TextUtils.equals("null", appkey) || TextUtils.equals("null", appSecret)) return false;
        return !TextUtils.isEmpty(appkey) && !TextUtils.isEmpty(appSecret);
    }

    public static String getInfo(String infoName, Context context) {
        ApplicationInfo e;
        try {
            e = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return e.metaData.getString(infoName);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return "";
    }

}
