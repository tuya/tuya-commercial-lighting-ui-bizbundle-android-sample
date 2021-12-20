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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.widget.Toast;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.pages.BrowserActivity;
import com.tuya.smart.commercial.lighting.demo.pages.config.view.AddDeviceTipActivity;
import com.tuya.smart.commercial.lighting.demo.pages.config.view.ECActivity;
import com.tuya.smart.commercial.lighting.demo.pages.config.view.IECBindView;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;
import com.tuya.smart.commercial.lighting.demo.utils.ProgressUtil;
import com.tuya.smart.android.device.utils.WiFiUtil;
import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.sdk.os.TuyaOSActivator;
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken;
import com.tuya.smart.sdk.bean.DeviceBean;

import static com.tuya.smart.commercial.lighting.demo.app.IntentExtra.KEY_AREA_ID;
import static com.tuya.smart.commercial.lighting.demo.app.IntentExtra.KEY_PROJECT_ID;


public class ECBindPresenter extends BasePresenter {

    private static final String TAG = "ECBindPresenter";
    private static final int MESSAGE_SHOW_SUCCESS_PAGE = 1001;
    private final Context mContext;
    private final IECBindView mView;

    private static final int MESSAGE_CONFIG_WIFI_OUT_OF_TIME = 0x16;
    private final int mConfigMode;
    private int mTime;
    private boolean mStop;
    private DeviceBindModel mModel;
    private final String mPassWord;
    private final String mSSId;
    private boolean mBindDeviceSuccess;
    private long projectId;
    private long areaId;

    public ECBindPresenter(Context context, IECBindView view, long projectId, long areaId) {
        super(context);
        mContext = context;
        mView = view;
        mConfigMode = ((Activity) mContext).getIntent().getIntExtra(ECActivity.CONFIG_MODE, ECActivity.EC_MODE);
        mModel = new DeviceBindModel(context, mHandler);
        mPassWord = ((Activity) mContext).getIntent().getStringExtra(ECActivity.CONFIG_PASSWORD);
        mSSId = ((Activity) mContext).getIntent().getStringExtra(ECActivity.CONFIG_SSID);
        this.projectId = projectId;
        this.areaId = areaId;
        showConfigDevicePage();
        getTokenForConfigDevice();
    }

    private void showConfigDevicePage() {
        if (mConfigMode == ECActivity.EC_MODE) {
            mView.hideSubPage();
        } else {
            mView.hideMainPage();
            mView.showSubPage();
        }
    }

    private void getTokenForConfigDevice() {
        ProgressUtil.showLoading(mContext, R.string.loading);
        TuyaOSActivator.deviceActivator().getActivatorToken(projectId, new ITuyaActivatorGetToken() {
            @Override
            public void onSuccess(String token) {
                ProgressUtil.hideLoading();
                initConfigDevice(token);
            }

            @Override
            public void onFailure(String s, String s1) {
                ProgressUtil.hideLoading();
                if (mConfigMode == ECActivity.EC_MODE) {
                    mView.showNetWorkFailurePage();
                }
            }
        });

    }

    private void initConfigDevice(String token) {
        if (mConfigMode == ECActivity.EC_MODE) {
            mModel.setEC(mSSId, mPassWord, token);
            startSearch();
        } else if (mConfigMode == ECActivity.AP_MODE) {
            mModel.setAP(mSSId, mPassWord, token);
        }
    }

    public void startSearch() {
        mModel.start();
        mView.showConnectPage();
        mBindDeviceSuccess = false;
        startLoop();
    }

    private void startLoop() {
        mTime = 0;
        mStop = false;
        mHandler.sendEmptyMessage(MESSAGE_CONFIG_WIFI_OUT_OF_TIME);
    }

    public void reStartEZConfig() {
        if (mConfigMode == ECActivity.AP_MODE) {
            Intent intent = new Intent(mContext, AddDeviceTipActivity.class);
            intent.putExtra(KEY_AREA_ID, areaId);
            intent.putExtra(KEY_PROJECT_ID, projectId);
            ActivityUtils.startActivity((Activity) mContext, intent, ActivityUtils.ANIMATE_FORWARD, true);
        } else {
            goToEZActivity();
        }
    }

    private void goToEZActivity() {
        Intent intent = new Intent(mContext, ECActivity.class);
        intent.putExtra(KEY_AREA_ID, areaId);
        intent.putExtra(KEY_PROJECT_ID, projectId);
        ActivityUtils.startActivity((Activity) mContext, intent, ActivityUtils.ANIMATE_FORWARD, true);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MESSAGE_SHOW_SUCCESS_PAGE:
                mView.showSuccessPage();
                break;
            case MESSAGE_CONFIG_WIFI_OUT_OF_TIME:
                checkLoop();
                break;
            case DeviceBindModel.WHAT_EC_GET_TOKEN_ERROR:
                stopSearch();
                mView.showNetWorkFailurePage();
                break;
            case DeviceBindModel.WHAT_EC_ACTIVE_ERROR:
                L.d(TAG, "ec_active_error");
                stopSearch();
                if (mBindDeviceSuccess) {
                    mView.showBindDeviceSuccessFinalTip();
                    break;
                }
                mView.showFailurePage();
                break;

            case DeviceBindModel.WHAT_AP_ACTIVE_ERROR:
                L.d(TAG, "ap_active_error");
                stopSearch();
                if (mBindDeviceSuccess) {
                    mView.showBindDeviceSuccessFinalTip();
                    break;
                }
                mView.showFailurePage();
                String currentSSID = WiFiUtil.getCurrentSSID(mContext);
                if (BindDeviceUtils.isAPMode())
                    WiFiUtil.removeNetwork(mContext, currentSSID);
                break;

            case DeviceBindModel.WHAT_EC_ACTIVE_SUCCESS:
            case DeviceBindModel.WHAT_AP_ACTIVE_SUCCESS:
                L.d(TAG, "active_success");
                DeviceBean configDev = (DeviceBean) ((Result) msg.obj).getObj();
                stopSearch();
                configSuccess(configDev);
                break;

            case DeviceBindModel.WHAT_DEVICE_FIND:
                L.d(TAG, "device_find");
                deviceFind((String) ((Result) (msg.obj)).getObj());
                break;
            case DeviceBindModel.WHAT_BIND_DEVICE_SUCCESS:
                L.d(TAG, "bind_device_success");
                bindDeviceSuccess(((DeviceBean) ((Result) (msg.obj)).getObj()).getName());
                break;

        }
        return super.handleMessage(msg);
    }

    private void bindDeviceSuccess(String name) {
        if (!mStop) {
            mBindDeviceSuccess = true;
            mView.setAddDeviceName(name);
            mView.showBindDeviceSuccessTip();
        }
    }

    private void deviceFind(String gwId) {
        if (!mStop) {
            mView.showDeviceFindTip(gwId);
        }
    }

    private void checkLoop() {
        if (mStop) return;
        if (mTime >= 100) {
            stopSearch();
            mModel.configFailure();
        } else {
            mView.setConnectProgress(mTime++, 1000);
            mHandler.sendEmptyMessageDelayed(MESSAGE_CONFIG_WIFI_OUT_OF_TIME, 1000);
        }
    }

    private void configSuccess(DeviceBean deviceBean) {
        if (deviceBean != null) {
            Toast.makeText(mContext, "the device id is: " + deviceBean.getDevId(), Toast.LENGTH_SHORT).show();
        }
        mView.onConfigSuccess(deviceBean);
        stopSearch();
        mView.showConfigSuccessTip();
        mView.setConnectProgress(100, 800);
        mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_SUCCESS_PAGE, 1000);
    }

    private void stopSearch() {
        mStop = true;
        mHandler.removeMessages(MESSAGE_CONFIG_WIFI_OUT_OF_TIME);
        mModel.cancel();
    }


    public void goForHelp() {
        Intent intent = new Intent(mContext, BrowserActivity.class);
        intent.putExtra(BrowserActivity.EXTRA_LOGIN, false);
        intent.putExtra(BrowserActivity.EXTRA_REFRESH, true);
        intent.putExtra(BrowserActivity.EXTRA_TOOLBAR, true);
        intent.putExtra(BrowserActivity.EXTRA_TITLE, mContext.getString(R.string.ty_ez_help));
        intent.putExtra(BrowserActivity.EXTRA_URI, CommonConfig.FAILURE_URL);
        mContext.startActivity(intent);
    }

    @Override
    public void onDestroy() {
        mHandler.removeMessages(MESSAGE_CONFIG_WIFI_OUT_OF_TIME);
        mHandler.removeMessages(MESSAGE_SHOW_SUCCESS_PAGE);
        mModel.onDestroy();
        super.onDestroy();
    }
}
