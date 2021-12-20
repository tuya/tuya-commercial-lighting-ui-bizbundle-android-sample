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
import android.os.Message;
import android.text.TextUtils;

import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.android.user.bean.TuyaMerchantBean;
import com.tuya.smart.android.user.bean.TuyaUserLoginWithPwdReqBean;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.android.user.callback.ITuyaUserLoginCallback;
import com.tuya.smart.commercial.lighting.demo.app.Constant;
import com.tuya.smart.commercial.lighting.demo.pages.login.view.CountryListActivity;
import com.tuya.smart.commercial.lighting.demo.pages.login.view.ILoginView;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;
import com.tuya.smart.commercial.lighting.demo.utils.CountryUtils;
import com.tuya.smart.commercial.lighting.demo.utils.LoginHelper;
import com.tuya.smart.commercial.lighting.demo.utils.MessageUtil;
import com.tuya.sdk.os.TuyaOSUser;
import com.tuya.smart.sdk.TuyaSdk;

import java.util.List;


public class LoginPresenter extends BasePresenter {
    protected Activity mContext;
    protected ILoginView mView;

    private String mCountryName;
    private String mCountryCode;

    public static final int MSG_LOGIN_SUCCESS = 15;
    public static final int MSG_LOGIN_FAILURE = 16;

    public LoginPresenter(Context context, ILoginView view) {
        super();
        mContext = (Activity) context;
        mView = view;
        initCountryInfo();
    }

    private void initCountryInfo() {
        String countryKey = CountryUtils.getCountryKey(TuyaSdk.getApplication());
        if (!TextUtils.isEmpty(countryKey)) {
            mCountryName = CountryUtils.getCountryTitle(countryKey);
            mCountryCode = CountryUtils.getCountryNum(countryKey);
        } else {
            countryKey = CountryUtils.getCountryDefault(TuyaSdk.getApplication());
            mCountryName = CountryUtils.getCountryTitle(countryKey);
            mCountryCode = CountryUtils.getCountryNum(countryKey);
        }

        mView.setCountry(mCountryName, mCountryCode);
    }

    public void selectCountry() {
        mContext.startActivityForResult(new Intent(mContext, CountryListActivity.class), 0x01);
    }

    public void login(String userName, String password) {
        TuyaUserLoginWithPwdReqBean loginWithPwdReqBean = new TuyaUserLoginWithPwdReqBean();
        loginWithPwdReqBean.setUsername(userName);
        loginWithPwdReqBean.setPassword(password);
        loginWithPwdReqBean.setCountryCode(mCountryCode);
        TuyaOSUser.getUserInstance().loginWithPassword(loginWithPwdReqBean, new ITuyaUserLoginCallback<User>() {
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
                Message msg = MessageUtil.getCallFailMessage(MSG_LOGIN_FAILURE, s, s1);
                mHandler.sendMessage(msg);
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_LOGIN_SUCCESS:
                mView.modelResult(msg.what, null);
                Constant.finishActivity();
                LoginHelper.afterLogin();
                ActivityUtils.gotoHomeActivity(mContext);
                break;
            case MSG_LOGIN_FAILURE:
                mView.modelResult(msg.what, (Result) msg.obj);
                break;
            default:
                break;
        }

        return super.handleMessage(msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0x01:
                if (resultCode == Activity.RESULT_OK) {
                    mCountryName = data.getStringExtra(CountryListActivity.COUNTRY_NAME);
                    mCountryCode = data.getStringExtra(CountryListActivity.PHONE_CODE);
                    mView.setCountry(mCountryName, mCountryCode);
                }
                break;
        }
    }
}
