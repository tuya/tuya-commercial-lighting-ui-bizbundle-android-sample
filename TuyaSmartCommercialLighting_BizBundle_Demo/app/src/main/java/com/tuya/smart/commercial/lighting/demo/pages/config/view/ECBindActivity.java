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

package com.tuya.smart.commercial.lighting.demo.pages.config.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.pages.BrowserActivity;
import com.tuya.smart.commercial.lighting.demo.pages.config.BindDeviceUtils;
import com.tuya.smart.commercial.lighting.demo.pages.config.CommonConfig;
import com.tuya.smart.commercial.lighting.demo.pages.config.ECBindPresenter;
import com.tuya.smart.commercial.lighting.demo.utils.ToastUtil;
import com.tuya.smart.commercial.lighting.demo.utils.ViewUtils;
import com.tuya.smart.commercial.lighting.demo.widget.circleprogress.CircleProgressView;
import com.tuya.smart.lighting.sdk.bean.TransferResultSummary;
import com.tuya.smart.lighting.sdk.impl.DefaultDeviceTransferListener;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingArea;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.wnafee.vector.compat.VectorDrawable;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tuya.smart.commercial.lighting.demo.app.IntentExtra.KEY_AREA_ID;
import static com.tuya.smart.commercial.lighting.demo.app.IntentExtra.KEY_PROJECT_ID;

public class ECBindActivity extends BaseActivity implements IECBindView {
    private static final String TAG = "ECBindActivity";
    CircleProgressView mCircleView;
    TextView mEcConnectTip;
    LinearLayout mEcConnecting;
    Button mTvFinishButton;
    Button mTvShareButton;
    Button mTvRetryButton;
    TextView mTvAddDeviceSuccess;
    LinearLayout mLlFailureView;
    TextView mNetworkTip;
    private ECBindPresenter mECBindPresenter;
    private View mRetryContactTip;
    private TextView mHelp;

    private TextView mDeviceFindTip;
    private TextView mDeviceBindSussessTip;
    private TextView mDeviceInitTip;
    private TextView mDeviceInitFailureTip;

    private RelativeLayout mSwitchWifiLayout;
    private Unbinder mBind;
    private long areaId;
    private long projectId;
    private List<String> devIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ec_bind1);
        areaId = getIntent().getLongExtra(KEY_AREA_ID, -1);
        projectId = getIntent().getLongExtra(KEY_PROJECT_ID, -1);
        mBind = ButterKnife.bind(this);
        initView();
        initToolbar();
        initMenu();
        initPresenter();
        mCircleView.setValueInterpolator(new LinearInterpolator());
        registerWifiReceiver();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tv_retry_button) {
                onClickRetry();
            } else if (v.getId() == R.id.tv_share_button) {
                onClickConnect();
            } else if (v.getId() == R.id.tv_finish_button) {
                onClickFinish();
            } else if (v.getId() == R.id.tv_ec_find_search_help) {
                onClickFins();
            }
        }
    };

    private void initView() {
        mCircleView = (CircleProgressView) findViewById(R.id.circleView);
        mEcConnectTip = (TextView) findViewById(R.id.ec_connect_tip);
        mEcConnecting = (LinearLayout) findViewById(R.id.ec_connecting);
        mTvFinishButton = (Button) findViewById(R.id.tv_finish_button);
        mTvFinishButton.setOnClickListener(mOnClickListener);
        mTvShareButton = (Button) findViewById(R.id.tv_share_button);
        mTvShareButton.setOnClickListener(mOnClickListener);
        mTvRetryButton = (Button) findViewById(R.id.tv_retry_button);
        mTvRetryButton.setOnClickListener(mOnClickListener);
        mTvAddDeviceSuccess = (TextView) findViewById(R.id.tv_add_device_success);
        mLlFailureView = (LinearLayout) findViewById(R.id.ll_failure_view);
        mNetworkTip = (TextView) findViewById(R.id.network_tip);
        mRetryContactTip = findViewById(R.id.tv_add_device_contact_tip);
        mHelp = (TextView) findViewById(R.id.tv_ec_find_search_help);
        mHelp.setOnClickListener(mOnClickListener);
        int color = ViewUtils.getColor(this, R.color.navbar_font_color);
        mCircleView.setBarColor(color);
        mCircleView.setSpinBarColor(color);
        mCircleView.setTextColor(color);
        mCircleView.setUnitColor(color);
        mCircleView.setRimColor(Color.argb(51, Color.red(color), Color.green(color), Color.blue(color)));

        ImageView failIv = (ImageView) findViewById(R.id.iv_add_device_fail);
        VectorDrawable drawable = VectorDrawable.getDrawable(TuyaSdk.getApplication(), R.drawable.add_device_fail_icon);
        failIv.setImageDrawable(drawable);

        mDeviceFindTip = (TextView) findViewById(R.id.tv_dev_find);
        mDeviceBindSussessTip = (TextView) findViewById(R.id.tv_bind_success);
        mDeviceInitTip = (TextView) findViewById(R.id.tv_device_init);
        mDeviceInitFailureTip = (TextView) findViewById(R.id.tv_device_init_tip);

        mSwitchWifiLayout = (RelativeLayout) findViewById(R.id.switch_wifi_layout);
        ((TextView) findViewById(R.id.tv_ap_ssid)).setText(CommonConfig.DEFAULT_COMMON_AP_SSID.concat("_XXX"));
    }

    @OnClick(R.id.tv_bottom_button)
    public void onClickSettings() {
        Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
        if (null != wifiSettingsIntent.resolveActivity(getPackageManager())) {
            startActivity(wifiSettingsIntent);
        } else {
            wifiSettingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            if (null != wifiSettingsIntent.resolveActivity(getPackageManager())) {
                startActivity(wifiSettingsIntent);
            }
        }
    }

    @OnClick(R.id.add_device_tip_help)
    public void onClickHelp() {
        Intent intent = new Intent(this, BrowserActivity.class);
        intent.putExtra(BrowserActivity.EXTRA_LOGIN, false);
        intent.putExtra(BrowserActivity.EXTRA_REFRESH, true);
        intent.putExtra(BrowserActivity.EXTRA_TOOLBAR, true);
        intent.putExtra(BrowserActivity.EXTRA_TITLE, getString(R.string.ty_ez_help));
        intent.putExtra(BrowserActivity.EXTRA_URI, CommonConfig.RESET_URL);
        startActivity(intent);
    }

    @Override
    public void showSubPage() {
        setViewVisible(mSwitchWifiLayout);
    }

    @Override
    public void hideSubPage() {
        setViewGone(mSwitchWifiLayout);
    }

    @Override
    public void showMainPage() {
        setViewVisible(mDeviceFindTip);
        setViewVisible(mDeviceBindSussessTip);
        setViewVisible(mDeviceInitTip);
    }

    @Override
    public void hideMainPage() {
        setViewGone(mEcConnecting);
        setViewGone(mTvFinishButton);
        setViewGone(mTvShareButton);
        setViewGone(mTvRetryButton);
        setViewGone(mRetryContactTip);
        setViewGone(mTvAddDeviceSuccess);
        setViewGone(mDeviceInitFailureTip);
        setViewGone(mLlFailureView);
        setViewGone(mDeviceFindTip);
        setViewGone(mDeviceBindSussessTip);
        setViewGone(mDeviceInitTip);
    }

    private void initPresenter() {
        mECBindPresenter = new ECBindPresenter(this, this, projectId, areaId);
    }

    private void initMenu() {
        setTitle(getString(R.string.ty_ez_connecting_device_title));
        setDisplayHomeAsUpEnabled();
    }

    public void showSuccessPage() {
        setAddDeviceTipGone();
        setViewVisible(mTvAddDeviceSuccess);
        setViewVisible(mTvFinishButton);
        setViewVisible(mTvShareButton);
        setViewGone(mEcConnecting);
    }

    private void setAddDeviceTipGone() {
        setViewGone(mDeviceBindSussessTip);
        setViewGone(mDeviceFindTip);
        setViewGone(mDeviceInitTip);
    }

    public void showFailurePage() {
        setAddDeviceTipGone();
        setTitle(getString(R.string.ty_ap_error_title));
        setViewGone(mEcConnecting);
        setViewVisible(mLlFailureView);
        setViewVisible(mTvRetryButton);
        setViewVisible(mRetryContactTip);
        mHelp.setText(R.string.ty_ap_error_description);
    }

    public void showConnectPage() {
        setTitle(getString(R.string.ty_ez_connecting_device_title));
        setViewVisible(mEcConnecting);
        setViewGone(mLlFailureView);
        setViewGone(mTvRetryButton);
        setViewGone(mRetryContactTip);
    }

    public void setConnectProgress(float progress, int animationDuration) {
        mCircleView.setValueAnimated(progress, animationDuration);
    }

    @Override
    public void showNetWorkFailurePage() {
        showFailurePage();
        setViewGone(mRetryContactTip);
        mHelp.setText(R.string.network_time_out);
    }

    @Override
    public void showBindDeviceSuccessTip() {
        ViewUtils.setTextViewDrawableLeft(TuyaSdk.getApplication(), mDeviceBindSussessTip, R.drawable.ty_add_device_ok_tip);
    }

    @Override
    public void showDeviceFindTip(String gwId) {
        ViewUtils.setTextViewDrawableLeft(TuyaSdk.getApplication(), mDeviceFindTip, R.drawable.ty_add_device_ok_tip);
    }

    @Override
    public void showConfigSuccessTip() {
        ViewUtils.setTextViewDrawableLeft(TuyaSdk.getApplication(), mDeviceInitTip, R.drawable.ty_add_device_ok_tip);
    }

    @Override
    public void showBindDeviceSuccessFinalTip() {
        showSuccessPage();
        setViewVisible(mDeviceInitFailureTip);
    }

    @Override
    public void setAddDeviceName(String name) {
        mTvAddDeviceSuccess.setText(String.format("%s%s", name, mTvAddDeviceSuccess.getText().toString()));
    }

    @Override
    public void onConfigSuccess(DeviceBean deviceBean) {
        if (!devIds.contains(deviceBean.devId)) {
            devIds.add(deviceBean.devId);
        }
    }

    public void onClickRetry() {
        mCircleView.setValue(0);
        mECBindPresenter.reStartEZConfig();
    }

    public void onClickConnect() {
//        mECBindPresenter.gotoShareActivity();
        if (projectId == -1 || areaId == -1) {
            ToastUtil.showToast(this, "Invalid projectId or areaId");
            return;
        }
        ToastUtil.showToast(this, "Begin..");
        TuyaCommercialLightingArea.newAreaInstance(projectId, areaId).transferDevices(devIds, new DefaultDeviceTransferListener() {
            @Override
            public void handleResult(TransferResultSummary transferResultSummary) {
                ToastUtil.showToast(ECBindActivity.this, "Success");
            }

            @Override
            public void onError(String errorMsg, String errorCode) {
                ToastUtil.showToast(ECBindActivity.this, "Failed");
            }
        });

    }

    public void onClickFinish() {
        onBackPressed();

    }

    public void onClickFins() {
        mECBindPresenter.goForHelp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSSIDAndGoNext();
    }

    @Override
    public void onDestroy() {
        mBind.unbind();
        mECBindPresenter.onDestroy();
        unRegisterWifiReceiver();
        super.onDestroy();
    }

    private void checkSSIDAndGoNext() {
        if (isPause()) return;
        if (BindDeviceUtils.isAPMode()) {
            unRegisterWifiReceiver();
            hideSubPage();
            showMainPage();
            mECBindPresenter.startSearch();
        }
    }

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, final Intent intent) {
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (networkInfo.isAvailable() && networkInfo.isConnected()) {
                    checkSSIDAndGoNext();
                }
            }
        }
    };

    private void registerWifiReceiver() {
        try {
            registerReceiver(mBroadcastReceiver, new IntentFilter(
                    WifiManager.NETWORK_STATE_CHANGED_ACTION));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unRegisterWifiReceiver() {
        try {
            if (mBroadcastReceiver != null) {
                unregisterReceiver(mBroadcastReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
