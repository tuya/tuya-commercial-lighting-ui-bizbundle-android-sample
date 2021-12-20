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

package com.tuya.smart.commercial.lighting.demo.pages.project.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.commercial.lighting.demo.utils.CollectionUtils;
import com.tuya.smart.commercial.lighting.demo.pages.project.model.IProjectIndexModel;
import com.tuya.smart.commercial.lighting.demo.pages.project.model.ProjectIndexModel;
import com.tuya.smart.commercial.lighting.demo.pages.project.view.IProjectIndexView;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.lighting.sdk.bean.LightingProjectConfigsBean;

import java.util.List;

public class ProjectIndexPresenter extends BasePresenter {

    public static final String TAG = ProjectIndexPresenter.class.getSimpleName();

    private IProjectIndexModel mProjectIndexModel;

    private IProjectIndexView iProjectIndexView;


    public ProjectIndexPresenter(final IProjectIndexView iProjectIndexView) {
        super();
        this.iProjectIndexView = iProjectIndexView;
        this.mProjectIndexModel = new ProjectIndexModel(iProjectIndexView.getContext());

        getProjectList();
    }


    public void getProjectList() {
        mProjectIndexModel.getProjectList(new ITuyaGetHomeListCallback() {
            @Override
            public void onSuccess(List<HomeBean> list) {
                Log.i(TAG, "queryProjectList onSuccess: " + JSON.toJSONString(list));
                if (null == iProjectIndexView.getContext()) {
                    return;
                }
                if (CollectionUtils.isEmpty(list)) {
                    iProjectIndexView.showProjectEmptyView();
                } else {
                    iProjectIndexView.setProjectList(list);
                }
            }

            @Override
            public void onError(String s, String s1) {
                Log.e(TAG, "queryProjectList onError: errorCode=" + s);
            }
        });

    }

    public void getProjectTypeList() {
        mProjectIndexModel.getProjectTypeList(new ITuyaResultCallback<List<LightingProjectConfigsBean>>() {
            @Override
            public void onSuccess(List<LightingProjectConfigsBean> result) {
                if (null != result) {
                    iProjectIndexView.getProjectTypeListSuccess(result);
                }
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                Log.e(TAG, "getProjectTypeList onError: errorCode=" + errorMessage);
                iProjectIndexView.getProjectTypeListError();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
