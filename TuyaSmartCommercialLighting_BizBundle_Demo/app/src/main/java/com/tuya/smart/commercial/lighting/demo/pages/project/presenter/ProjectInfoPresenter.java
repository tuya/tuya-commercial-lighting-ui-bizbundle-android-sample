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


import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.commercial.lighting.demo.pages.project.model.IProjectInfoModel;
import com.tuya.smart.commercial.lighting.demo.pages.project.model.ProjectInfoModel;
import com.tuya.smart.commercial.lighting.demo.pages.project.view.IProjectInfoView;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingBLE;
import com.tuya.smart.sdk.api.IResultCallback;

public class ProjectInfoPresenter extends BasePresenter {

    private IProjectInfoView mIProjectInfoView;

    private IProjectInfoModel mProjectInfoModel;

    private HomeBean mHomeBean;

    public ProjectInfoPresenter(IProjectInfoView view) {
        super(view.getContext());
        this.mIProjectInfoView = view;
        this.mProjectInfoModel = new ProjectInfoModel(view.getContext());

        getHomeInfo();
    }

    private void getHomeInfo() {
        mProjectInfoModel.getProjectDetail(mIProjectInfoView.getProjectId(), new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean homeBean) {
                if (null == mIProjectInfoView) {
                    return;
                }
                mHomeBean = homeBean;
                mIProjectInfoView.setProjectData(mHomeBean);

                if (homeBean.getSigMeshList() != null && homeBean.getSigMeshList().size() > 0) {
                    TuyaCommercialLightingBLE.getTuyaSigMeshClient().startClient(homeBean.getSigMeshList().get(0));
                }
            }

            @Override
            public void onError(String s, String s1) {
                if (null == mIProjectInfoView) {
                    return;
                }
                mIProjectInfoView.showToast(s1);
            }
        });
    }

    public void updateProjectName(String content) {
        mProjectInfoModel.updateProjectInfo(mHomeBean.getHomeId(), content, mHomeBean.getLeaderName(), mHomeBean.getLeaderMobile(), mHomeBean.getRegionLocationId(), mHomeBean.getDetail(), new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                if (null == mIProjectInfoView) {
                    return;
                }
                mIProjectInfoView.showToast(error);
            }

            @Override
            public void onSuccess() {
                if (null == mIProjectInfoView) {
                    return;
                }
                mIProjectInfoView.updateProjectName(content);
            }
        });
    }

    public void updateLeaderName(String content) {
        mProjectInfoModel.updateProjectInfo(mHomeBean.getHomeId(), mHomeBean.getName(), content, mHomeBean.getLeaderMobile(), mHomeBean.getRegionLocationId(), mHomeBean.getDetail(), new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                if (null == mIProjectInfoView) {
                    return;
                }
                mIProjectInfoView.showToast(error);
            }

            @Override
            public void onSuccess() {
                if (null == mIProjectInfoView) {
                    return;
                }
                mIProjectInfoView.updateLeaderName(content);
            }
        });
    }

    public void updateLeaderMobile(String content) {
        mProjectInfoModel.updateProjectInfo(mHomeBean.getHomeId(), mHomeBean.getName(), mHomeBean.getLeaderName(), content, mHomeBean.getRegionLocationId(), mHomeBean.getDetail(), new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                if (null == mIProjectInfoView) {
                    return;
                }
                mIProjectInfoView.showToast(error);
            }

            @Override
            public void onSuccess() {
                if (null == mIProjectInfoView) {
                    return;
                }
                mIProjectInfoView.updateLeaderMobile(content);
            }
        });
    }

    public void updateAddressDetail(String content) {
        mProjectInfoModel.updateProjectInfo(mHomeBean.getHomeId(), mHomeBean.getName(), mHomeBean.getLeaderName(), mHomeBean.getLeaderMobile(), mHomeBean.getRegionLocationId(), content, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                if (null == mIProjectInfoView) {
                    return;
                }
                mIProjectInfoView.showToast(error);
            }

            @Override
            public void onSuccess() {
                if (null == mIProjectInfoView) {
                    return;
                }
                mIProjectInfoView.updateAddressDetail(content);
            }
        });
    }

    public void updateOutdoorAddressDetail(String regionId, String content) {
        mProjectInfoModel.updateProjectInfo(mHomeBean.getHomeId(), mHomeBean.getName(), mHomeBean.getLeaderName(), mHomeBean.getLeaderMobile(), regionId, content, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                if (null == mIProjectInfoView) {
                    return;
                }
                mIProjectInfoView.showToast(error);
            }

            @Override
            public void onSuccess() {
                if (null == mIProjectInfoView) {
                    return;
                }
                mIProjectInfoView.updateAddressDetail(content);
            }
        });
    }

    public void remove(String password) {
        mProjectInfoModel.delete(mIProjectInfoView.getProjectId(), password, new IResultCallback() {
            @Override
            public void onError(String s, String s1) {
                if (null == mIProjectInfoView) {
                    return;
                }
                mIProjectInfoView.showToast(s1);
            }

            @Override
            public void onSuccess() {
                if (null == mIProjectInfoView) {
                    return;
                }
                mIProjectInfoView.doRemoveView(true);
            }
        });
    }


    @Override
    public void onDestroy() {
        mHomeBean = null;
        super.onDestroy();
    }
}
