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

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.tuya.smart.commercial.lighting.demo.app.TuyaSmartApp;

import java.lang.reflect.Field;

public class SizeUtils {

    private SizeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static int dp2px(float dpValue) {

        float scale = TuyaSmartApp.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(float pxValue) {
        float scale = TuyaSmartApp.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(float spValue) {
        float fontScale = TuyaSmartApp.getAppContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(float pxValue) {
        float fontScale = TuyaSmartApp.getAppContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static float applyDimension(int unit, float value, DisplayMetrics metrics) {
        switch (unit) {
            case TypedValue.COMPLEX_UNIT_PX:
                return value;
            case TypedValue.COMPLEX_UNIT_DIP:
                return value * metrics.density;
            case TypedValue.COMPLEX_UNIT_SP:
                return value * metrics.scaledDensity;
            case TypedValue.COMPLEX_UNIT_PT:
                return value * metrics.xdpi * (1.0f / 72);
            case TypedValue.COMPLEX_UNIT_IN:
                return value * metrics.xdpi;
            case TypedValue.COMPLEX_UNIT_MM:
                return value * metrics.xdpi * (1.0f / 25.4f);
        }
        return 0;
    }

    public static void forceGetViewSize(final View view, final onGetSizeListener listener) {
        view.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onGetSize(view);
                }
            }
        });
    }

    public interface onGetSizeListener {
        void onGetSize(View view);
    }

    public static int[] measureView(View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
        int widthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int lpHeight = lp.height;
        int heightSpec;
        if (lpHeight > 0) {
            heightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
        } else {
            heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(widthSpec, heightSpec);
        return new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};
    }
    public static int getMeasuredWidth(View view) {
        return measureView(view)[0];
    }

    public static int getMeasuredHeight(View view) {
        return measureView(view)[1];
    }

    public static int getWidth() {
        int width = TuyaSmartApp.getAppContext().getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    public static int getHeight() {
        int height = TuyaSmartApp.getAppContext().getResources().getDisplayMetrics().heightPixels;
        return height;
    }


    public static int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = TuyaSmartApp.getAppContext().getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }


    public static int getNavigationBarHeight() {
        Resources resources = TuyaSmartApp.getAppContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }


}
