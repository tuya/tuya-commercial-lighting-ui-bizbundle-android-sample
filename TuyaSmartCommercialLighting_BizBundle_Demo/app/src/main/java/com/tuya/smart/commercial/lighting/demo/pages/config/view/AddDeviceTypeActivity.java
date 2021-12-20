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

import static com.tuya.smart.commercial.lighting.demo.app.IntentExtra.KEY_AREA_ID;
import static com.tuya.smart.commercial.lighting.demo.app.IntentExtra.KEY_PROJECT_ID;


public class AddDeviceTypeActivity extends BaseActivity {
    private long areaId;
    private long projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_type);
        areaId = getIntent().getLongExtra(KEY_AREA_ID, -1);
        projectId = getIntent().getLongExtra(KEY_PROJECT_ID, -1);
        initToolbar();
        initView();
        setTitle(getString(R.string.ty_add_device_sort));
        setDisplayHomeAsUpEnabled();
    }

    private void initView() {
        findViewById(R.id.wifi_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWifiDevConfig();
            }
        });
        findViewById(R.id.gateway_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGatewayDevConfig();
            }
        });

        findViewById(R.id.sigmesh_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSigMeshDevConfig();
            }
        });
    }

    private void startSigMeshDevConfig() {
        Intent intent = new Intent(this, SigMeshConfigActivity.class);
        intent.putExtra(KEY_AREA_ID, areaId);
        intent.putExtra(KEY_PROJECT_ID, projectId);
        startActivity(intent);
    }

    private void startGatewayDevConfig() {
        Intent intent = new Intent(this, ZigbeeConfigActivity.class);
        intent.putExtra(KEY_AREA_ID, areaId);
        intent.putExtra(KEY_PROJECT_ID, projectId);
        startActivity(intent);
    }

    private void startWifiDevConfig() {
        Intent intent = new Intent(this, AddDeviceTipActivity.class);
        intent.putExtra(KEY_AREA_ID, areaId);
        intent.putExtra(KEY_PROJECT_ID, projectId);
        startActivity(intent);
    }
}
