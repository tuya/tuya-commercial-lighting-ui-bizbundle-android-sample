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
import android.graphics.Typeface;

import androidx.appcompat.app.AlertDialog;

import com.tuya.smart.android.demo.R;


public class UIFactory {

    public static AlertDialog.Builder buildAlertDialog(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.Dialog_Alert);
        return dialog;
    }

    public static AlertDialog.Builder buildSmartAlertDialog(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.Dialog_Alert_NoTitle);
        return dialog;
    }

    public static AlertDialog.Builder buildMultichoiceAlertDialog(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.Dialog_Alert_Multichoice);
        return dialog;
    }

    public static AlertDialog.Builder buildSinglechoiceAlertDialog(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.Dialog_Alert_Singlechoice);
        return dialog;
    }

    private static Typeface mFontTtf;

    public static Typeface getFontTtf(Context ctx) {
        if (null == mFontTtf) {
            mFontTtf = Typeface.createFromAsset(ctx.getAssets(), "font/iconfont.ttf");
        }
        return mFontTtf;
    }

}
