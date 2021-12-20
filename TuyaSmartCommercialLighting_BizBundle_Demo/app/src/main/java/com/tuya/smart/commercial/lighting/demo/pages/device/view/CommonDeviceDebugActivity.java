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

package com.tuya.smart.commercial.lighting.demo.pages.device.view;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONObject;
import com.tuya.smart.android.common.utils.TuyaUtil;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.bean.DpLogBean;
import com.tuya.smart.commercial.lighting.demo.pages.device.adapter.CommonDebugDeviceAdapter;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.utils.DialogUtil;
import com.tuya.smart.commercial.lighting.demo.utils.ToastUtil;
import com.tuya.smart.commercial.lighting.demo.pages.device.presenter.CommonDeviceDebugPresenter;
import com.tuya.smart.android.device.bean.SchemaBean;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CommonDeviceDebugActivity extends BaseActivity implements ICommonDeviceDebugView {

    private CommonDebugDeviceAdapter mCommonDebugDeviceAdapter;
    @BindView(R.id.lv_dp_list)
    public ListView mDpListView;
    private CommonDeviceDebugPresenter mPresenter;

    @BindView(R.id.test_log)
    public TextView mLogView;

    @BindView(R.id.test_scroll)
    public ScrollView testScroll;

    @BindView(R.id.v_off_line)
    public View mOffLineView;

    @BindView(R.id.network_tip)
    public TextView mOffLineTip;
    private Unbinder mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_debug);
        mBind = ButterKnife.bind(this);
        initToolbar();
        initMenu();
        initPresenter();
        initTitle();
        initAdapter();
    }

    private void initPresenter() {
        mPresenter = new CommonDeviceDebugPresenter(this, this);
    }


    private void initMenu() {
        setDisplayHomeAsUpEnabled();
        setMenu(R.menu.toolbar_top_smart_device, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_test_mode:
                        break;
                    case R.id.action_rename:
                        mPresenter.renameDevice();
                        break;
                    case R.id.action_close:
                        finish();
                        break;
                    case R.id.action_resume_factory_reset:
                        mPresenter.resetFactory();
                        break;
                    case R.id.action_unconnect:
                        mPresenter.removeDevice();
                        break;
                }
                return false;
            }
        });
    }

    private void initTitle() {
        setTitle(mPresenter.getTitle());

    }

    private void initAdapter() {
        mCommonDebugDeviceAdapter = new CommonDebugDeviceAdapter(this, mPresenter.getDevBean(), R.layout.list_view_common_debug, new ArrayList<SchemaBean>());
        mCommonDebugDeviceAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onClick(view);
            }
        });
        mCommonDebugDeviceAdapter.setCheckChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mPresenter.onCheckedChanged(compoundButton, b);
            }
        });
        mDpListView.setAdapter(mCommonDebugDeviceAdapter);
        mCommonDebugDeviceAdapter.setData(mPresenter.getSchemaList());

        mOffLineView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    @Override
    public void updateView(String dpStr) {
        JSONObject jsonObject = JSONObject.parseObject(dpStr);
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            mCommonDebugDeviceAdapter.updateViewData(entry.getKey(), entry.getValue());
        }

        mCommonDebugDeviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void logError(String error) {
        showMessage(Color.parseColor("#D0021B"), error);
    }

    private void showMessage(final int color, final String log) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(CommonDeviceDebugActivity.this, log);
            }
        });
    }

    @Override
    public void logSuccess(final String log) {
        showMessage(Color.parseColor("#5fb336"), log);
    }

    public void scroll2Bottom() {
        int offset = mLogView.getMeasuredHeight()
                - testScroll.getMeasuredHeight();
        if (offset < 0) {
            offset = 0;
        }
        testScroll.scrollTo(0, offset);
    }

    @Override
    public void logSuccess(DpLogBean logBean) {
        String msg = "\n";
        msg += "send success";
        msg += "\n";
        msg += getString(R.string.message_request) + " " + logBean.getDpSend();
        msg += "\n";
        msg += getString(R.string.time_dp_start) + " " + TuyaUtil.formatDate(logBean.getTimeStart(), "yyyy-MM-dd hh:mm:ss");
        msg += "\n";
        msg += getString(R.string.time_dp_end) + " " + TuyaUtil.formatDate(logBean.getTimeEnd(), "yyyy-MM-dd hh:mm:ss");
        msg += "\n";
        msg += getString(R.string.time_dp_spend) + " " + (logBean.getTimeEnd() - logBean.getTimeStart()) + "ms";
        msg += "\n";
        msg += getString(R.string.message_receive) + " " + logBean.getDpReturn();
        msg += "\n";
        logSuccess(msg);
    }

    @Override
    public void logError(DpLogBean logBean) {
        String msg = "\n";
        msg += "send failure";
        msg += "\n";
        msg += getString(R.string.message_request) + " " + logBean.getDpSend();
        msg += "\n";
        msg += getString(R.string.time_dp_start) + " " + TuyaUtil.formatDate(logBean.getTimeStart(), "yyyy-MM-dd hh:mm:ss");
        msg += "\n";
        msg += getString(R.string.time_dp_end) + " " + TuyaUtil.formatDate(logBean.getTimeEnd(), "yyyy-MM-dd hh:mm:ss");
        msg += "\n";
        msg += getString(R.string.time_dp_spend) + " " + (logBean.getTimeEnd() - logBean.getTimeStart()) + "ms";
        msg += "\n";
        if (!TextUtils.isEmpty(logBean.getDpReturn())) {
            msg += getString(R.string.message_receive) + " " + logBean.getDpReturn();
            msg += "\n";
        }
        msg += getString(R.string.failure_reason) + " " + logBean.getErrorMsg();
        msg += "\n";
        logError(msg);
    }


    @OnClick(R.id.tv_clear)
    public void clearMsg() {
        mLogView.setText("");
    }

    @Override
    public void logDpReport(String dpStr) {
        dpStr = getString(R.string.data_report) + "  " + dpStr;
        final String finalDpStr = dpStr;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SpannableStringBuilder style = new SpannableStringBuilder(finalDpStr);
                style.setSpan(new ForegroundColorSpan(Color.parseColor("#303030")), 0, finalDpStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mLogView.append(style);
                mLogView.append("\n");


            }
        });
        scroll2Bottom();
    }

    @Override
    public void deviceRemoved() {
        DialogUtil.simpleSmartDialog(this, R.string.device_has_unbinded, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }

    @Override
    public void deviceOnlineStatusChanged(boolean online) {
        if (online) {
            setViewGone(mOffLineView);
        } else {
            setViewVisible(mOffLineView);
            mOffLineTip.setText(getString(R.string.device_offLine));
        }
    }

    @Override
    public void onNetworkStatusChanged(boolean status) {
        if (status) {
            setViewGone(mOffLineView);
        } else {
            setViewVisible(mOffLineView);
            mOffLineTip.setText(getString(R.string.device_network_error));
        }
    }

    @Override
    public void devInfoUpdate() {
        initTitle();
    }

    @Override
    public void updateTitle(String titleName) {
        setTitle(titleName);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        mPresenter.onDestroy();
    }
}
