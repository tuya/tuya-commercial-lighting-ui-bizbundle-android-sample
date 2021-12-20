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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.pages.area.view.AreaIndexActivity;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;
import com.tuya.smart.commercial.lighting.demo.utils.DialogUtil;
import com.tuya.smart.commercial.lighting.demo.app.IntentExtra;
import com.tuya.smart.commercial.lighting.demo.pages.project.item.ProjectIndexFoot;
import com.tuya.smart.commercial.lighting.demo.pages.project.item.ProjectInfoItem;
import com.tuya.smart.commercial.lighting.demo.pages.project.presenter.ProjectInfoPresenter;
import com.tuya.smart.commercial.lighting.demo.widget.recyclerview.adapter.BaseRVAdapter;
import com.tuya.smart.commercial.lighting.demo.widget.recyclerview.item.BaseItem;
import com.tuya.smart.home.sdk.bean.HomeBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tuya.smart.commercial.lighting.demo.pages.project.view.ProjectAddActivity.REGION_CODE;
import static com.tuya.smart.commercial.lighting.demo.pages.project.view.ProjectAddActivity.REGION_LIST_REQUEST_CODE;
import static com.tuya.smart.commercial.lighting.demo.pages.project.view.ProjectAddActivity.REGION_NAME;

public class ProjectInfoActivity extends BaseActivity implements IProjectInfoView {

    @BindView(R.id.project_info_rv)
    RecyclerView rv;

    private BaseRVAdapter<BaseItem> mAdapter;
    private ProjectInfoPresenter mPresenter;
    private long mProjectId;
    Unbinder unbinder;
    private HomeBean currentHomeBean;

    public static final int SECTION_1 = 1;
    public static final int SECTION_2 = 2;
    public static final int SECTION_3 = 3;
    public static final int SECTION_4 = 4;
    public static final int SECTION_5 = 5;
    public static final int SECTION_6 = 6;


    public static final int PROJECT_INDOOR = 0;
    public static final int PROJECT_OUTDOOR = 1;

    private ProjectInfoItem projectNameItem, leaderNameItem, leaderMobileItem, addressDetailItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cl_activity_project_info);
        unbinder = ButterKnife.bind(this);
        mProjectId = getIntent().getLongExtra(IntentExtra.KEY_PROJECT_ID, -1);

        initToolbar();
        initTitle();
        initAdapter();
        initPresenter();
    }


    private void initAdapter() {
        mAdapter = new BaseRVAdapter<>();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapter);
        mAdapter.setOnItemViewClickListener((item, sectionKey, sectionItemPosition) -> {
            if (currentHomeBean == null) return;
            ProjectInfoItem projectInfoItem = (ProjectInfoItem) item;
            if (sectionKey == SECTION_5 && currentHomeBean.getProjectType() == PROJECT_OUTDOOR) {
                ActivityUtils.gotoRegionListActivity(this, REGION_LIST_REQUEST_CODE);
            } else {
                showEditDialog(sectionKey, projectInfoItem);
            }
        });
    }

    private void showEditDialog(int sectionKey, ProjectInfoItem projectInfoItem) {
        DialogUtil.simpleInputDialog(this, projectInfoItem.getData().first, projectInfoItem.getData().second, false, new DialogUtil.SimpleInputDialogInterface() {
            @Override
            public void onPositive(DialogInterface dialog, String inputText) {
                if (inputText.isEmpty()) {
                    showToast(projectInfoItem.getData().first + getString(R.string.cl_project_add_item_check_tip));
                    return;
                }
                switch (sectionKey) {
                    case SECTION_1:
                        break;
                    case SECTION_2:
                        mPresenter.updateProjectName(inputText);
                        break;
                    case SECTION_3:
                        mPresenter.updateLeaderName(inputText);
                        break;
                    case SECTION_4:
                        mPresenter.updateLeaderMobile(inputText);
                        break;
                    case SECTION_5:
                        mPresenter.updateAddressDetail(inputText);
                        break;
                }
            }

            @Override
            public void onNegative(DialogInterface dialog) {

            }
        });
    }

    private void initTitle() {
        setDisplayHomeAsUpEnabled();
        setTitle(getString(R.string.cl_project_info_title));
        mToolBar.setTitleTextColor(Color.WHITE);
    }

    private void initPresenter() {
        mPresenter = new ProjectInfoPresenter(this);
    }


    @OnClick(R.id.project_info_remove)
    public void onRemoveProjectClick() {
        DialogUtil.simpleInputDialog(this, getString(R.string.cl_project_input_password), "", false, new DialogUtil.SimpleInputDialogInterface() {
            @Override
            public void onPositive(DialogInterface dialog, String inputText) {
                mPresenter.remove(inputText);
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
    public long getProjectId() {
        return mProjectId;
    }

    @Override
    public void setProjectData(HomeBean homeBean) {
        if (null == homeBean) {
            return;
        }
        this.currentHomeBean = homeBean;
        projectNameItem = new ProjectInfoItem(Pair.create(getString(R.string.cl_project_info_name), homeBean.getName()));
        leaderNameItem = new ProjectInfoItem(Pair.create(getString(R.string.cl_project_info_leader_name), homeBean.getLeaderName()));
        leaderMobileItem = new ProjectInfoItem(Pair.create(getString(R.string.cl_project_info_leader_mobile), homeBean.getLeaderMobile()));
        addressDetailItem = new ProjectInfoItem(Pair.create(getString(R.string.cl_project_info_address), homeBean.getDetail()));
        ProjectIndexFoot areaManagement = new ProjectIndexFoot(getString(R.string.cl_area_index_title));
        areaManagement.setOnClickListener(v -> {
            Intent intent = new Intent(this, AreaIndexActivity.class);
            intent.putExtra(IntentExtra.KEY_PROJECT_ID, mProjectId);
            intent.putExtra(IntentExtra.KEY_PROJECT_TYPE, currentHomeBean.getProjectType());
            ActivityUtils.startActivity(this, intent,
                    ActivityUtils.ANIMATE_SLIDE_TOP_FROM_BOTTOM,
                    false);
        });
        runOnUiThread(() -> {
            mAdapter.addItemView(SECTION_2, projectNameItem);
            mAdapter.addItemView(SECTION_3, leaderNameItem);
            mAdapter.addItemView(SECTION_4, leaderMobileItem);
            mAdapter.addItemView(SECTION_5, addressDetailItem);
            mAdapter.addFootView(SECTION_6, areaManagement);
        });
    }

    @Override
    public void doRemoveView(boolean isSuccess) {
        hideLoading();
        if (isSuccess) {
            finishActivity();
        }
        showToast(isSuccess ? R.string.success : R.string.fail);
    }


    @Override
    public void updateProjectName(String content) {
        projectNameItem.setData(Pair.create(getString(R.string.cl_project_info_name), content));
        mAdapter.updateItemView(SECTION_2, projectNameItem);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateLeaderName(String content) {
        leaderNameItem.setData(Pair.create(getString(R.string.cl_project_info_leader_name), content));
        mAdapter.updateItemView(SECTION_3, leaderNameItem);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateLeaderMobile(String content) {
        leaderMobileItem.setData(Pair.create(getString(R.string.cl_project_info_leader_mobile), content));
        mAdapter.updateItemView(SECTION_4, leaderMobileItem);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateAddressDetail(String content) {
        addressDetailItem.setData(Pair.create(getString(R.string.cl_project_info_address), content));
        mAdapter.updateItemView(SECTION_5, addressDetailItem);
        mAdapter.notifyDataSetChanged();
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
                String regionCode = data.getStringExtra(REGION_CODE);
                mPresenter.updateOutdoorAddressDetail(regionCode, regionName);
            }
        }
    }
}
