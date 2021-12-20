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
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.pages.project.adapter.RegionListAdapter;
import com.tuya.smart.commercial.lighting.demo.pages.project.presenter.RegionListPresenter;
import com.tuya.smart.lighting.sdk.bean.LightingRegionListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.tuya.smart.commercial.lighting.demo.pages.project.view.ProjectAddActivity.REGION_CODE;
import static com.tuya.smart.commercial.lighting.demo.pages.project.view.ProjectAddActivity.REGION_NAME;

public class RegionListActivity extends BaseActivity implements IRegionListView {

    @BindView(R.id.rv_region_list)
    RecyclerView rvRegionList;

    @BindView(R.id.rv_country_list)
    RecyclerView rvSubRegionList;

    Unbinder unbinder;
    private RegionListPresenter mPresenter;
    private RegionListAdapter regionListAdapter, subRegionListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_list);
        unbinder = ButterKnife.bind(this);
        initToolbar();
        initAdapter();
        initPresenter();
    }

    private void initPresenter() {
        showLoading();
        mPresenter = new RegionListPresenter(this);
        mPresenter.getRegionList();
    }

    private void initAdapter() {
        regionListAdapter = new RegionListAdapter(this);
        subRegionListAdapter = new RegionListAdapter(this);

        rvRegionList.setLayoutManager(new GridLayoutManager(this, 5));
        rvSubRegionList.setLayoutManager(new LinearLayoutManager(this));

        rvRegionList.setAdapter(regionListAdapter);
        rvSubRegionList.setAdapter(subRegionListAdapter);

        regionListAdapter.setClickListener((position, regionBean) -> {
            regionListAdapter.refreshUI(position);
            showLoading();
            List<LightingRegionListBean> subRegionList = mPresenter.getSubRegionList(regionBean.locationId);
            subRegionListAdapter.setData(subRegionList);
            hideLoading();
        });

        subRegionListAdapter.setClickListener((position, regionBean) -> {
            LightingRegionListBean selectedItem = regionListAdapter.getSelectedItem();
            String regionCode = selectedItem.locationId + "," + regionBean.locationId;
            String regionName = selectedItem.name + regionBean.name;
            Intent intent = new Intent();
            intent.putExtra(REGION_CODE, regionCode);
            intent.putExtra(REGION_NAME, regionName);
            setResult(RESULT_OK, intent);
            finishActivity();
        });
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void getRegionListSuccess(List<LightingRegionListBean> result) {
        regionListAdapter.setData(result);
        hideLoading();
    }

    @Override
    public void getRegionListError(String errorMsg) {
        hideLoading();
        showToast(errorMsg);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
        mPresenter.onDestroy();
    }

}