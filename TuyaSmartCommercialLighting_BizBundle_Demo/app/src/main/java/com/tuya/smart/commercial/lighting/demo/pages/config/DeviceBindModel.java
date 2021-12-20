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

package com.tuya.smart.commercial.lighting.demo.pages.config;

import android.content.Context;

import com.tuya.smart.android.common.utils.SafeHandler;
import com.tuya.smart.android.mvp.model.BaseModel;
import com.tuya.smart.home.sdk.builder.ActivatorBuilder;
import com.tuya.sdk.os.TuyaOSActivator;
import com.tuya.smart.sdk.api.ITuyaActivator;
import com.tuya.smart.sdk.api.ITuyaSmartActivatorListener;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.sdk.enums.ActivatorAPStepCode;
import com.tuya.smart.sdk.enums.ActivatorEZStepCode;
import com.tuya.smart.sdk.enums.ActivatorModelEnum;

import static com.tuya.smart.sdk.enums.ActivatorModelEnum.TY_AP;
import static com.tuya.smart.sdk.enums.ActivatorModelEnum.TY_EZ;


public class DeviceBindModel extends BaseModel implements IDeviceBindModel {
    public static final String STATUS_FAILURE_WITH_NETWORK_ERROR = "1001";
    public static final String STATUS_FAILURE_WITH_BIND_GWIDS = "1002";
    public static final String STATUS_FAILURE_WITH_BIND_GWIDS_1 = "1003";
    public static final String STATUS_FAILURE_WITH_GET_TOKEN = "1004";
    public static final String STATUS_FAILURE_WITH_CHECK_ONLINE_FAILURE = "1005";
    public static final String STATUS_FAILURE_WITH_OUT_OF_TIME = "1006";
    public static final String STATUS_DEV_CONFIG_ERROR_LIST = "1007";
    public static final int WHAT_EC_ACTIVE_ERROR = 0x02;
    public static final int WHAT_EC_ACTIVE_SUCCESS = 0x03;
    public static final int WHAT_AP_ACTIVE_ERROR = 0x04;
    public static final int WHAT_AP_ACTIVE_SUCCESS = 0x05;
    public static final int WHAT_EC_GET_TOKEN_ERROR = 0x06;
    public static final int WHAT_DEVICE_FIND = 0x07;
    public static final int WHAT_BIND_DEVICE_SUCCESS = 0x08;
    private static final long CONFIG_TIME_OUT = 100;

    private ITuyaActivator mTuyaActivator;
    private ActivatorModelEnum mModelEnum;

    public DeviceBindModel(Context ctx, SafeHandler handler) {
        super(ctx, handler);
    }


    @Override
    public void start() {
        if (mTuyaActivator != null) {
            mTuyaActivator.start();
        }
    }

    @Override
    public void cancel() {
        if (mTuyaActivator != null) {
            mTuyaActivator.stop();
        }
    }

    @Override
    public void setEC(String ssid, String password, String token) {
        mModelEnum = TY_EZ;
        mTuyaActivator = TuyaOSActivator.deviceActivator().newMultiActivator(new ActivatorBuilder()
                .setSsid(ssid)
                .setContext(mContext)
                .setPassword(password)
                .setActivatorModel(TY_EZ)
                .setTimeOut(CONFIG_TIME_OUT)
                .setToken(token).setListener(new ITuyaSmartActivatorListener() {
                    @Override
                    public void onError(String s, String s1) {
                        switch (s) {
                            case STATUS_FAILURE_WITH_GET_TOKEN:
                                resultError(WHAT_EC_GET_TOKEN_ERROR, "wifiError", s1);
                                return;
                        }
                        resultError(WHAT_EC_ACTIVE_ERROR, s, s1);
                    }

                    @Override
                    public void onActiveSuccess(DeviceBean deviceBean) {
                        resultSuccess(WHAT_EC_ACTIVE_SUCCESS, deviceBean);
                    }

                    @Override
                    public void onStep(String s, Object o) {
                        switch (s) {
                            case ActivatorEZStepCode.DEVICE_BIND_SUCCESS:
                                resultSuccess(WHAT_BIND_DEVICE_SUCCESS, o);
                                break;
                            case ActivatorEZStepCode.DEVICE_FIND:
                                resultSuccess(WHAT_DEVICE_FIND, o);
                                break;
                        }
                    }
                }));
    }

    @Override
    public void setAP(String ssid, String password, String token) {
        mModelEnum = TY_AP;
        mTuyaActivator = TuyaOSActivator.deviceActivator().newActivator(new ActivatorBuilder()
                .setSsid(ssid)
                .setContext(mContext)
                .setPassword(password)
                .setActivatorModel(TY_AP)
                .setTimeOut(CONFIG_TIME_OUT)
                .setToken(token).setListener(new ITuyaSmartActivatorListener() {
                    @Override
                    public void onError(String error, String s1) {
                        resultError(WHAT_AP_ACTIVE_ERROR, error, s1);
                    }

                    @Override
                    public void onActiveSuccess(DeviceBean gwDevResp) {
                        resultSuccess(WHAT_AP_ACTIVE_SUCCESS, gwDevResp);
                    }

                    @Override
                    public void onStep(String step, Object o) {
                        switch (step) {
                            case ActivatorAPStepCode.DEVICE_BIND_SUCCESS:
                                resultSuccess(WHAT_BIND_DEVICE_SUCCESS, o);
                                break;
                            case ActivatorAPStepCode.DEVICE_FIND:
                                resultSuccess(WHAT_DEVICE_FIND, o);
                                break;
                        }
                    }
                }));

    }

    @Override
    public void configFailure() {
        if (mModelEnum == null) return;
        if (mModelEnum == TY_AP) {
            resultError(WHAT_AP_ACTIVE_ERROR, "TIME_ERROR", "OutOfTime");
        } else {
            resultError(WHAT_EC_ACTIVE_ERROR, "TIME_ERROR", "OutOfTime");
        }
    }

    @Override
    public void onDestroy() {
        if (mTuyaActivator != null)
            mTuyaActivator.onDestroy();

    }
}
