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

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;

import static com.tuya.smart.commercial.lighting.demo.app.IntentExtra.KEY_AREA_ID;
import static com.tuya.smart.commercial.lighting.demo.app.IntentExtra.KEY_PROJECT_ID;


public class AddDeviceAPTipActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "AddDeviceAPTipActivity";
    public static final String FROM_EZ_FAILURE = "FROM_EZ_FAILURE";

    private TextView mStatusLightTip;
    private Button mStatusLightOption;
    private Button mStatusLightHelp;
    private ImageView mStatusLightImageView;
    private AnimationDrawable mStatusLightAnimation;
    private long areaId;
    private long projectId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_tip);
        areaId = getIntent().getLongExtra(KEY_AREA_ID, -1);
        projectId = getIntent().getLongExtra(KEY_PROJECT_ID, -1);
        initToolbar();
        initView();
        setTitle(R.string.choose_wifi);
        setDisplayHomeAsUpEnabled();

    }

    private void initView() {
        mStatusLightTip = (TextView) findViewById(R.id.status_light_tip);
        mStatusLightTip.setText(R.string.ty_add_device_ap_info);

        initTipImageview();

        mStatusLightOption = (Button) findViewById(R.id.status_light_option);
        mStatusLightOption.setText(R.string.ty_add_device_ap_btn_info);
        mStatusLightOption.setOnClickListener(this);
        mStatusLightHelp = (Button) findViewById(R.id.status_light_help);
        mStatusLightHelp.setOnClickListener(this);
        if (getIntent().getBooleanExtra(FROM_EZ_FAILURE, false)) {
            ((TextView) findViewById(R.id.status_light_tip)).setText(R.string.ty_add_device_ez_failure_ap_tip);
        }
    }

    private void initTipImageview() {
        mStatusLightImageView = (ImageView) findViewById(R.id.status_light_imageview);
        mStatusLightAnimation = new AnimationDrawable();
        mStatusLightAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.ty_adddevice_lighting), 1500);
        mStatusLightAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.ty_adddevice_light), 1500);
        mStatusLightAnimation.setOneShot(false);
        mStatusLightImageView.setImageDrawable(mStatusLightAnimation);
        mStatusLightAnimation.start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.status_light_option) {
            gotoNextActivity();
        } else if (v.getId() == R.id.status_light_help) {
            ActivityUtils.gotoAddDeviceHelpActivity(this, getString(R.string.ty_ez_help));
        }
    }

    private void gotoNextActivity() {
        if (getIntent().getBooleanExtra(FROM_EZ_FAILURE, false)) {
            gotoApTipActivity();
        } else {
            gotoWifiInputActivity();
        }
    }

    private void gotoWifiInputActivity() {
        Intent intent = new Intent(AddDeviceAPTipActivity.this, ECActivity.class);
        intent.putExtra(ECActivity.CONFIG_MODE, ECActivity.AP_MODE);
        intent.putExtra(KEY_AREA_ID, areaId);
        intent.putExtra(KEY_PROJECT_ID, projectId);
        ActivityUtils.startActivity(AddDeviceAPTipActivity.this, intent, ActivityUtils.ANIMATE_FORWARD, true);
    }

    private void gotoApTipActivity() {
        Intent getIntent = getIntent();
        Intent intent = new Intent(this, ECBindActivity.class);
        intent.putExtra(ECActivity.CONFIG_PASSWORD, getIntent.getStringExtra(ECActivity.CONFIG_PASSWORD));
        intent.putExtra(ECActivity.CONFIG_SSID, getIntent.getStringExtra(ECActivity.CONFIG_SSID));
        intent.putExtra(ECActivity.CONFIG_MODE, ECActivity.AP_MODE);
        intent.putExtra(KEY_AREA_ID, areaId);
        intent.putExtra(KEY_PROJECT_ID, projectId);
        ActivityUtils.startActivity(this, intent, ActivityUtils.ANIMATE_FORWARD, true);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AddDeviceTipActivity.class);
        ActivityUtils.startActivity(this, intent, ActivityUtils.ANIMATE_BACK, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mStatusLightAnimation != null && mStatusLightAnimation.isRunning()) {
            mStatusLightAnimation.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mStatusLightAnimation != null && !mStatusLightAnimation.isRunning()) {
            mStatusLightAnimation.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mStatusLightAnimation != null && mStatusLightAnimation.isRunning()) {
            mStatusLightAnimation.stop();
        }
    }
}
