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

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;


public class ProgressUtil {

    private static Dialog progressDialog;

    public static void showLoading(Context context, String message) {
        if (progressDialog == null) {
            progressDialog = getSimpleProgressDialog(context, "", new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    progressDialog = null;
                }
            });
        }
        if (!TextUtils.isEmpty(message)) {
            ((TextView) progressDialog.findViewById(R.id.progress_dialog_message)).setText(message);
        }
        if (!isShowLoading()) {
            progressDialog.show();
        }
    }

    public static void setAlpha(float dimAmount, float alpha) {
        WindowManager.LayoutParams lp = progressDialog.getWindow().getAttributes();
        lp.dimAmount = dimAmount;
        lp.alpha = alpha;
        progressDialog.getWindow().setAttributes(lp);
        progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }


    public static void showLoading(Context context, CharSequence title, CharSequence message) {
        showLoading(context, title, message, false);
    }

    public static void showLoading(Context context, CharSequence title, int resId) {
        showLoading(context, title, resId, false);

    }

    public static void showLoading(Context context, CharSequence title, CharSequence message, boolean isCancelable) {
        showLoading(context, title, message, isCancelable, false, null);
    }

    public static void showLoading(Context context, CharSequence title, int resId, boolean isCancelable) {
        showLoading(context, title, resId, isCancelable, false, null);
    }

    public static void showLoading(Context context, CharSequence title, CharSequence message, boolean isCancelable, DialogInterface.OnCancelListener listener) {
        showLoading(context, title, message, isCancelable, false, listener);
    }

    public static void showLoading(Context context, CharSequence title, int resId, boolean isCancelable, DialogInterface.OnCancelListener listener) {
        showLoading(context, title, resId, isCancelable, false, listener);
    }

    public static void showLoading(Context context, CharSequence title, int resId, boolean isCancelable, boolean isCancelOnTouchOutside, DialogInterface.OnCancelListener listener) {
        if (context == null) return;
        String message = context.getResources().getString(resId);
        showLoading(context, title, message, isCancelable, isCancelOnTouchOutside, listener);
    }

    public static void showLoading(Context context, CharSequence title, CharSequence message, boolean isCancelable, boolean isCancelOnTouchOutside, DialogInterface.OnCancelListener listener) {
        if (context == null) return;
        if (progressDialog != null) hideLoading();
        progressDialog = ProgressDialog.show(context, title, message, false, isCancelable, listener);
        progressDialog.setCanceledOnTouchOutside(isCancelOnTouchOutside);
    }

    public static void showLoading(Context context, int resId) {
        showLoading(context, context.getString(resId));
    }

    public static boolean isShowLoading() {
        if (progressDialog == null) {
            return false;
        }
        return progressDialog.isShowing();
    }

    public static void hideLoading() {
        if (progressDialog != null && progressDialog.getContext() != null) {
            progressDialog.hide();
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        progressDialog = null;
    }

    public static ProgressDialog getProgressDialog(Context mContext, String title, String msg) {
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        if (!TextUtils.isEmpty(title)) {
            progressDialog.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            progressDialog.setMessage(msg);
        }
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.progress_normal);
        progressDialog.setProgressDrawable(drawable);
        progressDialog.setIndeterminate(false);
        return progressDialog;
    }

    public static Dialog getSimpleProgressDialog(Context mContext, String msg) {
        return getSimpleProgressDialog(mContext, msg, null);
    }

    public static Dialog getSimpleProgressDialog(Context mContext, String msg, DialogInterface.OnCancelListener listener) {
        Dialog dialog = new Dialog(mContext, R.style.TY_Progress_Dialog);
        dialog.setContentView(R.layout.ty_progress_dialog_h);
        if (!TextUtils.isEmpty(msg)) {
            ((TextView) dialog.findViewById(R.id.progress_dialog_message)).setText(msg);
        }
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        if (listener != null) {
            dialog.setOnCancelListener(listener);
        }
        return dialog;
    }


}
