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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;
import com.tuya.smart.commercial.lighting.demo.utils.ToastUtil;
import com.tuya.smart.commercial.lighting.demo.pages.login.presenter.AccountConfirmPresenter;

public class AccountConfirmActivity extends BaseActivity implements TextWatcher, IAccountConfirmView {
    public static final String EXTRA_ACCOUNT_CONFIRM_MODE = "extra_account_confirm_mode";
    public static final String EXTRA_ACCOUNT_PLATFORM = "extra_account_platform";

    public static final String EXTRA_ACCOUNT = "extra_account";
    public static final String EXTRA_COUNTRY_CODE = "extra_country_code";

    public static final int MODE_REGISTER = 0;
    public static final int MODE_FORGET_PASSWORD = 1;
    public static final int MODE_CHANGE_PASSWORD = 2;

    public static final int PLATFORM_EMAIL = 0;
    public static final int PLATFORM_PHONE = 1;

    private EditText mValidateCode;
    private EditText mETPassword;
    private EditText mETCompanyName;
    private Button mGetValidateCode;
    private Button mBtConfirm;
    private TextView mTip;
    private ImageButton mPasswordSwitch;

    private AccountConfirmPresenter mPresenter;
    private int mMode = MODE_REGISTER;
    private int mPlatform = PLATFORM_PHONE;
    private boolean passwordOn = true;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bt_confirm) {
                mPresenter.confirm();
            } else if (v.getId() == R.id.get_validate_code) {
                sendValidateCode();
            } else if (v.getId() == R.id.bt_password_switch) {
                clickPasswordSwitch(v);
            }
        }
    };

    public static void gotoAccountConfirmActivityForResult(Activity mContext, String account, String countryCode, int mode, int accountType, int requestCode) {
        Intent intent = new Intent(mContext, AccountConfirmActivity.class);
        intent.putExtra(AccountConfirmActivity.EXTRA_COUNTRY_CODE, countryCode);
        intent.putExtra(AccountConfirmActivity.EXTRA_ACCOUNT, account);
        intent.putExtra(AccountConfirmActivity.EXTRA_ACCOUNT_PLATFORM, accountType);
        intent.putExtra(AccountConfirmActivity.EXTRA_ACCOUNT_CONFIRM_MODE, mode);
        ActivityUtils.startActivityForResult(mContext, intent, requestCode, 0, false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_code_request);
        initData();
        initToolbar();
        initView();
        initTitle();
        initPresenter();
    }

    public void sendValidateCode() {
        if (mGetValidateCode.isEnabled()) {
            hideIMM();
            disableGetValidateCode();
            mPresenter.getValidateCode();
        }
    }


    private void initTitle() {
        switch (mMode) {
            case MODE_CHANGE_PASSWORD:
                setTitle(R.string.ty_login_modify_password);
                break;

            case MODE_FORGET_PASSWORD:
                setTitle(R.string.ty_login_forget_keyword);
                break;

            case MODE_REGISTER:
                setTitle(R.string.ty_login_register);
                mETCompanyName.setVisibility(View.VISIBLE);
                break;
        }
        setDisplayHomeAsUpEnabled();
    }

    private void initData() {
        mMode = getIntent().getIntExtra(EXTRA_ACCOUNT_CONFIRM_MODE, MODE_REGISTER);
        mPlatform = getIntent().getIntExtra(EXTRA_ACCOUNT_PLATFORM, PLATFORM_PHONE);
    }

    private void initView() {
        mValidateCode = (EditText) findViewById(R.id.validate_code);
        mETPassword = (EditText) findViewById(R.id.et_password);
        mETCompanyName = findViewById(R.id.et_company_name);
        mETPassword.addTextChangedListener(this);
        mGetValidateCode = (Button) findViewById(R.id.get_validate_code);
        mGetValidateCode.setOnClickListener(mOnClickListener);

        mBtConfirm = (Button) findViewById(R.id.bt_confirm);
        mBtConfirm.setOnClickListener(mOnClickListener);
        mBtConfirm.setEnabled(false);
        mPasswordSwitch = (ImageButton) findViewById(R.id.bt_password_switch);
        mPasswordSwitch.setOnClickListener(mOnClickListener);

        mTip = (TextView) findViewById(R.id.tv_validate_tip);


        if (mMode == MODE_CHANGE_PASSWORD) {
            mETPassword.setHint(R.string.input_new_password);
        }
    }

    private void initPresenter() {
        mPresenter = new AccountConfirmPresenter(this, this);
    }

    public void clickPasswordSwitch(View v) {
        passwordOn = !passwordOn;
        if (passwordOn) {
            mPasswordSwitch.setImageResource(R.drawable.ty_password_on);
            mETPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            mPasswordSwitch.setImageResource(R.drawable.ty_password_off);
            mETPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        if (mETPassword.getText().length() > 0) {
            mETPassword.setSelection(mETPassword.getText().length());
        }
    }

    private void resetGetValidateCode() {
        if (!mGetValidateCode.isEnabled()) {
            mGetValidateCode.setEnabled(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String password = mETPassword.getText().toString();

        if (TextUtils.isEmpty(password)) {
            mBtConfirm.setEnabled(false);
        } else {
            mBtConfirm.setEnabled(true);
        }
    }

    @Override
    public String getValidateCode() {
        return mValidateCode.getText().toString();
    }

    @Override
    public String getPassword() {
        return mETPassword.getText().toString();
    }

    @Override
    public String getCompanyName() {
        return mETCompanyName.getText().toString();
    }

    @Override
    public void setCountdown(int sec) {
        mGetValidateCode.setText(getString(R.string.reget_validation_second, sec));
    }

    @Override
    public void enableGetValidateCode() {
        mGetValidateCode.setText(R.string.login_reget_code);
    }

    @Override
    public void disableGetValidateCode() {
        if (mGetValidateCode.isEnabled()) {
            mGetValidateCode.setEnabled(false);
        }
    }

    @Override
    public int getMode() {
        return mMode;
    }

    @Override
    public int getPlatform() {
        return mPlatform;
    }

    @Override
    public void modelResult(int what, Result result) {
        switch (what) {
            case AccountConfirmPresenter.MSG_REGISTER_FAIL:
            case AccountConfirmPresenter.MSG_RESET_PASSWORD_FAIL:
            case AccountConfirmPresenter.MSG_SEND_VALIDATE_CODE_ERROR:
            case AccountConfirmPresenter.MSG_LOGIN_FAIL:
                // resetGetValidateCode();
                ToastUtil.shortToast(this, result.error);
                break;

            case AccountConfirmPresenter.MSG_SEND_VALIDATE_CODE_SUCCESS:
                disableGetValidateCode();
                mValidateCode.requestFocus();
                break;
        }
    }

    @Override
    public void checkValidateCode() {
        if (!mPresenter.isSended()) {
            resetGetValidateCode();
        }
    }

    @Override
    public boolean needLogin() {
        return false;
    }

    @Override
    public void setValidateTip(Spanned tip) {
        mTip.setText(tip);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }
}
