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

package com.tuya.smart.commercial.lighting.demo.app.base;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.tuya.smart.android.demo.R;


public abstract class BaseFragment extends Fragment {

    protected Toolbar mToolBar;

    protected void initToolbar(View contentView) {
        if (mToolBar == null) {
            mToolBar = (Toolbar) contentView.findViewById(R.id.toolbar_top_view);
            if (mToolBar != null) {
                TypedArray a = getActivity().obtainStyledAttributes(new int[]{
                        R.attr.status_font_color});
                int titleColor = a.getInt(0, Color.WHITE);
                mToolBar.setTitleTextColor(titleColor);
            }
        }
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }

    protected void setTitle(String title) {
        if (mToolBar != null) {
            mToolBar.setTitle(title);
        }
    }

    protected void setSubTitle(String title) {
        if (mToolBar != null) {
            mToolBar.setSubtitle(title);
        }
    }

    protected void setLogo(Drawable logo) {
        if (mToolBar != null) {
            mToolBar.setLogo(logo);
        }
    }

    protected void setNavigationIcon(Drawable logo) {
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(logo);
        }
    }

    protected void setMenu(int resId, Toolbar.OnMenuItemClickListener listener) {
        if (mToolBar != null) {
            mToolBar.inflateMenu(resId);
            mToolBar.setOnMenuItemClickListener(listener);
        }
    }

    protected void setDisplayHomeAsUpEnabled() {
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(R.drawable.tysmart_back);
            mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }
    }

    protected void setDisplayHomeAsUpEnabled(final View.OnClickListener listener) {
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(R.drawable.tysmart_back);
            mToolBar.setNavigationOnClickListener(listener);
        }
    }

    protected void hideToolBarView() {
        if (mToolBar != null && mToolBar.isShown()) {
            mToolBar.setVisibility(View.GONE);
        }
    }

    protected void showToolBarView() {
        if (mToolBar != null && !mToolBar.isShown()) {
            mToolBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mToolBar = null;
    }
}
