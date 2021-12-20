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

package com.tuya.smart.commercial.lighting.demo.pages.project.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.app.Constant;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.pages.project.item.ProjectAddEditNameItem;
import com.tuya.smart.commercial.lighting.demo.pages.project.item.ProjectIndexFoot;
import com.tuya.smart.commercial.lighting.demo.pages.project.presenter.ProjectAddPresenter;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;
import com.tuya.smart.commercial.lighting.demo.widget.recyclerview.adapter.BaseRVAdapter;
import com.tuya.smart.commercial.lighting.demo.widget.recyclerview.item.BaseItem;
import com.tuya.smart.lighting.sdk.bean.LightingProjectConfigsBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProjectAddActivity extends BaseActivity implements IProjectAddView {

    @BindView(R.id.project_add_rv)
    RecyclerView rv;

    @BindView(R.id.project_type_icon)
    SimpleDraweeView projectTypeIcon;

    @BindView(R.id.project_type_name)
    TextView projectTypeName;

    public static final String KEY_EMPTY_PROJECT = "empty_project_key";
    public static final int REGION_LIST_REQUEST_CODE = 1;
    public static final String REGION_CODE = "regionCode";
    public static final String REGION_NAME = "regionName";
    public static final int PROJECT_INDOOR = 0;
    public static final int PROJECT_OUTDOOR = 1;

    private ProjectAddPresenter mPresenter;

    private BaseRVAdapter<BaseItem> mAdapter;

    private LightingProjectConfigsBean projectConfigsBean;
    Unbinder unbinder;
    private ProjectAddEditNameItem mEditNameItem, mLeaderNameItem, mLeaderMobileItem, mDetailAddressItem;
    private ProjectIndexFoot mSelectAddressItem;
    private boolean isEmptyProject;
    private String regionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cl_activity_project_add);
        unbinder = ButterKnife.bind(this);
        isEmptyProject = getIntent().getBooleanExtra(KEY_EMPTY_PROJECT, false);
        String projectType = getIntent().getStringExtra("projectType");
        try {
            projectConfigsBean = JSONObject.parseObject(projectType, LightingProjectConfigsBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initToolbar();
        initTitle();
        initAdapter();
        initListener();
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new ProjectAddPresenter(this);
    }

    private void initListener() {

    }

    private void initAdapter() {
        mAdapter = new BaseRVAdapter<>();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapter);
        mEditNameItem = new ProjectAddEditNameItem(getString(R.string.cl_project_info_name));
        mLeaderNameItem = new ProjectAddEditNameItem(getString(R.string.cl_project_info_leader_name));
        mLeaderMobileItem = new ProjectAddEditNameItem(getString(R.string.cl_project_info_leader_mobile));
        mDetailAddressItem = new ProjectAddEditNameItem(getString(R.string.cl_project_info_address));
        mSelectAddressItem = new ProjectIndexFoot(getString(R.string.cl_project_info_address));
        mAdapter.addItemView(mEditNameItem);
        mAdapter.addItemView(mLeaderNameItem);
        mAdapter.addItemView(mLeaderMobileItem);
        if (projectConfigsBean.projectType == PROJECT_OUTDOOR) {
            mAdapter.addItemView(mSelectAddressItem);
        } else {
            mAdapter.addItemView(mDetailAddressItem);
        }
        mSelectAddressItem.setOnClickListener(v -> ActivityUtils.gotoRegionListActivity(ProjectAddActivity.this, REGION_LIST_REQUEST_CODE));
    }

    private void initTitle() {
        setTitle(getString(R.string.cl_project_add_title));
        mToolBar.setTitleTextColor(Color.WHITE);
        setDisplayHomeAsUpEnabled();
        setMenu(R.menu.toolbar_save, item -> {
            if (item.getItemId() == R.id.action_menu_save) {
                checkParams();
            }
            return false;
        });
        projectTypeIcon.setImageURI(projectConfigsBean.projectIconUrl);
        projectTypeName.setText(projectConfigsBean.projectTypeName);
    }


    private void checkParams() {
        String projectName = mEditNameItem.getEditText();
        if (TextUtils.isEmpty(projectName)) {
            showToast(mEditNameItem.getTitleName() + getString(R.string.cl_project_add_item_check_tip));
            return;
        }
        String leaderName = mLeaderNameItem.getEditText();
        if (TextUtils.isEmpty(leaderName)) {
            showToast(mLeaderNameItem.getTitleName() + getString(R.string.cl_project_add_item_check_tip));
            return;
        }
        String leaderMobile = mLeaderMobileItem.getEditText();
        if (TextUtils.isEmpty(leaderMobile)) {
            showToast(mLeaderMobileItem.getTitleName() + getString(R.string.cl_project_add_item_check_tip));
            return;
        }
        String projectAddress;
        if (projectConfigsBean.projectType == PROJECT_INDOOR) {
            projectAddress = mDetailAddressItem.getEditText();
            if (TextUtils.isEmpty(projectAddress)) {
                showToast(mDetailAddressItem.getTitleName() + getString(R.string.cl_project_add_item_check_tip));
                return;
            }
        } else {
            projectAddress = mSelectAddressItem.getContentName();
            if (TextUtils.isEmpty(projectAddress) || TextUtils.isEmpty(regionCode)) {
                showToast(mSelectAddressItem.getContentName() + getString(R.string.cl_project_add_item_check_tip));
                return;
            }
        }

        showLoading();
        mPresenter.addProject(projectConfigsBean.projectType, projectName, leaderName, leaderMobile, projectAddress, regionCode);

    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void doSaveSuccess() {
        hideLoading();
        showToast(R.string.save_success);
        finishActivity();
        if (isEmptyProject) {
            Constant.finishActivity();
            ActivityUtils.gotoHomeActivity(this);
        }
    }

    @Override
    public void doSaveFailed(String errorMsg) {
        hideLoading();
        showToast(R.string.save_failed + ":" + errorMsg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
        mPresenter.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGION_LIST_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String regionName = data.getStringExtra(REGION_NAME);
                regionCode = data.getStringExtra(REGION_CODE);
                mSelectAddressItem.setContentName(regionName);
            }
        }
    }
}
