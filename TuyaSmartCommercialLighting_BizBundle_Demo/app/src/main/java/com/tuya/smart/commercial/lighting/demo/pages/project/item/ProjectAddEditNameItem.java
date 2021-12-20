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

package com.tuya.smart.commercial.lighting.demo.pages.project.item;

import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.widget.recyclerview.item.BaseItem;
import com.tuya.smart.commercial.lighting.demo.widget.recyclerview.item.BaseViewHolder;

import butterknife.BindView;

public class ProjectAddEditNameItem extends BaseItem<String> {
    @BindView(R.id.recycler_project_add_edit)
    EditText editText;
    @BindView(R.id.recycler_project_add_title)
    TextView textView;

    private static final int EDIT_VIEW_TYPE = 10;

    private String mTitleName;

    public ProjectAddEditNameItem(String title) {
        super("");
        this.mTitleName = title;
    }

    @Override
    public int getViewType() {
        return EDIT_VIEW_TYPE;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.cl_recylcer_project_add_item;
    }

    @Override
    public void onReleaseViews(BaseViewHolder holder, int sectionKey, int sectionViewPosition) {
    }

    @Override
    public void onSetViewsData(BaseViewHolder holder, int sectionKey, int sectionViewPosition) {
        textView.setText(mTitleName);
    }

    public void setEditTypeNumber() {
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    public void setEditCanEdit(boolean enable) {
        editText.setEnabled(enable);
    }

    public String getEditText() {
        if (null == editText) {
            return "";
        }
        String text = editText.getText().toString();
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        return text.trim();
    }

    public String getTitleName() {
        return mTitleName;
    }
}
