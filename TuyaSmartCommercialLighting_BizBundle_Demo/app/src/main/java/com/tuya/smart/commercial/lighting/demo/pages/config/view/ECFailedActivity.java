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
import android.os.Bundle;
import android.view.View;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.pages.BrowserActivity;
import com.tuya.smart.commercial.lighting.demo.pages.config.CommonConfig;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;


public class ECFailedActivity extends BaseActivity {

    public static final String FROM = "from";
    public static final int FROM_EC_ACTIVITY = 1;
    public static final int FROM_EC_BIND_ACTIVITY = 2;
    private static String TAG = "ECFailedActivity";
    private String mPassWord;
    private String mSSId;
    private int mFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_failed);
        initToolbar();
        setTitle(R.string.choose_wifi);
        setDisplayHomeAsUpEnabled();
        mPassWord = getIntent().getStringExtra(ECActivity.CONFIG_PASSWORD);
        mSSId = getIntent().getStringExtra(ECActivity.CONFIG_SSID);
        mFrom = getIntent().getIntExtra(FROM, FROM_EC_BIND_ACTIVITY);
        if (mFrom == FROM_EC_ACTIVITY) {
            setViewGone(findViewById(R.id.tv_network_tip));
        } else {

        }
        findViewById(R.id.tv_bottom_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ECFailedActivity.this, ECBindActivity.class);
                intent.putExtra(ECActivity.CONFIG_PASSWORD, mPassWord);
                intent.putExtra(ECActivity.CONFIG_SSID, mSSId);
                intent.putExtra(ECActivity.CONFIG_MODE, ECActivity.AP_MODE);
                ActivityUtils.startActivity(ECFailedActivity.this, intent, ActivityUtils.ANIMATE_FORWARD, true);
            }
        });
        findViewById(R.id.add_device_tip_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ECFailedActivity.this, BrowserActivity.class);
                intent.putExtra(BrowserActivity.EXTRA_LOGIN, false);
                intent.putExtra(BrowserActivity.EXTRA_REFRESH, true);
                intent.putExtra(BrowserActivity.EXTRA_TOOLBAR, true);
                intent.putExtra(BrowserActivity.EXTRA_TITLE, ECFailedActivity.this.getString(R.string.ty_ez_help));
                intent.putExtra(BrowserActivity.EXTRA_URI, CommonConfig.RESET_URL);
                startActivity(intent);
            }
        });
    }

}
