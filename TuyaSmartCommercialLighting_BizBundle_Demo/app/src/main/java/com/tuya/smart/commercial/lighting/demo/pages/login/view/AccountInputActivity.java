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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tuya.smart.android.common.utils.ValidatorUtil;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;
import com.tuya.smart.commercial.lighting.demo.utils.ToastUtil;
import com.tuya.smart.commercial.lighting.demo.pages.login.presenter.AccountInputPresenter;

public class AccountInputActivity extends BaseActivity implements TextWatcher, IAccountInputView {

    public static final String EXTRA_ACCOUNT_INPUT_MODE = "extra_account_input_mode";
    public static final int MODE_REGISTER = 0;
    public static final int MODE_PASSWORD_FOUND = 1;

    private int mMode = 0;

    private AccountInputPresenter mPresenter;

    private int mAccountType = AccountConfirmActivity.PLATFORM_PHONE;
    private TextView mCountryName;
    private EditText mEtAccount;
    private Button mNextStepBtn;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bt_next) {
                if (mAccountType == AccountConfirmActivity.PLATFORM_PHONE
                        && mCountryName.getText().toString().contains("+86")
                        && mEtAccount.getText().length() != 11) {
                    ToastUtil.shortToast(AccountInputActivity.this, getString(R.string.ty_phone_num_error));
                    return;
                }
                if (mAccountType == AccountConfirmActivity.PLATFORM_EMAIL) {
                    mPresenter.gotoNext(mAccountType);
                } else if (mAccountType == AccountConfirmActivity.PLATFORM_PHONE) {
                    mPresenter.gotoNext(mAccountType);
                }

            } else if (v.getId() == R.id.country_name) {
                mPresenter.selectCountry();
            }
        }
    };

    public static void gotoAccountInputActivity(Activity activity, int mode, int requestCode) {
        Intent intent = new Intent(activity, AccountInputActivity.class);
        intent.putExtra(EXTRA_ACCOUNT_INPUT_MODE, mode);
        ActivityUtils.startActivityForResult(activity, intent, requestCode, 0, false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_input);
        initData();
        initToolbar();
        initTitle();
        initView();
        initPresenter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    private void initData() {
        mMode = getIntent().getIntExtra(EXTRA_ACCOUNT_INPUT_MODE, MODE_REGISTER);
    }

    private void initPresenter() {
        mPresenter = new AccountInputPresenter(this, this);
    }

    private void initView() {
        mCountryName = (TextView) findViewById(R.id.country_name);
        mCountryName.setOnClickListener(mOnClickListener);
        mNextStepBtn = (Button) findViewById(R.id.bt_next);
        mNextStepBtn.setOnClickListener(mOnClickListener);
        mNextStepBtn.setEnabled(false);
        mEtAccount = (EditText) findViewById(R.id.et_account);
        mEtAccount.addTextChangedListener(this);
    }

    private void initTitle() {
        switch (mMode) {
            case MODE_REGISTER:
                setTitle(R.string.ty_login_register);
                break;

            case MODE_PASSWORD_FOUND:
                setTitle(R.string.ty_login_forget_keyword_find);
                break;
        }
        setDisplayHomeAsUpEnabled();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String userName = mEtAccount.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            mNextStepBtn.setEnabled(false);
        } else {
            if (ValidatorUtil.isEmail(userName)) {
                mAccountType = AccountConfirmActivity.PLATFORM_EMAIL;
                mNextStepBtn.setEnabled(true);
            } else {
                try {
                    Long.valueOf(userName);
                    mAccountType = AccountConfirmActivity.PLATFORM_PHONE;
                    mNextStepBtn.setEnabled(true);
                } catch (Exception e) {
                    mNextStepBtn.setEnabled(false);
                }
            }
        }
    }

    @Override
    public void setCountryInfo(String countryName, String countryNum) {
        mCountryName.setText(String.format("%s +%s", countryName, countryNum));
    }

    @Override
    public String getAccount() {
        return mEtAccount.getText().toString();
    }

    @Override
    public int getMode() {
        return mMode;
    }

    @Override
    public boolean needLogin() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }
}
