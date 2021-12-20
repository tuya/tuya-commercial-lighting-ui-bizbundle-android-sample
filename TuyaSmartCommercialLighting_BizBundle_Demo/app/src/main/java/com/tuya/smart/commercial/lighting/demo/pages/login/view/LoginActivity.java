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
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.tuya.smart.android.common.utils.ValidatorUtil;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.utils.ProgressUtil;
import com.tuya.smart.commercial.lighting.demo.utils.ToastUtil;
import com.tuya.smart.commercial.lighting.demo.pages.login.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tuya.smart.commercial.lighting.demo.pages.login.view.AccountInputActivity.EXTRA_ACCOUNT_INPUT_MODE;
import static com.tuya.smart.commercial.lighting.demo.pages.login.view.AccountInputActivity.MODE_PASSWORD_FOUND;

public class LoginActivity extends BaseActivity implements ILoginView, TextWatcher {

    @BindView(R.id.login_submit)
    public Button mLoginSubmit;

    @BindView(R.id.country_name)
    public TextView mCountryName;

    @BindView(R.id.password)
    public EditText mPassword;

    @BindView(R.id.password_switch)
    public ImageButton mPasswordSwitch;
    @BindView(R.id.user_name)
    public TextView mUserName;
    private Unbinder mBind;

    private LoginPresenter mLoginPresenter;

    private boolean passwordOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mBind = ButterKnife.bind(this);
        initToolbar();
        initView();
        initTitle();
        initMenu();
        disableLogin();
        mLoginPresenter = new LoginPresenter(this, this);
    }


    private void initMenu() {
        setMenu(R.menu.toolbar_login_menu, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_login_reg_onclick) {
                    AccountInputActivity.gotoAccountInputActivity(LoginActivity.this, AccountInputActivity.MODE_REGISTER, 0);
                }
                return false;
            }
        });
    }

    private void initTitle() {
        setTitle(getString(R.string.login));
    }

    private void initView() {
        passwordOn = false;
        mUserName.addTextChangedListener(this);
        mPassword.addTextChangedListener(this);
        mPasswordSwitch.setImageResource(R.drawable.ty_password_off);
        mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            disableLogin();
        } else {
            if (ValidatorUtil.isEmail(userName)) {
                enableLogin();
            } else {
                try {
                    Long.valueOf(userName);
                    enableLogin();
                } catch (Exception e) {
                    disableLogin();
                }
            }
        }
    }

    @OnClick(R.id.option_validate_code)
    public void loginWithPhoneCode() {
        startActivity(new Intent(LoginActivity.this, LoginWithPhoneActivity.class));
    }

    @OnClick(R.id.option_forget_password)
    public void retrievePassword() {
        Intent intent = new Intent(LoginActivity.this, AccountInputActivity.class);
        intent.putExtra(EXTRA_ACCOUNT_INPUT_MODE, MODE_PASSWORD_FOUND);
        startActivity(intent);
    }

    @OnClick(R.id.country_name)
    public void onClickSelectCountry() {
        mLoginPresenter.selectCountry();
    }

    @OnClick(R.id.password_switch)
    public void onClickPasswordSwitch() {
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

    @OnClick(R.id.login_with_uid)
    public void loginWithUid() {
        Intent intent = new Intent(this, LoginWithUidActivity.class);
        startActivity(intent);
        finishActivity();
    }

    @OnClick(R.id.login_submit)
    public void onClickLogin() {
        if (mLoginSubmit.isEnabled()) {
            String userName = mUserName.getText().toString();
            if (!ValidatorUtil.isEmail(userName) && mCountryName.getText().toString().contains("+86") && mUserName.getText().length() != 11) {
                ToastUtil.shortToast(LoginActivity.this, getString(R.string.ty_phone_num_error));
                return;
            }
            hideIMM();
            disableLogin();
            ProgressUtil.showLoading(LoginActivity.this, R.string.logining);
            mLoginPresenter.login(userName, mPassword.getText().toString());
        }
    }

    @Override
    public void setCountry(String name, String code) {
        mCountryName.setText(String.format("%s +%s", name, code));
    }

    @Override
    public void modelResult(int what, Result result) {
        switch (what) {
            case LoginPresenter.MSG_LOGIN_SUCCESS:
                ProgressUtil.hideLoading();
                break;
            case LoginPresenter.MSG_LOGIN_FAILURE:
                ProgressUtil.hideLoading();
                ToastUtil.shortToast(this, result.error);
                enableLogin();
                break;
            default:
                break;
        }
    }

    public void enableLogin() {
        if (!mLoginSubmit.isEnabled()) mLoginSubmit.setEnabled(true);
    }

    public void disableLogin() {
        if (mLoginSubmit.isEnabled()) mLoginSubmit.setEnabled(false);
    }

    @Override
    public boolean needLogin() {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        mLoginPresenter.onDestroy();
    }
}
