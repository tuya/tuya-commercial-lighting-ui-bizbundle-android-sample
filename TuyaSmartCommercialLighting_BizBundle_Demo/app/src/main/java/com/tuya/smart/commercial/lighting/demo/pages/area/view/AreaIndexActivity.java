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

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.app.IntentExtra;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.pages.area.item.AreaIndexItemStyle1;
import com.tuya.smart.commercial.lighting.demo.pages.area.item.AreaIndexItemStyle2;
import com.tuya.smart.commercial.lighting.demo.pages.area.presenter.AreaIndexPresenter;
import com.tuya.smart.commercial.lighting.demo.pages.area.util.CreateAreaUtils;
import com.tuya.smart.commercial.lighting.demo.pages.device.view.DeviceListActivity;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;
import com.tuya.smart.commercial.lighting.demo.widget.recyclerview.adapter.BaseRVAdapter;
import com.tuya.smart.home.sdk.bean.SimpleAreaBean;
import com.tuya.smart.lighting.sdk.anno.AreaCategory;
import com.tuya.smart.lighting.sdk.bean.AreaBean;
import com.tuya.smart.lighting.sdk.bean.AreaConfig;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils.ANIMATE_SCALE_IN;


public class AreaIndexActivity extends BaseActivity implements IAreaIndexView {
    @BindView(R.id.rv_area_list)
    RecyclerView rvAreaList;
    @BindView(R.id.rv_area_level_list)
    RecyclerView rvLevelList;

    private static final int AREA_INFO_REQUEST_CODE = 1;
    private AreaIndexPresenter mPresenter;
    private BaseRVAdapter<AreaIndexItemStyle1> adapterAreaList;
    private BaseRVAdapter<AreaIndexItemStyle2> adapterLevelList;
    private long mProjectId;
    private int mProjectType;
    Unbinder unbinder;
    private String levelHint;

    public static final int PROJECT_INDOOR = 0;
    public static final int PROJECT_OUTDOOR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cl_activity_area_index);
        unbinder = ButterKnife.bind(this);
        Intent intent = getIntent();
        mProjectId = intent.getLongExtra(IntentExtra.KEY_PROJECT_ID, 0);
        mProjectType = intent.getIntExtra(IntentExtra.KEY_PROJECT_TYPE, 0);
        initToolbar();
        initTitle();
        initAdapter();
        initListener();
        initPresenter();
    }

    private void initListener() {
        adapterAreaList.setOnItemViewClickListener((item, sectionKey, sectionItemPosition) -> {
            AreaBean data = item.getData();
            Intent intent = null;
            switch (data.getRoomSource()) {
                case AreaCategory.CUSTOM_AREA:
                    intent = new Intent(this, AreaInfoActivity.class);
                    intent.putExtra(IntentExtra.KEY_PROJECT_ID, mProjectId);
                    intent.putExtra(IntentExtra.KEY_AREA_ID, data.getAreaId());
                    intent.putExtra(IntentExtra.KEY_PROJECT_TYPE, mProjectType);
                    ActivityUtils.startActivityForResult(this, intent, AREA_INFO_REQUEST_CODE, ANIMATE_SCALE_IN, false);
                    break;
                case AreaCategory.PUBLIC_AREA:
                case AreaCategory.UN_SUB_AREA:
                    intent = new Intent(this, DeviceListActivity.class);
                    intent.putExtras(getIntent());
                    startActivity(intent);
                    break;
            }
        });
    }

    private void initAdapter() {
        adapterAreaList = new BaseRVAdapter<>();
        rvAreaList.setLayoutManager(new LinearLayoutManager(this));
        rvAreaList.setAdapter(adapterAreaList);

        adapterLevelList = new BaseRVAdapter<>();
        rvLevelList.setLayoutManager(new LinearLayoutManager(this));
        rvLevelList.setAdapter(adapterLevelList);
    }

    private void initTitle() {
        setDisplayHomeAsUpEnabled();
        setTitle(getString(R.string.cl_area_index_title));
        mToolBar.setTitleTextColor(Color.WHITE);
    }

    private void initPresenter() {
        mPresenter = new AreaIndexPresenter(mProjectId, this);
    }

    @OnClick(R.id.btn_area_level_list)
    public void getLevelList() {
        mPresenter.getAreaLevels(true, true);
    }

    @OnClick(R.id.btn_area_list)
    public void getAreaList() {
        mPresenter.getAreaList();
    }

    @OnClick(R.id.btn_create_area)
    public void createNewArea() {
        CreateAreaUtils.createNewDialog(this, levelHint, mProjectType, new CreateAreaUtils.AreaInputDialogInterface() {
            @Override
            public void onPositive(DialogInterface dialog, long areaId, String areaName, int areaLevel, double longitude, double latitude, String address) {
                if (mProjectType == PROJECT_INDOOR) {
                    mPresenter.createArea(areaName, areaLevel);
                } else {
                    mPresenter.createArea(areaName, areaLevel, longitude, latitude, address);
                }
            }

            @Override
            public void onNegative(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setAreaList(List<AreaBean> areaList) {
        adapterAreaList.clearAllItemView();
        for (AreaBean item : areaList) {
            adapterAreaList.addItemView(new AreaIndexItemStyle1(item));
        }
        adapterAreaList.notifyDataSetChanged();
    }

    @Override
    public void setAreaLevels(List<SimpleAreaBean> areaList) {
        adapterLevelList.clearAllItemView();
        for (SimpleAreaBean item : areaList) {
            adapterLevelList.addItemView(new AreaIndexItemStyle2(item));
        }
        adapterLevelList.notifyDataSetChanged();
    }


    @Override
    public void doSaveFailed(String content) {
        showToast(content);
    }

    @Override
    public void setProjectConfig(List<AreaConfig> areaConfigs) {
        StringBuilder sb = new StringBuilder();
        for (AreaConfig item : areaConfigs) {
            sb.append(item.getName()).append(":").append(item.getId()).append(" , ");
        }
        levelHint = sb.toString();
    }

    @Override
    public void doSaveSuccess(SimpleAreaBean simpleAreaBean) {
        AreaBean areaBean = new AreaBean();
        areaBean.setName(simpleAreaBean.getName());
        areaBean.setAreaId(simpleAreaBean.getAreaId());
        areaBean.setRoomSource(AreaCategory.CUSTOM_AREA);
        adapterAreaList.addItemView(new AreaIndexItemStyle1(areaBean));
        adapterAreaList.notifyDataSetChanged();
        adapterLevelList.addItemView(new AreaIndexItemStyle2(simpleAreaBean));
        adapterLevelList.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AREA_INFO_REQUEST_CODE && resultCode == RESULT_OK) {
            mPresenter.getAreaList();
            mPresenter.getAreaLevels(true, true);
        }
    }
}