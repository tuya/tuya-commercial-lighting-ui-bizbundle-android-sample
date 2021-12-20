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

import android.os.Bundle;
import android.view.View;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.user.api.ILogoutCallback;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;
import com.tuya.smart.commercial.lighting.demo.utils.LoginHelper;
import com.tuya.sdk.os.TuyaOSUser;

public class LauncherActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        View projectManagementView = findViewById(R.id.btn_project_management);
        View logoutView = findViewById(R.id.btn_logout);

        projectManagementView.setOnClickListener(this);
        logoutView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_project_management:
                ActivityUtils.gotoProjectManagementActivity(this);
                break;
            case R.id.btn_logout:
                logout();
                break;
            default:
                break;
        }
    }

    private void logout() {
        TuyaOSUser.getUserInstance().logout(new ILogoutCallback() {
            @Override
            public void onSuccess() {
                LoginHelper.reLogin(LauncherActivity.this, false);
            }

            @Override
            public void onError(String code, String error) {
                L.e(code, error);
            }
        });
    }
}
