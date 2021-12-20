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
import android.widget.Toast;

public class ToastUtil {

    private static Toast longToast, shortToast;

    public static synchronized void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

    public static synchronized void showToast(Context context, String tips) {
        if (longToast == null) {
            longToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        }

        longToast.setText(tips);
        longToast.show();
    }

    public static synchronized void shortToast(Context context, String tips) {
        if (shortToast == null) {
            shortToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        shortToast.setText(tips);
        shortToast.show();
    }

    public static synchronized void shortToast(Context context, int tipsResId) {
        showToast(context, context.getString(tipsResId));
    }

}
