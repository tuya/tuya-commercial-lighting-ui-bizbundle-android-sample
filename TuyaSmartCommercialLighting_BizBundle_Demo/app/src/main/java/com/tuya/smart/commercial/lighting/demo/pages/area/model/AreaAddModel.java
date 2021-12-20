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

package com.tuya.smart.commercial.lighting.demo.pages.area.model;


import android.content.Context;

import com.tuya.smart.android.mvp.model.BaseModel;
import com.tuya.smart.home.sdk.bean.SimpleAreaBean;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.lighting.sdk.bean.AreaConfig;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingArea;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingProject;

import java.util.List;

public class AreaAddModel extends BaseModel implements IAreaAddModel {

    public static final String TAG = AreaAddModel.class.getSimpleName();

    public AreaAddModel(Context ctx) {
        super(ctx);
    }

    @Override
    public void getProjectConfig(long projectId, ITuyaResultCallback<List<AreaConfig>> callback) {
        TuyaCommercialLightingProject.getLightingProjectManager().getProjectConfig(projectId, callback);
    }

    @Override
    public void createArea(long projectId, long currentAreaId, String name, int roomLevel, ITuyaResultCallback<SimpleAreaBean> callback) {
        TuyaCommercialLightingArea.getLightingAreaManager().createArea(projectId, currentAreaId, name, roomLevel, callback);
    }

    @Override
    public void createArea(long projectId, long currentAreaId, String name, int roomLevel, double longitude, double latitude, String address, ITuyaResultCallback<SimpleAreaBean> callback) {
        TuyaCommercialLightingArea.getLightingAreaManager().createArea(projectId, currentAreaId, name, roomLevel, longitude, latitude, address, callback);
    }

    @Override
    public void createSubArea(long projectId, long areaId, String subAreaName, ITuyaResultCallback<SimpleAreaBean> callback) {
        TuyaCommercialLightingArea.newAreaInstance(projectId, areaId).createSubArea(subAreaName, callback);
    }

    @Override
    public void createSubArea(long projectId, long areaId, String subAreaName, double longitude, double latitude, String address, ITuyaResultCallback<SimpleAreaBean> callback) {
        TuyaCommercialLightingArea.newAreaInstance(projectId, areaId).createSubArea(subAreaName, longitude, latitude, address, callback);
    }

    @Override
    public void createParentArea(long projectId, long areaId, String parentAreaName, ITuyaResultCallback<SimpleAreaBean> callback) {
        TuyaCommercialLightingArea.newAreaInstance(projectId, areaId).createParentArea(parentAreaName, callback);
    }

    @Override
    public void createParentArea(long projectId, long areaId, String parentAreaName, double longitude, double latitude, String address, ITuyaResultCallback<SimpleAreaBean> callback) {
        TuyaCommercialLightingArea.newAreaInstance(projectId, areaId).createParentArea(parentAreaName, longitude, latitude, address, callback);
    }

    @Override
    public void onDestroy() {

    }
}
