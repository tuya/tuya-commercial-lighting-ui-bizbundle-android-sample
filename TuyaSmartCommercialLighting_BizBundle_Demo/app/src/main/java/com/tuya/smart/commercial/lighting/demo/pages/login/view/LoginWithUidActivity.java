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

package com.tuya.smart.commercial.lighting.demo.pages.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.tuya.sdk.os.TuyaOSUser;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.android.user.callback.ITuyaUserResultCallback;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;
import com.tuya.smart.commercial.lighting.demo.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginWithUidActivity extends BaseActivity {
    private Unbinder mBind;

    @BindView(R.id.et_uid)
    public EditText mUidEditText;

    @BindView(R.id.btn_login)
    public Button mLoginButton;

    @BindView(R.id.tv_login_result)
    public TextView mLoginResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cl_activity_login_with_uid);
        mBind = ButterKnife.bind(this);
    }

    @Override
    public boolean needLogin() {
        return false;
    }

    @OnClick(R.id.btn_login)
    public void loginWithUid() {
        String content = mUidEditText.getText().toString().trim();

        if (TextUtils.isEmpty(content)) {
            ToastUtil.showToast(this, "Please input a ticket");
            return;
        }

        TuyaOSUser.getUserInstance().loginByTicket(content, new ITuyaUserResultCallback<User>() {
            @Override
            public void onSuccess(User user) {
                ToastUtil.showToast(LoginWithUidActivity.this, "Login success");
                mLoginResult.setText(JSON.toJSONString(user));

                ActivityUtils.gotoHomeActivity(LoginWithUidActivity.this);
            }

            @Override
            public void onError(String code, String error) {
                ToastUtil.showToast(LoginWithUidActivity.this, "Login failed：" + error);
            }
        });
    }

    @OnClick(R.id.btn_login_with_username)
    public void loginWithUserName() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finishActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }
}
