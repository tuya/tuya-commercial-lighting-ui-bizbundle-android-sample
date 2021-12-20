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
import android.widget.TextView;
import android.widget.Toast;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;
import com.tuya.smart.commercial.lighting.demo.utils.ProgressUtil;
import com.tuya.smart.commercial.lighting.demo.utils.ToastUtil;
import com.tuya.smart.android.hardware.bean.HgwBean;
import com.tuya.smart.home.sdk.api.IGwSearchListener;
import com.tuya.smart.home.sdk.api.ITuyaGwSearcher;
import com.tuya.smart.home.sdk.builder.TuyaGwActivatorBuilder;
import com.tuya.smart.lighting.sdk.bean.TransferResultSummary;
import com.tuya.smart.lighting.sdk.impl.DefaultDeviceTransferListener;
import com.tuya.sdk.os.TuyaOSActivator;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingArea;
import com.tuya.smart.sdk.api.ITuyaActivator;
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken;
import com.tuya.smart.sdk.api.ITuyaSmartActivatorListener;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;

import static com.tuya.smart.commercial.lighting.demo.app.IntentExtra.KEY_AREA_ID;
import static com.tuya.smart.commercial.lighting.demo.app.IntentExtra.KEY_PROJECT_ID;

public class ZigbeeConfigActivity extends BaseActivity implements View.OnClickListener, IGwSearchListener {
    private static final String TAG = "ZigbeeConfigActivity";
    protected static final long CONFIG_TIME_OUT = 120;
    private TextView mTextView;
    private HgwBean mHgwBean;
    private String mToken;
    private ITuyaActivator mITuyaActivator;
    private List<String> devIds = new ArrayList<>();
    private long projectId;
    private long areaId;
    private ITuyaGwSearcher mITuyaGwSearcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zigbee_config);
        projectId = getIntent().getLongExtra(KEY_PROJECT_ID, -1);
        areaId = getIntent().getLongExtra(KEY_AREA_ID, -1);
        initView();
        initToolbar();
        setTitle("gateway config");
    }

    private void initView() {
        mTextView = findViewById(R.id.tv_message);
        findViewById(R.id.btn_wired_zigbee).setOnClickListener(this);
        findViewById(R.id.btn_zigbee).setOnClickListener(this);
        findViewById(R.id.btn_area).setOnClickListener(this);
    }

    public void requestGWToken(boolean isWire) {
        ProgressUtil.showLoading(this, "Request token... ");
        TuyaOSActivator.deviceActivator().getActivatorToken(projectId, new ITuyaActivatorGetToken() {
            @Override
            public void onSuccess(String token) {
                mTextView.setText("Get token success");
                mToken = token;
                ProgressUtil.hideLoading();
                if (isWire) {
                    findWireZigbee();
                } else {
                    findZigbee();
                }
            }

            @Override
            public void onFailure(String s, String s1) {
                ProgressUtil.hideLoading();
                mTextView.setText("Get token failed");
                L.d("AddDeviceTypeActivity", "get token error:::" + s + " ++ " + s1);
                Toast.makeText(getApplicationContext(), "get token error: " + s + " ++ " + s1, Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void findZigbee() {
        ToastUtil.showToast(this, "Please confirm that the device is in the state of network configuration");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mITuyaActivator != null) {
            mITuyaActivator.onDestroy();
        }

        if (mITuyaGwSearcher != null) {
            mITuyaGwSearcher.unRegisterGwSearchListener();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_wired_zigbee:
                configWireZigbee();
                break;
            case R.id.btn_zigbee:
                configWire();
                break;
            case R.id.btn_area:
                gotoArea();
                break;


        }
    }


    private void configWire() {
        Intent intent = new Intent(this, ECActivity.class);
        intent.putExtra(ECActivity.CONFIG_MODE, ECActivity.EC_MODE);
        intent.putExtra(KEY_AREA_ID, areaId);
        intent.putExtra(KEY_PROJECT_ID, projectId);
        ActivityUtils.startActivity(this, intent, ActivityUtils.ANIMATE_FORWARD, true);
    }

    private void gotoArea() {
        if (devIds.isEmpty()) {
            ToastUtil.showToast(this, "Please configure the network first");
            return;
        }
        TuyaCommercialLightingArea.newAreaInstance(projectId, areaId).transferDevices(devIds, new DefaultDeviceTransferListener() {
            @Override
            public void handleResult(TransferResultSummary transferResultSummary) {
                mTextView.setText("success：areaId = " + areaId);
            }

            @Override
            public void onError(String errorMsg, String errorCode) {
                mTextView.setText("failed");
            }
        });
    }

    private void configWireZigbee() {
        ToastUtil.showToast(this, "The phone needs to be connected to the same network as the device before acquiring the device");
        mTextView.setText("Obtaining the token...");
        requestGWToken(true);

    }

    private void findWireZigbee() {
        mTextView.setText("Start searching for wired gateway");
        mITuyaGwSearcher = TuyaOSActivator.deviceActivator().newTuyaGwActivator().newSearcher();
        mITuyaGwSearcher.registerGwSearchListener(this);
    }

    private void config() {
        ProgressUtil.showLoading(this, "Start to configure");
        mITuyaActivator = TuyaOSActivator.deviceActivator().newGwActivator(
                new TuyaGwActivatorBuilder()
                        .setToken(mToken)
                        .setTimeOut(CONFIG_TIME_OUT)
                        .setContext(this)
                        .setHgwBean(mHgwBean)
                        .setListener(new ITuyaSmartActivatorListener() {

                                         @Override
                                         public void onError(String errorCode, String errorMsg) {
                                             ProgressUtil.hideLoading();
                                             mITuyaActivator.stop();
                                             mTextView.setText("failed");
                                         }

                                         @Override
                                         public void onActiveSuccess(DeviceBean devResp) {
                                             ProgressUtil.hideLoading();
                                             mITuyaActivator.stop();
                                             mTextView.setText("succeed：devId =" + devResp.getDevId());
                                             devIds.clear();
                                             devIds.add(devResp.devId);
                                         }

                                         @Override
                                         public void onStep(String step, Object data) {
                                         }
                                     }
                        ));

        mITuyaActivator.start();
    }

    @Override
    public void onDevFind(HgwBean gw) {
        mTextView.setText("Search successful");
        mHgwBean = gw;
        config();
    }


}
