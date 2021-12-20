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

package com.tuya.smart.commercial.lighting.demo.pages.config;

import android.text.TextUtils;

import com.tuya.smart.android.device.utils.WiFiUtil;
import com.tuya.smart.sdk.TuyaSdk;

public class BindDeviceUtils {

    public static boolean isAPMode() {
        String ssid = "";
        if (TextUtils.isEmpty(ssid)) {
            ssid = CommonConfig.DEFAULT_COMMON_AP_SSID;
        }
        String currentSSID = WiFiUtil.getCurrentSSID(TuyaSdk.getApplication()).toLowerCase();
        return !TextUtils.isEmpty(currentSSID) && (currentSSID.startsWith(ssid.toLowerCase()) ||
                currentSSID.startsWith(CommonConfig.DEFAULT_OLD_AP_SSID.toLowerCase()) ||
                currentSSID.contains(CommonConfig.DEFAULT_KEY_AP_SSID.toLowerCase()));
    }
}
