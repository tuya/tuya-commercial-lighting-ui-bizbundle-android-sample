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
import android.content.Intent;
import android.text.TextUtils;

import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.commercial.lighting.demo.utils.CountryUtils;
import com.tuya.smart.commercial.lighting.demo.pages.login.view.IAccountInputView;
import com.tuya.smart.commercial.lighting.demo.pages.login.view.AccountConfirmActivity;
import com.tuya.smart.commercial.lighting.demo.pages.login.view.AccountInputActivity;
import com.tuya.smart.commercial.lighting.demo.pages.login.view.CountryListActivity;
import com.tuya.smart.sdk.TuyaSdk;

public class AccountInputPresenter extends BasePresenter {
    private static final int REQUEST_COUNTRY_CODE = 998;
    private static final int REQUEST_CONFIRM = 999;
    private IAccountInputView mView;

    private Activity mContext;

    private String mCountryName;
    private String mCountryCode;


    public AccountInputPresenter(Activity context, IAccountInputView iAccountInputView) {
        mContext = context;
        mView = iAccountInputView;

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
        mView.setCountryInfo(mCountryName, mCountryCode);
    }

    public void selectCountry() {
        mContext.startActivityForResult(new Intent(mContext, CountryListActivity.class), REQUEST_COUNTRY_CODE);
    }

    public void gotoNext(int accountType) {
        int mode = AccountConfirmActivity.MODE_REGISTER;
        if (AccountInputActivity.MODE_PASSWORD_FOUND == mView.getMode()) {
            mode = AccountConfirmActivity.MODE_FORGET_PASSWORD;
        }

        AccountConfirmActivity.gotoAccountConfirmActivityForResult(mContext, mView.getAccount(), mCountryCode, mode, accountType, REQUEST_CONFIRM);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_COUNTRY_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    mCountryName = data.getStringExtra(CountryListActivity.COUNTRY_NAME);
                    mCountryCode = data.getStringExtra(CountryListActivity.PHONE_CODE);
                    mView.setCountryInfo(mCountryName, mCountryCode);
                }
                break;

            case REQUEST_CONFIRM:
                if (resultCode == Activity.RESULT_OK) {
                    mContext.finish();
                }
                break;
        }
    }
}
