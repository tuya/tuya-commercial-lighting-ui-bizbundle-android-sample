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

package com.tuya.smart.commercial.lighting.demo.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.tuya.smart.android.base.bean.CountryBean;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.common.utils.TyCommonUtil;
import com.tuya.smart.commercial.lighting.demo.widget.contact.ContactItemInterface;
import com.tuya.smart.commercial.lighting.demo.bean.CountryViewBean;
import com.tuya.smart.sdk.TuyaSdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class CountryUtils {
    public static final HashMap<String, String> mIdWithNumHashMap = new HashMap<>();
    public static final HashMap<String, String> mIdWithEngHashMap = new HashMap<>();
    public static final HashMap<String, String> mIdWithChiHashMap = new HashMap<>();
    public static final HashMap<String, String> mIdWithPinHashMap = new HashMap<>();
    public static final String TAG = "CountryData";
    public static boolean isPutInMap = false;

    public static void initCountryData(final CountryDataGetListener listener) {
        putCountryDataToMap(CommonUtil.getDefaultCountryData());
        if (listener != null) {
            listener.onSuccess();
        }
    }

    public static List<ContactItemInterface> getSampleContactList() {
        if (!isPutInMap) {
            isPutInMap = true;
            putCountryDataToMap(CommonUtil.getDefaultCountryData());
        }
        List<ContactItemInterface> list = new ArrayList<ContactItemInterface>();
        Iterator iter = null;
        if (TyCommonUtil.isZh(TuyaSdk.getApplication())) {
            iter = mIdWithChiHashMap.entrySet().iterator();
        } else {
            iter = mIdWithEngHashMap.entrySet().iterator();
        }
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) iter.next();
            String key = entry.getKey();
            String val = entry.getValue();
            list.add(new CountryViewBean(key, val, mIdWithNumHashMap.get(key), mIdWithPinHashMap.get(key)));
        }
        return list;
    }

    public static void putCountryDataToMap(ArrayList<CountryBean> countryDatas) {
        int size = countryDatas.size();
        L.d(TAG, "size" + size);
        for (int i = 0; i < size; i++) {
            CountryBean country = countryDatas.get(i);
            String key = country.getAbbr();
            mIdWithChiHashMap.put(key, country.getChinese());
            mIdWithEngHashMap.put(key, country.getEnglish());
            mIdWithNumHashMap.put(key, country.getCode());
            mIdWithPinHashMap.put(key, country.getSpell());
        }
    }

    public static String getCountryNum(String key) {
        if (!isPutInMap) {
            isPutInMap = true;
            putCountryDataToMap(CommonUtil.getDefaultCountryData());
        }

        if (TextUtils.isEmpty(key)) return "";

        return mIdWithNumHashMap.get(key);
    }

    public static String getCountryTitle(String key) {
        if (!isPutInMap) {
            isPutInMap = true;
            putCountryDataToMap(CommonUtil.getDefaultCountryData());
        }

        if (TextUtils.isEmpty(key)) return "";

        if (TyCommonUtil.isZh(TuyaSdk.getApplication())) {
            return mIdWithChiHashMap.get(key);
        } else {
            return mIdWithEngHashMap.get(key);
        }
    }

    public static String getCountryKey(Context context) {
        String countryKey = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            countryKey = tm.getNetworkCountryIso().toUpperCase();
        } catch (Exception e) {
        }

        return countryKey;
    }

    public static String getCountryDefault(Context context) {
        TimeZone tz = TimeZone.getDefault();
        String id = tz.getID();
        if (TextUtils.equals(id, "Asia/Shanghai")) {
            return "CN";
        }
        if (id.startsWith("Europe")) {
            return "DE";
        } else {
            return "US";
        }
//        if (TextUtils.isEmpty(countryKey)) {
//            countryKey = Locale.getDefault().getCountry().toUpperCase();
//        }
    }

    public interface CountryDataGetListener {
        public void onSuccess();
    }
}
