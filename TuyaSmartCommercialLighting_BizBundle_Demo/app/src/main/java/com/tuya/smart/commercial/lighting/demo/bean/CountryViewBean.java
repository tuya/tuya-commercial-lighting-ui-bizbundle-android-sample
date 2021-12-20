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

package com.tuya.smart.commercial.lighting.demo.bean;

import com.tuya.smart.android.common.utils.TyCommonUtil;
import com.tuya.smart.commercial.lighting.demo.widget.contact.ContactItemInterface;
import com.tuya.smart.sdk.TuyaSdk;

public class CountryViewBean implements ContactItemInterface {

    private String countryName;
    private String key;
    private String number;
    private String pinyin;


    private boolean isChinese;

    public CountryViewBean(String key, String countryName, String number, String pinyin) {
        super();
        this.countryName = countryName;
        this.key = key;
        this.number = number;
        this.pinyin = pinyin;
        isChinese = TyCommonUtil.isZh(TuyaSdk.getApplication());
    }

    // index the list by nickname
    @Override
    public String getItemForIndex() {
        if (isChinese) return pinyin;
        else return countryName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public boolean isChinese() {
        return isChinese;
    }

    public void setChinese(boolean isChinese) {
        this.isChinese = isChinese;
    }
}
