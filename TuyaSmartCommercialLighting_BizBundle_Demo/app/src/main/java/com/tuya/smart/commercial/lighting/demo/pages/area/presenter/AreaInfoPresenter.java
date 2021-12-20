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
import com.tuya.smart.commercial.lighting.demo.pages.area.model.AreaInfoModel;
import com.tuya.smart.commercial.lighting.demo.pages.area.view.IAreaInfoView;
import com.tuya.smart.home.sdk.bean.SimpleAreaBean;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.lighting.sdk.bean.AreaBean;

import java.util.List;

public class AreaInfoPresenter extends BasePresenter {

    public static final String TAG = AreaInfoPresenter.class.getSimpleName();

    private final AreaInfoModel mAreaInfoModel;
    private final AreaAddModel mAreaAddModel;
    private final long mProjectId;
    private final long mAreaId;
    private final IAreaInfoView iAreaInfoView;

    public AreaInfoPresenter(long projectId, long areaId, final IAreaInfoView iAreaInfoView) {
        super();
        this.iAreaInfoView = iAreaInfoView;
        this.mAreaInfoModel = new AreaInfoModel(iAreaInfoView.getContext());
        this.mAreaAddModel = new AreaAddModel(iAreaInfoView.getContext());
        this.mProjectId = projectId;
        this.mAreaId = areaId;

        getAreaInfo();
        getSubAreaList();
    }

    public void getAreaInfo() {
        mAreaInfoModel.getAreaInfo(mProjectId, mAreaId, new ITuyaResultCallback<SimpleAreaBean>() {
            @Override
            public void onSuccess(SimpleAreaBean result) {
                if (null == iAreaInfoView) {
                    return;
                }
                iAreaInfoView.setAreaData(result);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                if (null == iAreaInfoView) {
                    return;
                }
                iAreaInfoView.showToast(errorMessage);
            }
        });
    }

    public void getSubAreaList() {
        mAreaInfoModel.getSubAreaList(mProjectId, mAreaId, new ITuyaResultCallback<List<AreaBean>>() {
            @Override
            public void onSuccess(List<AreaBean> result) {
                if (null == iAreaInfoView) {
                    return;
                }
                iAreaInfoView.setSubAreaList(result);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                if (null == iAreaInfoView) {
                    return;
                }
                iAreaInfoView.showToast(errorMessage);
            }
        });
    }

    public void updateAreaName(String areaName) {
        mAreaInfoModel.updateName(mProjectId, mAreaId, areaName, new ITuyaResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean success) {
                if (null == iAreaInfoView) {
                    return;
                }
                if (success) {
                    iAreaInfoView.updateSuccess(areaName);
                } else {
                    iAreaInfoView.showToast("fail");
                }
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                if (null == iAreaInfoView) {
                    return;
                }
                iAreaInfoView.showToast(errorMessage);
            }
        });
    }

    public void deleteArea() {
        mAreaInfoModel.delete(mProjectId, mAreaId, new ITuyaResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (null == iAreaInfoView) {
                    return;
                }
                iAreaInfoView.doRemoveView(result);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                if (null == iAreaInfoView) {
                    return;
                }
                iAreaInfoView.showToast(errorMessage);
            }
        });
    }

    public void createSubArea(String name) {
        mAreaAddModel.createSubArea(mProjectId, mAreaId, name, new ITuyaResultCallback<SimpleAreaBean>() {
            @Override
            public void onSuccess(SimpleAreaBean result) {
                if (null == iAreaInfoView) {
                    return;
                }
                iAreaInfoView.createSubSuccess(result);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                if (null == iAreaInfoView) {
                    return;
                }
                iAreaInfoView.createFailed(errorMessage);
            }
        });
    }

    public void createOutdoorSubArea(String name, double longitude, double latitude, String address) {
        mAreaAddModel.createSubArea(mProjectId, mAreaId, name, longitude, latitude, address, new ITuyaResultCallback<SimpleAreaBean>() {
            @Override
            public void onSuccess(SimpleAreaBean result) {
                if (null == iAreaInfoView) {
                    return;
                }
                iAreaInfoView.createSubSuccess(result);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                if (null == iAreaInfoView) {
                    return;
                }
                iAreaInfoView.createFailed(errorMessage);
            }
        });
    }

    public void createParentArea(String name) {
        mAreaAddModel.createParentArea(mProjectId, mAreaId, name, new ITuyaResultCallback<SimpleAreaBean>() {
            @Override
            public void onSuccess(SimpleAreaBean result) {
                if (null == iAreaInfoView) {
                    return;
                }
                iAreaInfoView.createParentSuccess(result);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                if (null == iAreaInfoView) {
                    return;
                }
                iAreaInfoView.createFailed(errorMessage);
            }
        });
    }

    public void createOutdoorParentArea(String name, double longitude, double latitude, String address) {
        mAreaAddModel.createParentArea(mProjectId, mAreaId, name, longitude, latitude, address, new ITuyaResultCallback<SimpleAreaBean>() {
            @Override
            public void onSuccess(SimpleAreaBean result) {
                if (null == iAreaInfoView) {
                    return;
                }
                iAreaInfoView.createParentSuccess(result);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                if (null == iAreaInfoView) {
                    return;
                }
                iAreaInfoView.createFailed(errorMessage);
            }
        });
    }
}
