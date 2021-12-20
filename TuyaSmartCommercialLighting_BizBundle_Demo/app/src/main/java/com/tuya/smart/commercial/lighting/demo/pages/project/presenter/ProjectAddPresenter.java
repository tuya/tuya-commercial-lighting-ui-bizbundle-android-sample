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
import com.tuya.smart.commercial.lighting.demo.pages.project.model.IProjectAddModel;
import com.tuya.smart.commercial.lighting.demo.pages.project.model.ProjectAddModel;
import com.tuya.smart.commercial.lighting.demo.pages.project.view.IProjectAddView;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;

public class ProjectAddPresenter extends BasePresenter {


    private IProjectAddModel mProjectAddModel;
    private IProjectAddView mProjectAddView;

    public ProjectAddPresenter(IProjectAddView view) {
        super(view.getContext());
        this.mProjectAddView = view;
        this.mProjectAddModel = new ProjectAddModel();
    }


    public void addProject(int projectType, String projectName, String leaderName, String leaderMobile, String detailAddress, String regionId) {
        mProjectAddModel.createProject(projectType, projectName, leaderName, leaderMobile, detailAddress, regionId, new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean homeBean) {
                if (null == mProjectAddView) {
                    return;
                }
                mProjectAddView.doSaveSuccess();
            }

            @Override
            public void onError(String s, String s1) {
                if (null == mProjectAddView) {
                    return;
                }
                mProjectAddView.doSaveFailed(s1);
            }
        });
    }
}
