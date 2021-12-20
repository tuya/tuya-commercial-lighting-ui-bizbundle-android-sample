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
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.tuya.smart.android.demo.R;


public class DialogUtil {

    public static void simpleSmartDialog(Context context, CharSequence msg,
                                         final DialogInterface.OnClickListener listener) {
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onClick(dialog, which);
                }
            }
        };
        AlertDialog.Builder dialog = UIFactory.buildSmartAlertDialog(context);
        dialog.setPositiveButton(R.string.ty_confirm, onClickListener);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public static void simpleSmartDialog(Context context, int msgResId,
                                         final DialogInterface.OnClickListener listener) {
        simpleSmartDialog(context, context.getString(msgResId), listener);
    }

    public static void simpleConfirmDialog(Context context, CharSequence msg,
                                           final DialogInterface.OnClickListener listener) {
        simpleConfirmDialog(context, context.getString(R.string.ty_simple_confirm_title), msg,
                listener);
    }

    public static void simpleConfirmDialog(Context context, String title, CharSequence msg,
                                           final DialogInterface.OnClickListener listener) {
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onClick(dialog, which);
                }
            }
        };
        AlertDialog.Builder dialog = UIFactory.buildAlertDialog(context);
        dialog.setNegativeButton(R.string.ty_cancel, onClickListener);
        dialog.setPositiveButton(R.string.ty_confirm, onClickListener);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.create().show();
    }

    public static void simpleTipDialog(Context context, CharSequence msg,
                                       final DialogInterface.OnClickListener listener) {
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onClick(dialog, which);
                }
            }
        };
        AlertDialog.Builder dialog = UIFactory.buildAlertDialog(context);
        dialog.setPositiveButton(R.string.ty_confirm, onClickListener);
        dialog.setTitle(context.getString(R.string.ty_simple_confirm_title));
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.create().show();
    }

    public static void simpleInputDialog(Context context, String title, CharSequence text,
                                         boolean isHint,
                                         final SimpleInputDialogInterface listener) {
        AlertDialog.Builder dialog = UIFactory.buildAlertDialog(context);
        final EditText inputEditText = (EditText) LayoutInflater.from(context).inflate(
                R.layout.ty_dialog_simple_input, null);
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (listener != null) {
                            listener.onPositive(dialog, inputEditText.getEditableText().toString());
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        if (listener != null) {
                            listener.onNegative(dialog);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        dialog.setNegativeButton(R.string.ty_cancel, onClickListener);
        dialog.setPositiveButton(R.string.ty_confirm, onClickListener);
        dialog.setTitle(title);
        if (!TextUtils.isEmpty(text)) {
            if (isHint) {
                inputEditText.setHint(text);
            } else {
                inputEditText.setText(text);
            }
        }
        dialog.setView(inputEditText);
        inputEditText.requestFocus();
        dialog.setCancelable(false);
        dialog.create().show();
    }

    public static void simpleInputDialog(Context context, String title, String message, CharSequence text,
                                         boolean isHint,
                                         final SimpleInputDialogInterface listener) {
        AlertDialog.Builder dialog = UIFactory.buildAlertDialog(context);

        final EditText inputEditText = (EditText) LayoutInflater.from(context).inflate(
                R.layout.ty_dialog_simple_input, null);
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (listener != null) {
                            listener.onPositive(dialog, inputEditText.getEditableText().toString());
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        if (listener != null) {
                            listener.onNegative(dialog);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        dialog.setNegativeButton(R.string.ty_cancel, onClickListener);
        dialog.setPositiveButton(R.string.ty_confirm, onClickListener);
        dialog.setTitle(title);
        if (!TextUtils.isEmpty(text)) {
            if (isHint) {
                inputEditText.setHint(text);
            } else {
                inputEditText.setText(text);
            }
        }

        dialog.setView(inputEditText);
        inputEditText.requestFocus();
        dialog.setCancelable(false);
        dialog.create().show();
    }

    public interface SimpleInputDialogInterface {

        public void onPositive(DialogInterface dialog, String inputText);

        public void onNegative(DialogInterface dialog);
    }

    public static void listSelectDialog(Context context, String title, String[] items,
                                        final AdapterView.OnItemClickListener listener) {
        customerListSelectDialog(context, title, new ArrayAdapter<String>(context,
                R.layout.ty_simple_list_item_1,
                items), listener);
    }

    public static void listSelectDialogCenter(Context context, String title, String[] items,
                                              final AdapterView.OnItemClickListener listener) {
        customerListSelectDialogTitleCenter(context, title, new ArrayAdapter<String>(context,
                R.layout.ty_simple_list_item_2,
                items), listener);
    }

    public static void customerListSelectDialog(Context context, String title, ListAdapter adapter,
                                                final AdapterView.OnItemClickListener listener) {
        AlertDialog.Builder dialog = UIFactory.buildAlertDialog(context);
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        }
        ListView listView = (ListView) LayoutInflater.from(context).inflate(
                R.layout.ty_dialog_simple_list, null);
        listView.setAdapter(adapter);
        dialog.setView(listView);
        final AlertDialog create = dialog.create();
        create.setCanceledOnTouchOutside(true);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                create.dismiss();
                if (listener != null) {
                    listener.onItemClick(parent, view, position, id);
                }
            }

        };
        listView.setOnItemClickListener(onItemClickListener);
        create.show();
    }

    public static void customerListSelectDialogTitleCenter(Context context, String title, ListAdapter adapter,
                                                           final AdapterView.OnItemClickListener listener) {
        AlertDialog.Builder dialog = UIFactory.buildAlertDialog(context);
        TextView tvTitle = new TextView(context);
        tvTitle.setText(title);
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setHeight(90);
        dialog.setCustomTitle(tvTitle);
        ListView listView = (ListView) LayoutInflater.from(context).inflate(
                R.layout.ty_dialog_simple_list, null);
        listView.setAdapter(adapter);
        dialog.setView(listView);
        final AlertDialog create = dialog.create();
        create.setCanceledOnTouchOutside(true);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                create.dismiss();
                if (listener != null) {
                    listener.onItemClick(parent, view, position, id);
                }
            }

        };
        listView.setOnItemClickListener(onItemClickListener);
        create.show();
    }

    public static void singleChoiceDialog(Context context, String title, String[] items,
                                          int checkedItem,
                                          final SingleChoiceDialogInterface listener) {
        final Integer[] realTimecheckedItem = new Integer[]{
                checkedItem
        };
        AlertDialog.Builder dialog = UIFactory.buildSinglechoiceAlertDialog(context);
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                realTimecheckedItem[0] = which;
            }
        };
        DialogInterface.OnClickListener buttonOnClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            listener.onPositive(dialog, realTimecheckedItem[0]);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            listener.onNegative(dialog);
                            break;
                        default:
                            break;
                    }
                }
            }
        };

        dialog.setTitle(title);
        dialog.setPositiveButton(R.string.ty_confirm, buttonOnClickListener);
        dialog.setNegativeButton(R.string.ty_cancel, buttonOnClickListener);
        dialog.setSingleChoiceItems(items, checkedItem, onClickListener);
        dialog.setCancelable(false);
        dialog.create().show();
    }

    public interface SingleChoiceDialogInterface {

        public void onPositive(DialogInterface dialog, int checkedItem);

        public void onNegative(DialogInterface dialog);
    }

    public static void multiChoiceDialog(Context context, String title, String[] items,
                                         boolean[] checkedItems, final MultiChoiceDialogInterface listener) {
        final boolean[] realTimecheckedItems = checkedItems.clone();
        AlertDialog.Builder dialog = UIFactory.buildMultichoiceAlertDialog(context);
        DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener = new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                realTimecheckedItems[which] = isChecked;
            }
        };
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            listener.onPositive(dialog, realTimecheckedItems);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            listener.onNegative(dialog);
                            break;
                        default:
                            break;
                    }
                }
            }
        };
        dialog.setTitle(title);
        dialog.setPositiveButton(R.string.ty_confirm, onClickListener);
        dialog.setNegativeButton(R.string.ty_cancel, onClickListener);
        dialog.setMultiChoiceItems(items, checkedItems, onMultiChoiceClickListener);
        dialog.setCancelable(false);
        dialog.create().show();
    }

    public interface MultiChoiceDialogInterface {

        public void onPositive(DialogInterface dialog, boolean[] checkedItems);

        public void onNegative(DialogInterface dialog);
    }

    public static AlertDialog customDialog(Context context, String title, CharSequence message,
                                           String positiveButton,
                                           String negativeButton, String neutralButton, DialogInterface.OnClickListener listener
    ) {
        AlertDialog.Builder dialog = UIFactory.buildAlertDialog(context);
        if (!TextUtils.isEmpty(positiveButton)) {
            dialog.setPositiveButton(positiveButton, listener);
        }
        if (!TextUtils.isEmpty(negativeButton)) {
            dialog.setNegativeButton(negativeButton, listener);
        }
        if (!TextUtils.isEmpty(neutralButton)) {
            dialog.setNeutralButton(neutralButton, listener);
        }
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        }
        dialog.setMessage(message);
        return dialog.create();
    }
}
