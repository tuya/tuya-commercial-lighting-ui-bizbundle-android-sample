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

package com.tuya.smart.commercial.lighting.demo.pages.area.presenter;


import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.commercial.lighting.demo.pages.area.model.AreaAddModel;
import com.tuya.smart.commercial.lighting.demo.pages.area.model.AreaIndexModel;
import com.tuya.smart.commercial.lighting.demo.pages.area.model.IAreaAddModel;
import com.tuya.smart.commercial.lighting.demo.pages.area.model.IAreaIndexModel;
import com.tuya.smart.commercial.lighting.demo.pages.area.view.IAreaIndexView;
import com.tuya.smart.home.sdk.bean.SimpleAreaBean;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.lighting.sdk.area.bean.AreaListInProjectResponse;
import com.tuya.smart.lighting.sdk.bean.AreaBean;
import com.tuya.smart.lighting.sdk.bean.AreaConfig;

import java.util.List;

public class AreaIndexPresenter extends BasePresenter {

    public static final String TAG = AreaIndexPresenter.class.getSimpleName();

    private final IAreaIndexModel mAreaIndexModel;
    private final IAreaAddModel mAreaAddModel;

    private final IAreaIndexView iAreaIndexView;
    private final long mProjectId;

    public AreaIndexPresenter(long projectId, final IAreaIndexView iAreaIndexView) {
        super();
        this.iAreaIndexView = iAreaIndexView;
        this.mAreaIndexModel = new AreaIndexModel(iAreaIndexView.getContext());
        this.mAreaAddModel = new AreaAddModel(iAreaIndexView.getContext());
        this.mProjectId = projectId;
        getAreaList();
        getAreaLevels(true, true);
        getProjectConfig();
    }

    public void getAreaList() {
        mAreaIndexModel.getAreaList(mProjectId, new ITuyaResultCallback<List<AreaBean>>() {
            @Override
            public void onSuccess(List<AreaBean> result) {
                if (null == iAreaIndexView) {
                    return;
                }
                iAreaIndexView.setAreaList(result);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                if (null == iAreaIndexView) {
                    return;
                }
                iAreaIndexView.showToast(errorMessage);
            }
        });
    }

    public void getAreaLevels(boolean needUnassignedArea, boolean needPublicArea) {
        mAreaIndexModel.getAreaLevels(mProjectId, needUnassignedArea, needPublicArea, new ITuyaResultCallback<AreaListInProjectResponse>() {
            @Override
            public void onSuccess(AreaListInProjectResponse result) {
                if (null == iAreaIndexView) {
                    return;
                }
                iAreaIndexView.setAreaLevels(result.getList());
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                if (null == iAreaIndexView) {
                    return;
                }
                iAreaIndexView.showToast(errorMessage);
            }
        });
    }

    public void getProjectConfig() {
        mAreaAddModel.getProjectConfig(mProjectId, new ITuyaResultCallback<List<AreaConfig>>() {
            @Override
            public void onSuccess(List<AreaConfig> result) {
                if (null == iAreaIndexView) {
                    return;
                }
                iAreaIndexView.setProjectConfig(result);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                if (null == iAreaIndexView) {
                    return;
                }
                iAreaIndexView.showToast(errorMessage);
            }
        });
    }

    public void createArea(String name, int roomLevel) {
        mAreaAddModel.createArea(mProjectId, 0, name, roomLevel, new ITuyaResultCallback<SimpleAreaBean>() {
            @Override
            public void onSuccess(SimpleAreaBean result) {
                if (null == iAreaIndexView) {
                    return;
                }
                iAreaIndexView.doSaveSuccess(result);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                if (null == iAreaIndexView) {
                    return;
                }
                iAreaIndexView.doSaveFailed(errorMessage);
            }
        });
    }

    public void createArea(String name, int roomLevel, double longitude, double latitude, String address) {
        mAreaAddModel.createArea(mProjectId, 0, name, roomLevel, longitude, latitude, address,
                new ITuyaResultCallback<SimpleAreaBean>() {
                    @Override
                    public void onSuccess(SimpleAreaBean result) {
                        if (null == iAreaIndexView) {
                            return;
                        }
                        iAreaIndexView.doSaveSuccess(result);
                    }

                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        if (null == iAreaIndexView) {
                            return;
                        }
                        iAreaIndexView.doSaveFailed(errorMessage);
                    }
                });
    }
}
