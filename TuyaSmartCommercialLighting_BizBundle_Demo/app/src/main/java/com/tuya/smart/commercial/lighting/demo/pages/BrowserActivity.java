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

package com.tuya.smart.commercial.lighting.demo.pages;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.appcompat.widget.Toolbar;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.common.utils.TyCommonUtil;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;
import com.tuya.smart.commercial.lighting.demo.utils.CommonUtil;

import java.util.HashMap;
import java.util.Map;

public class BrowserActivity extends BaseActivity {

    private static final String TAG = "Browser";

    public static final String EXTRA_TITLE = "Title";
    public static final String EXTRA_URI = "Uri";
    public static final String EXTRA_TOOLBAR = "Toolbar";
    public static final String EXTRA_LOGIN = "Login";
    public static final String EXTRA_REFRESH = "Refresh";
    public static final String EXTRA_FROM_PANNEL = "from_pannel";

    protected WebView mWebView;

    private boolean needlogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        Intent intent = getIntent();
        initTitleBar(intent);
        String url = intent.getStringExtra(EXTRA_URI);

        needlogin = intent.getBooleanExtra(EXTRA_LOGIN, false);
        boolean refresh = intent.getBooleanExtra(EXTRA_REFRESH, true);

        if (TextUtils.isEmpty(url) || !CommonUtil.checkUrl(url)) {
            url = "about:blank";
        }
        setMenu(R.menu.toolbar_top_refresh, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_refresh) {
                    mWebView.stopLoading();
                    mWebView.reload();
                    return true;
                }
                return false;
            }
        });
        mWebView = (WebView) findViewById(R.id.webview);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept-Language", TyCommonUtil.getLang(this));
        mWebView.loadUrl(url, headers);
    }

    private void initTitleBar(Intent intent) {
        boolean toolbar = intent.getBooleanExtra(EXTRA_TOOLBAR, true);
        String title = intent.getStringExtra(EXTRA_TITLE);
        boolean isFromPannel = intent.getBooleanExtra(EXTRA_FROM_PANNEL, false);

        initToolbar();
        showToolBarView();
        if (TextUtils.isEmpty(title)) {
            TypedArray a = obtainStyledAttributes(new int[]{R.attr.app_name});
            title = a.getString(0);
        }
        setTitle(title);

        if (isFromPannel) {
            setDisplayHomeAsUpEnabled();
            hideTitleBarLine();
            if (mToolBar != null) {
                mToolBar.setTitleTextColor(Color.WHITE);
            }
        } else {
            setDisplayHomeAsUpEnabled();
        }

        if (!toolbar) {
            hideToolBarView();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (null != mWebView) {
            mWebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mWebView) {
            mWebView.onPause();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isFinishing()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((ViewGroup) findViewById(R.id.browser_layout)).removeView(mWebView);
        if (null != mWebView) {
            mWebView.destroy();
            mWebView = null;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            String url = intent.getStringExtra(EXTRA_URI);
            L.d(TAG, "Browser : onNewIntent 2:" + url);
            if (mWebView != null && url != null) {
                mWebView.loadUrl(url);
            }
        }
        super.onNewIntent(intent);
    }

    @Override
    protected boolean onPanelKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mWebView.canGoBack()) {
                return true;
            }
        }
        return false;

    }

    @Override
    public void onBackPressed() {
        if (!mWebView.canGoBack()) {
            super.onBackPressed();
            ActivityUtils.back(this);
        }
    }

    @Override
    public boolean needLogin() {
        return needlogin;
    }
}
