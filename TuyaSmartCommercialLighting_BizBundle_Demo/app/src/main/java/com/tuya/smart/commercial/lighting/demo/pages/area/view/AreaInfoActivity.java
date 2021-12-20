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
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.app.IntentExtra;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.pages.area.item.AreaIndexItemStyle1;
import com.tuya.smart.commercial.lighting.demo.pages.area.presenter.AreaInfoPresenter;
import com.tuya.smart.commercial.lighting.demo.pages.area.util.CreateAreaUtils;
import com.tuya.smart.commercial.lighting.demo.pages.device.view.DeviceListActivity;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;
import com.tuya.smart.commercial.lighting.demo.utils.DialogUtil;
import com.tuya.smart.commercial.lighting.demo.widget.recyclerview.adapter.BaseRVAdapter;
import com.tuya.smart.home.sdk.bean.SimpleAreaBean;
import com.tuya.smart.lighting.sdk.anno.AreaCategory;
import com.tuya.smart.lighting.sdk.anno.AreaCreateStatus;
import com.tuya.smart.lighting.sdk.bean.AreaBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils.ANIMATE_SCALE_IN;

public class AreaInfoActivity extends BaseActivity implements IAreaInfoView {
    private static int DEVICE_ACTIVATOR_REQUEST_CODE = 0x8888;

    @BindView(R.id.area_info_rv)
    RecyclerView rv;
    @BindView(R.id.area_info_name)
    TextView tvName;
    @BindView(R.id.area_info_create_sub)
    Button btnCreateSub;
    @BindView(R.id.area_info_create_parent)
    Button btnCreateParent;
    @BindView(R.id.area_info_address)
    TextView tvAddress;

    private static final int AREA_INFO_REQUEST_CODE = 1;
    private AreaInfoPresenter mPresenter;
    private BaseRVAdapter<AreaIndexItemStyle1> mAdapter;
    private long mProjectId, mAreaId;
    Unbinder unbinder;
    private int projectType;
    private SimpleAreaBean currentAreaInfo;

    public static final int PROJECT_INDOOR = 0;
    public static final int PROJECT_OUTDOOR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cl_activity_area_info);
        unbinder = ButterKnife.bind(this);
        Intent intent = getIntent();
        mProjectId = intent.getLongExtra(IntentExtra.KEY_PROJECT_ID, 0);
        projectType = intent.getIntExtra(IntentExtra.KEY_PROJECT_TYPE, 0);
        mAreaId = intent.getLongExtra(IntentExtra.KEY_AREA_ID, 0);
        initToolbar();
        initTitle();
        initAdapter();
        initListener();
        initPresenter();
    }

    private void initTitle() {
        setDisplayHomeAsUpEnabled();
        setTitle(getString(R.string.cl_area_info_title));
        mToolBar.setTitleTextColor(Color.WHITE);
    }

    private void initPresenter() {
        mPresenter = new AreaInfoPresenter(mProjectId, mAreaId, this);
    }

    private void initListener() {
        mAdapter.setOnItemViewClickListener((item, sectionKey, sectionItemPosition) -> {
            AreaBean itemData = item.getData();
            if (itemData.getRoomSource() == AreaCategory.CUSTOM_AREA) {
                Intent intent = new Intent(this, AreaInfoActivity.class);
                intent.putExtra(IntentExtra.KEY_PROJECT_ID, mProjectId);
                intent.putExtra(IntentExtra.KEY_PROJECT_TYPE, projectType);
                intent.putExtra(IntentExtra.KEY_AREA_ID, itemData.getAreaId());
                ActivityUtils.startActivityForResult(this, intent, AREA_INFO_REQUEST_CODE, ANIMATE_SCALE_IN, false);
            } else {
                showDeviceList();
            }
        });
    }

    private void initAdapter() {
        mAdapter = new BaseRVAdapter<>();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapter);
    }

    @OnClick(R.id.btn_device_list)
    public void showDeviceList() {
//        Intent intent = new Intent(this, DeviceListActivity.class);
//        intent.putExtras(getIntent());
//        startActivity(intent);
        ActivityUtils.gotoDeviceControlActivity(this,mProjectId,mAreaId);
    }


    @OnClick(R.id.area_info_remove)
    public void deleteArea() {
        mPresenter.deleteArea();
    }

    @OnClick(R.id.area_info_name)
    public void updateAreaName() {
        DialogUtil.simpleInputDialog(this, getString(R.string.cl_area_edit_name), tvName.getText(), false, new DialogUtil.SimpleInputDialogInterface() {
            @Override
            public void onPositive(DialogInterface dialog, String inputText) {
                mPresenter.updateAreaName(inputText);
            }

            @Override
            public void onNegative(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.btn_packed_group)
    public void showPackedGroupList() {
        Intent intent = new Intent(this, GroupListActivity.class);
        intent.putExtras(getIntent());
        startActivity(intent);
    }

    @OnClick(R.id.btn_area_control)
    public void onAreaControlClick() {
        Intent intent = new Intent(this, AreaControlActivity.class);
        intent.putExtras(getIntent());
        startActivity(intent);
    }

    @OnClick(R.id.btn_device_config)
    public void onDeviceConfig() {
//        ActivityUtils.gotoActivatorActivity(this, mProjectId, mAreaId);
        ActivityUtils.gotoDeviceActivatorActivity(this, DEVICE_ACTIVATOR_REQUEST_CODE);
    }

    @OnClick(R.id.area_info_create_sub)
    public void createSubArea() {
        CreateAreaUtils.createAreaDialog(this, projectType, new CreateAreaUtils.AreaInputDialogInterface() {
            @Override
            public void onPositive(DialogInterface dialog, long areaId, String areaName, int areaLevel, double longitude, double latitude, String address) {
                if (projectType == PROJECT_INDOOR) {
                    mPresenter.createSubArea(areaName);
                } else {
                    mPresenter.createOutdoorSubArea(areaName, longitude, latitude, address);
                }
            }

            @Override
            public void onNegative(DialogInterface dialog) {
            }
        });
    }

    @OnClick(R.id.area_info_create_parent)
    public void createParentArea() {
        CreateAreaUtils.createAreaDialog(this, projectType, new CreateAreaUtils.AreaInputDialogInterface() {
            @Override
            public void onPositive(DialogInterface dialog, long areaId, String areaName, int areaLevel, double longitude, double latitude, String address) {
                if (projectType == PROJECT_INDOOR) {
                    mPresenter.createParentArea(areaName);
                } else {
                    mPresenter.createOutdoorParentArea(areaName, longitude, latitude, address);
                }
            }

            @Override
            public void onNegative(DialogInterface dialog) {

            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setAreaData(SimpleAreaBean simpleAreaBean) {
        this.currentAreaInfo = simpleAreaBean;
        tvName.setText(simpleAreaBean.getName());
        tvAddress.setText(simpleAreaBean.getAddress());
        switch (simpleAreaBean.getCanCreateStatus()) {
            case AreaCreateStatus.CREATE_CHILD_AND_PARENT_BOTH:
                btnCreateParent.setVisibility(View.VISIBLE);
                btnCreateSub.setVisibility(View.VISIBLE);
                break;
            case AreaCreateStatus.CREATE_CHILD_ONLY:
                btnCreateParent.setVisibility(View.GONE);
                btnCreateSub.setVisibility(View.VISIBLE);
                break;
            case AreaCreateStatus.CREATE_PARENT_ONLY:
                btnCreateParent.setVisibility(View.VISIBLE);
                btnCreateSub.setVisibility(View.GONE);
                break;
            case AreaCreateStatus.CREATE_DISABLE:
                btnCreateParent.setVisibility(View.GONE);
                btnCreateSub.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void setSubAreaList(List<AreaBean> subAreaList) {
        mAdapter.clearAllItemView();
        for (AreaBean item : subAreaList) {
            mAdapter.addItemView(new AreaIndexItemStyle1(item));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateSuccess(String name) {
        setResult(RESULT_OK);
        tvName.setText(name);
    }

    @Override
    public void doRemoveView(boolean isSuccess) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void createSubSuccess(SimpleAreaBean simpleAreaBean) {
        AreaBean areaBean = new AreaBean();
        areaBean.setAreaId(simpleAreaBean.getAreaId());
        areaBean.setName(simpleAreaBean.getName());
        mAdapter.addItemView(new AreaIndexItemStyle1(areaBean));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void createParentSuccess(SimpleAreaBean simpleAreaBean) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void createFailed(String content) {
        showToast(content);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AREA_INFO_REQUEST_CODE && resultCode == RESULT_OK) {
            mPresenter.getSubAreaList();
        }
        if (requestCode == DEVICE_ACTIVATOR_REQUEST_CODE && resultCode == RESULT_OK) {
            final ArrayList<String> ids = data.getStringArrayListExtra("ids");
            if (ids.size() > 0) {
                new Handler().postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        ActivityUtils.putDevicesInArea(AreaInfoActivity.this, mProjectId, mAreaId, ids);
                    }
                }, 5000);
            }
        }
    }
}