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
import com.tuya.smart.commercial.lighting.demo.pages.project.model.IRegionListModel;
import com.tuya.smart.commercial.lighting.demo.pages.project.model.RegionListModel;
import com.tuya.smart.commercial.lighting.demo.pages.project.view.IRegionListView;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.lighting.sdk.bean.LightingRegionListBean;

import java.util.List;


public class RegionListPresenter extends BasePresenter {


    private IRegionListModel mRegionListModel;
    private IRegionListView mRegionListView;

    public RegionListPresenter(IRegionListView view) {
        super(view.getContext());
        this.mRegionListView = view;
        this.mRegionListModel = new RegionListModel();
    }

    public void getRegionList() {
        mRegionListModel.getRegionList(new ITuyaResultCallback<List<LightingRegionListBean>>() {
            @Override
            public void onSuccess(List<LightingRegionListBean> result) {
                if (result != null) {
                    mRegionListView.getRegionListSuccess(result);
                }
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                mRegionListView.getRegionListError(errorMessage);
            }
        });
    }

    public List<LightingRegionListBean> getSubRegionList(String regionId) {
        return mRegionListModel.getSubRegionFromCache(regionId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
