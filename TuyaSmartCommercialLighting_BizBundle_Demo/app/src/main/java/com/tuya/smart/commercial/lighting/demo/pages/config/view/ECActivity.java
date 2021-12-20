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

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.pages.config.ECPresenter;
import com.tuya.smart.sdk.TuyaSdk;
import com.wnafee.vector.compat.VectorDrawable;

import static com.tuya.smart.commercial.lighting.demo.app.IntentExtra.KEY_AREA_ID;
import static com.tuya.smart.commercial.lighting.demo.app.IntentExtra.KEY_PROJECT_ID;
import static com.tuya.smart.commercial.lighting.demo.pages.config.ECPresenter.CODE_FOR_LOCATION_PERMISSION;
import static com.tuya.smart.commercial.lighting.demo.pages.config.ECPresenter.PRIVATE_CODE;


public class ECActivity extends BaseActivity implements IECView {

    public static final String CONFIG_MODE = "config_mode";
    public static final String CONFIG_PASSWORD = "config_password";
    public static final String CONFIG_SSID = "config_ssid";
    private static final String TAG = "ECActivity";
    public static final int AP_MODE = 0;
    public static final int EC_MODE = 1;

    public TextView mSSID;
    public EditText mPassword;
    public ImageButton mPasswordSwitch;
    ImageView mIvWifi;
    TextView mTvWifiStatus;
    TextView mTvOtherWifi;

    private ECPresenter mECPresenter;

    private boolean passwordOn = true;
    private int mWifiOnColor;
    private View m5gNetworkTip;
    private long areaId;
    private long projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ec_mode);
        areaId = getIntent().getLongExtra(KEY_AREA_ID, -1);
        projectId = getIntent().getLongExtra(KEY_PROJECT_ID, -1);
        initToolbar();
        initView();
        initMenu();
        initPresenter();

        TypedArray a = obtainStyledAttributes(new int[]{
                R.attr.navbar_font_color});
        mWifiOnColor = a.getColor(0, getResources().getColor(R.color.color_primary));
        a.recycle();
    }

    private void initPresenter() {
        mECPresenter = new ECPresenter(this, this, projectId, areaId);
        mECPresenter.showLocationError();
    }


    public void clickPasswordSwitch(View v) {
        passwordOn = !passwordOn;
        if (passwordOn) {
            mPasswordSwitch.setImageResource(R.drawable.ty_password_on);
            mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            mPasswordSwitch.setImageResource(R.drawable.ty_password_off);
            mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        if (mPassword.getText().length() > 0) {
            mPassword.setSelection(mPassword.getText().length());
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tv_other_wifi) {
                mECPresenter.userOtherWifi();
            } else if (v.getId() == R.id.ec_password_switch) {
                clickPasswordSwitch(v);
            } else if (v.getId() == R.id.tv_bottom_button) {
                mECPresenter.goNextStep();
            }
        }
    };

    private void initView() {
        mSSID = (TextView) findViewById(R.id.tv_network);
        mSSID.setVisibility(View.VISIBLE);

        mPassword = (EditText) findViewById(R.id.et_password);
        mPasswordSwitch = (ImageButton) findViewById(R.id.ec_password_switch);
        mPasswordSwitch.setOnClickListener(mOnClickListener);
        mIvWifi = (ImageView) findViewById(R.id.iv_wifi);
        setWifiVectorDrawable(mIvWifi);
        mIvWifi.setColorFilter(mWifiOnColor);

        mTvWifiStatus = (TextView) findViewById(R.id.tv_wifi_status);
        mTvOtherWifi = (TextView) findViewById(R.id.tv_other_wifi);
        mTvOtherWifi.setOnClickListener(mOnClickListener);
        findViewById(R.id.tv_bottom_button).setOnClickListener(mOnClickListener);
        m5gNetworkTip = findViewById(R.id.network_tip);
    }

    private void initMenu() {
        setTitle(getString(R.string.ty_ez_wifi_title));
        setDisplayHomeAsUpEnabled();
    }

    private void setWifiVectorDrawable(ImageView view) {
        VectorDrawable drawable = VectorDrawable.getDrawable(TuyaSdk.getApplication(), R.drawable.wifi_status);
        mIvWifi.setBackground(getResources().getDrawable(R.drawable.bg_bt_circle));
        drawable.setAlpha(128);
        view.setImageDrawable(drawable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mECPresenter.checkWifiNetworkStatus();
    }

    @Override
    protected boolean onPanelKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                mECPresenter.closeECModeActivity();
                return true;
        }
        return false;
    }

    @Override
    public void setWifiSSID(String ssId) {
        setViewVisible(mSSID);
        mIvWifi.setColorFilter(mWifiOnColor);
        mTvWifiStatus.setText(getString(R.string.ty_ez_current_wifi_android));
        mSSID.setText(ssId);
    }

    @Override
    public void setWifiPass(String pass) {
        mPassword.setText(pass);
    }

    @Override
    public String getWifiPass() {
        return mPassword.getText().toString();
    }

    @Override
    public String getWifiSsId() {
        return mSSID.getText().toString();
    }

    @Override
    public void showNoWifi() {
        setViewGone(mSSID);
        hide5gTip();
        setWifiPass("");
        mIvWifi.setColorFilter(Color.GRAY);
        mTvWifiStatus.setText(getString(R.string.ty_ez_current_no_wifi));
    }

    @Override
    public void show5gTip() {
        setViewVisible(m5gNetworkTip);
    }

    @Override
    public void hide5gTip() {
        setViewGone(m5gNetworkTip);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mECPresenter.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PRIVATE_CODE:
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                boolean isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (isOpen) {
                    mECPresenter.checkSinglePermission(Manifest.permission.ACCESS_FINE_LOCATION, CODE_FOR_LOCATION_PERMISSION);
                }
                break;
        }
    }
}
