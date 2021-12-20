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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.tuya.commerciallighting.commonbiz.bizbundle.project.api.AbsBizBundleProjectService;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.api.service.MicroServiceManager;
import com.tuya.smart.commercial.lighting.demo.app.IntentExtra;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.pages.project.item.ProjectIndexEmpty;
import com.tuya.smart.commercial.lighting.demo.pages.project.item.ProjectIndexFoot;
import com.tuya.smart.commercial.lighting.demo.pages.project.item.ProjectIndexItem;
import com.tuya.smart.commercial.lighting.demo.pages.project.presenter.ProjectIndexPresenter;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;
import com.tuya.smart.commercial.lighting.demo.utils.CollectionUtils;
import com.tuya.smart.commercial.lighting.demo.utils.DialogUtil;
import com.tuya.smart.commercial.lighting.demo.utils.ToastUtil;
import com.tuya.smart.commercial.lighting.demo.widget.recyclerview.adapter.BaseRVAdapter;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.lighting.sdk.bean.LightingProjectConfigsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProjectIndexActivity extends BaseActivity implements IProjectIndexView {
    @BindView(R.id.project_index_rv)
    RecyclerView rv;

    private ProjectIndexPresenter mPresenter;
    private BaseRVAdapter<ProjectIndexItem> mAdapter;

    Unbinder unbinder;
    private ProjectIndexFoot addProjectFoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cl_activity_project_index);
        unbinder = ButterKnife.bind(this);
        initToolbar();
        initTitle();
        initAdapter();
        initListener();

        initPresenter();
    }

    private void initListener() {
        mAdapter.setOnItemViewClickListener((item, sectionKey, sectionItemPosition) -> routeInfoActivity(item.getData()));
    }

    private void initAdapter() {
        mAdapter = new BaseRVAdapter<>();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapter);
    }

    private void initTitle() {
        setDisplayHomeAsUpEnabled();
        setTitle(getString(R.string.cl_project_index_title));
        mToolBar.setTitleTextColor(Color.WHITE);
    }

    private void initPresenter() {
        mPresenter = new ProjectIndexPresenter(this);
    }

    private void routeAddActivity() {
        showLoading();
        mPresenter.getProjectTypeList();
    }

    private void routeInfoActivity(HomeBean data) {
        if (null == data) return;
        long projectId = data.getHomeId();
        AbsBizBundleProjectService service = MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleProjectService.class.getName());
        service.shiftCurrentProject(projectId, data.getName());

        Intent intent = new Intent(this, ProjectInfoActivity.class);
        intent.putExtra(IntentExtra.KEY_PROJECT_ID, projectId);
        ActivityUtils.startActivity(this, intent, ActivityUtils.ANIMATE_SLIDE_TOP_FROM_BOTTOM, false);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        mPresenter.getProjectList();
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
    public Context getContext() {
        return this;
    }

    @Override
    public void setProjectList(List<HomeBean> projectList) {
        if (CollectionUtils.isEmpty(projectList)) {
            return;
        }
        final List<ProjectIndexItem> homeItems = new ArrayList<>();
        for (HomeBean homeBean : projectList) {
            homeItems.add(new ProjectIndexItem(homeBean));
        }
        runOnUiThread(() -> {
            mAdapter.setItemViewList(homeItems);
            if (null == addProjectFoot) {
                addProjectFoot = new ProjectIndexFoot(getString(R.string.cl_project_add_title));
                addProjectFoot.setOnClickListener(v -> routeAddActivity());
                mAdapter.addFootView(addProjectFoot);
            }
        });

    }

    @Override
    public void showProjectEmptyView() {
        runOnUiThread(() -> {
            ProjectIndexEmpty emptyView = new ProjectIndexEmpty();
            emptyView.setOnAddProjectListener(v -> routeAddActivity());
            mAdapter.showEmptyView(emptyView);
        });
    }

    @Override
    public void getProjectTypeListSuccess(List<LightingProjectConfigsBean> configsBeans) {
        hideLoading();
        ArrayList<String> list = new ArrayList<>();
        String[] strings = new String[configsBeans.size()];
        for (LightingProjectConfigsBean item : configsBeans) {
            list.add(item.projectTypeName);
        }
        strings = list.toArray(strings);
        DialogUtil.listSelectDialog(this, getResources().getString(R.string.cl_choose_project_type), strings, (parent, view, position, id) -> {
            Intent intent = new Intent(this, ProjectAddActivity.class);
            intent.putExtra("projectType", JSONObject.toJSONString(configsBeans.get(position)));
            ActivityUtils.startActivity(this, intent, ActivityUtils.ANIMATE_SLIDE_TOP_FROM_BOTTOM, false);
        });
    }

    @Override
    public void getProjectTypeListError() {
        hideLoading();
        ToastUtil.shortToast(this, "can not get project type");
    }
}
