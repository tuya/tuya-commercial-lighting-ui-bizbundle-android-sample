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

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.widget.recyclerview.item.BaseFoot;
import com.tuya.smart.commercial.lighting.demo.widget.recyclerview.item.BaseViewHolder;

import butterknife.BindView;

public class ProjectIndexFoot extends BaseFoot<String> {
    @BindView(R.id.project_index_add_txt)
    TextView addTxt;

    @BindView(R.id.tv_content_item)
    TextView contentTv;

    @BindView(R.id.rl_project_index_item)
    RelativeLayout rlItem;

    private String mTitleName;

    public ProjectIndexFoot(String title) {
        super("");
        this.mTitleName = title;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.cl_recycler_project_index_foot;
    }

    @Override
    public void onReleaseViews(BaseViewHolder holder, int sectionKey, int sectionFootPosition) {

    }

    @Override
    public void onSetViewsData(BaseViewHolder holder, int sectionKey, int sectionFootPosition) {
        rlItem.setOnClickListener(mListener);
        addTxt.setText(mTitleName);
    }

    public String getContentName() {
        return contentTv.getText().toString();
    }

    public void setContentName(String content) {
        contentTv.setText(content);
    }

    private View.OnClickListener mListener;

    public void setOnClickListener(View.OnClickListener listener) {
        mListener = listener;
    }
}
