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

package com.tuya.smart.commercial.lighting.demo.pages.area.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.pages.area.presenter.AreaControlPresenter;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.utils.ToastUtil;
import com.tuya.smart.commercial.lighting.demo.bean.AreaBeanWrapper;
import com.tuya.smart.commercial.lighting.demo.app.IntentExtra;
import com.tuya.smart.lighting.sdk.enums.AreaDpMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AreaControlActivity extends BaseActivity implements IAreaControlView {

    private long mProjectId;
    private long mAreaId;
    private AreaControlPresenter areaControlPresenter;

    @BindView(R.id.tv_area_info)
    public TextView areaInfoView;

    private String areaInfoHolder = "areaName:%s  switch:%s  mode:%s";

    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cl_dialog_area_control);
        unbinder = ButterKnife.bind(this);
        initToolbar();
        initTitle();

        Intent intent = getIntent();
        mProjectId = intent.getLongExtra(IntentExtra.KEY_PROJECT_ID, 0);
        mAreaId = intent.getLongExtra(IntentExtra.KEY_AREA_ID, 0);

        areaControlPresenter = new AreaControlPresenter(mProjectId, mAreaId, this);
    }

    @OnClick(R.id.btn_switch_on)
    public void toggleSwitchOn() {
        areaControlPresenter.setSwitch(true);
    }

    @OnClick(R.id.btn_switch_off)
    public void toggleSwitchOff() {
        areaControlPresenter.setSwitch(false);
    }

    @OnClick(R.id.btn_white_mode)
    public void toggleWhiteMode() {
        areaControlPresenter.setDpMode(AreaDpMode.MODE_WHITE_LIGHT);
    }

    @OnClick(R.id.btn_colour_mode)
    public void toggleColourMode() {
        areaControlPresenter.setDpMode(AreaDpMode.MDOE_COLORFUL_LIGHT);
    }

    @OnClick(R.id.btn_scene_mode)
    public void toggleSceneMode() {
        areaControlPresenter.setDpMode(AreaDpMode.MODE_SCENE);
    }

    private void initTitle() {
        setDisplayHomeAsUpEnabled();
        setTitle(getString(R.string.cl_area_control));
        mToolBar.setTitleTextColor(Color.WHITE);
    }


    @Override
    public void setAreaInfo(AreaBeanWrapper areaBean) {
        areaInfoView.setText(String.format(areaInfoHolder, areaBean.getName(),
                areaBean.isSwitchOpen() ? "ON" : "OFF", areaBean.getAreaDpMode().getDesc()));
    }

    @Override
    public void toast(String message) {
        ToastUtil.showToast(this, message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
