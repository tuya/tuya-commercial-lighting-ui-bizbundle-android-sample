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

package com.tuya.smart.commercial.lighting.demo.pages.login.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.TextUtils;

import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.android.user.bean.TuyaMerchantBean;
import com.tuya.smart.android.user.bean.TuyaUserLoginWithCodeReqBean;
import com.tuya.smart.android.user.bean.TuyaUserSendCodeReqBean;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.android.user.callback.ITuyaUserLoginCallback;
import com.tuya.smart.commercial.lighting.demo.app.Constant;
import com.tuya.smart.commercial.lighting.demo.pages.login.view.CountryListActivity;
import com.tuya.smart.commercial.lighting.demo.pages.login.view.ILoginWithPhoneView;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;
import com.tuya.smart.commercial.lighting.demo.utils.CountryUtils;
import com.tuya.smart.commercial.lighting.demo.utils.LoginHelper;
import com.tuya.smart.commercial.lighting.demo.utils.MessageUtil;
import com.tuya.sdk.os.TuyaOSUser;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.sdk.api.IResultCallback;

import java.util.List;

public class LoginWithPhonePresenter extends BasePresenter {

    private static final String TAG = "LoginWithPhonePresenter";

    protected Activity mContext;

    private static final int GET_VALIDATE_CODE_PERIOD = 60 * 1000;

    protected ILoginWithPhoneView mView;


    private CountDownTimer mCountDownTimer;

    protected String mPhoneCode;

    private String mCountryName;
    protected boolean mSend;

    public LoginWithPhonePresenter(Context context, ILoginWithPhoneView view) {
        super();
        mContext = (Activity) context;
        mView = view;
        getCountry();
    }

    private void getCountry() {
        String countryKey = CountryUtils.getCountryKey(TuyaSdk.getApplication());
        if (!TextUtils.isEmpty(countryKey)) {
            mCountryName = CountryUtils.getCountryTitle(countryKey);
            mPhoneCode = CountryUtils.getCountryNum(countryKey);
        } else {
            countryKey = CountryUtils.getCountryDefault(TuyaSdk.getApplication());
            mCountryName = CountryUtils.getCountryTitle(countryKey);
            mPhoneCode = CountryUtils.getCountryNum(countryKey);
        }
        mView.setCountry(mCountryName, mPhoneCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0x01:
                if (resultCode == Activity.RESULT_OK) {
                    mCountryName = data.getStringExtra(CountryListActivity.COUNTRY_NAME);
                    mPhoneCode = data.getStringExtra(CountryListActivity.PHONE_CODE);
                    mView.setCountry(mCountryName, mPhoneCode);
                }
                break;
        }
    }

    public static final int MSG_SEND_VALIDATE_CODE_SUCCESS = 12;
    public static final int MSG_SEND_VALIDATE_CODE_ERROR = 13;
    public static final int MSG_LOGIN_SUCCESS = 15;
    public static final int MSG_LOGIN_ERROR = 16;

    public void getValidateCode() {
        mSend = true;
        TuyaUserSendCodeReqBean sendCodeReqBean = new TuyaUserSendCodeReqBean();
        sendCodeReqBean.setCodeType(0);
        sendCodeReqBean.setUsername(mView.getPhone());
        sendCodeReqBean.setCountryCode(mPhoneCode);
        TuyaOSUser.getUserInstance().getValidateCode(sendCodeReqBean, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                getValidateCodeFail(code, error);
            }

            @Override
            public void onSuccess() {
                mHandler.sendEmptyMessage(MSG_SEND_VALIDATE_CODE_SUCCESS);
            }
        });
    }

    protected void getValidateCodeFail(String errorCode, String errorMsg) {
        Message msg = MessageUtil.getCallFailMessage(MSG_SEND_VALIDATE_CODE_ERROR, errorCode, errorMsg);
        mHandler.sendMessage(msg);
        mSend = false;
    }

    public void login() {
        String phoneNumber = mView.getPhone();
        String code = mView.getValidateCode();
        TuyaUserLoginWithCodeReqBean loginWithCodeReqBean = new TuyaUserLoginWithCodeReqBean();
        loginWithCodeReqBean.setCode(code);
        loginWithCodeReqBean.setCountryCode(mPhoneCode);
        loginWithCodeReqBean.setUsername(phoneNumber);
        TuyaOSUser.getUserInstance().loginWithVerifyCode(loginWithCodeReqBean, new ITuyaUserLoginCallback<User>() {
            @Override
            public void onPreLogin(List<TuyaMerchantBean> list) {
                //Do nothing because of unique merchant code
            }

            @Override
            public void onSuccess(User user) {
                mHandler.sendEmptyMessage(MSG_LOGIN_SUCCESS);

            }

            @Override
            public void onError(String s, String s1) {
                Message msg = MessageUtil.getCallFailMessage(MSG_LOGIN_ERROR, s, s1);
                mHandler.sendMessage(msg);
            }
        });
    }

    public void selectCountry() {
        mContext.startActivityForResult(new Intent(mContext, CountryListActivity.class), 0x01);
    }

    private void buildCountDown() {
        mCountDownTimer = new Countdown(GET_VALIDATE_CODE_PERIOD, 1000);
        mCountDownTimer.start();
        mView.disableGetValidateCode();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SEND_VALIDATE_CODE_SUCCESS:
                buildCountDown();
                mView.modelResult(msg.what, null);
                break;

            case MSG_SEND_VALIDATE_CODE_ERROR:
                mView.modelResult(msg.what, (Result) msg.obj);
                break;


            case MSG_LOGIN_ERROR:
                mView.modelResult(msg.what, (Result) msg.obj);
                break;

            case MSG_LOGIN_SUCCESS:
                mView.modelResult(msg.what, null);
                loginSuccess();
                break;
        }
        return super.handleMessage(msg);
    }

    private void loginSuccess() {
        Constant.finishActivity();
        LoginHelper.afterLogin();
        ActivityUtils.gotoHomeActivity(mContext);
    }

    @Override
    public void onDestroy() {
        mCountDownTimer = null;
    }

    public void validateBtResume() {
        if (mCountDownTimer != null) {
            mCountDownTimer.onFinish();
        }
    }

    public boolean isSended() {
        return mSend;
    }

    private class Countdown extends CountDownTimer {

        public Countdown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mView.setCountdown((int) (millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            mView.enableGetValidateCode();
            mSend = false;
            mView.checkValidateCode();
        }
    }
}
