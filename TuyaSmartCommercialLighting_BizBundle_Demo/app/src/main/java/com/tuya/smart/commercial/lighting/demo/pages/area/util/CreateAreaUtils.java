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

package com.tuya.smart.commercial.lighting.demo.pages.area.util;


import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.utils.ToastUtil;
import com.tuya.smart.commercial.lighting.demo.utils.UIFactory;

import static com.tuya.smart.commercial.lighting.demo.pages.area.view.AreaIndexActivity.PROJECT_INDOOR;

public class CreateAreaUtils {

    public static final double longitude = 120.000000;
    public static final double latitude = 80.000000;
    public static final String address = "Wuchanggang Road, Xihu District";

    /**
     * Create New Area
     *
     * @param context
     * @param listener
     */
    public static void createNewDialog(Context context, String levelHint, int projectType, final AreaInputDialogInterface listener) {
        final LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.cl_dialog_create_new_area, null);
        EditText etAreaName = view.findViewById(R.id.dialog_area_name_input);
        EditText etAreaLevel = view.findViewById(R.id.dialog_area_level_input);
        TextView tvLevelTip = view.findViewById(R.id.tv_area_level_tip);
        LinearLayout llOutdoorAddress = view.findViewById(R.id.ll_area_address);
        String contextString = context.getString(R.string.cl_area_level);
        tvLevelTip.setText(contextString + ":" + levelHint);
        if (projectType == PROJECT_INDOOR) {
            llOutdoorAddress.setVisibility(View.GONE);
        }
        DialogInterface.OnClickListener onClickListener = (dialog1, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (listener != null) {
                        String areaName = etAreaName.getEditableText().toString();
                        String areaLevel = etAreaLevel.getEditableText().toString();
                        if (checkParams(context, "null", areaName, areaLevel)) {
                            listener.onPositive(dialog1, 0, areaName, Integer.parseInt(areaLevel), longitude, latitude, address);
                        }
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    if (listener != null) {
                        listener.onNegative(dialog1);
                    }
                    break;
                default:
                    break;
            }
        };
        showDialog(context, view, onClickListener);
    }

    public static void createAreaDialog(Context context, int projectType, final AreaInputDialogInterface listener) {
        final LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.cl_dialog_create_area, null);
        EditText etAreaName = view.findViewById(R.id.dialog_area_name_input);
        LinearLayout llOutdoorAddress = view.findViewById(R.id.ll_area_address);
        if (projectType == PROJECT_INDOOR) {
            llOutdoorAddress.setVisibility(View.GONE);
        }
        DialogInterface.OnClickListener onClickListener = (dialog1, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (listener != null) {
                        String areaName = etAreaName.getEditableText().toString();
                        if (checkParams(context, "0", areaName, "null")) {
                            listener.onPositive(dialog1, 0, areaName, 1, longitude, latitude, address);
                        }
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    if (listener != null) {
                        listener.onNegative(dialog1);
                    }
                    break;
                default:
                    break;
            }
        };
        showDialog(context, view, onClickListener);
    }

    private static void showDialog(Context context, View view, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dialog = UIFactory.buildAlertDialog(context);
        dialog.setNegativeButton(R.string.ty_cancel, listener);
        dialog.setPositiveButton(R.string.ty_confirm, listener);
        dialog.setView(view);
        dialog.setCancelable(false);
        dialog.create().show();
    }

    public interface AreaInputDialogInterface {

        void onPositive(DialogInterface dialog, long areaId, String areaName, int areaLevel, double longitude, double latitude, String address);

        void onNegative(DialogInterface dialog);
    }

    private static Boolean checkParams(Context context, String id, String name, String level) {
        if (id.isEmpty()) {
            ToastUtil.shortToast(context, "area id can not be null");
            return false;
        } else if (name.isEmpty()) {
            ToastUtil.shortToast(context, "area name can not be null");
            return false;
        } else if (level.isEmpty()) {
            ToastUtil.shortToast(context, "area level can not be null");
            return false;
        } else {
            return true;
        }
    }
}
