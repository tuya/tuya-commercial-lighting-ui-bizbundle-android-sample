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
import com.tuya.smart.lighting.sdk.bean.AreaBean;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingArea;

import java.util.List;

public class AreaInfoModel extends BaseModel implements IAreaInfoModel {

    public static final String TAG = AreaInfoModel.class.getSimpleName();

    public AreaInfoModel(Context ctx) {
        super(ctx);
    }

    @Override
    public void getSubAreaList(long projectId, long areaId, ITuyaResultCallback<List<AreaBean>> callback) {
        TuyaCommercialLightingArea.newAreaInstance(projectId, areaId).getSubAreaList(callback);
    }

    @Override
    public void getAreaInfo(long projectId, long areaId, ITuyaResultCallback<SimpleAreaBean> callback) {
        TuyaCommercialLightingArea.newAreaInstance(projectId, areaId).getAreaInfo(callback);
    }

    @Override
    public AreaBean getAreaBeanInCache(long projectId, long areaId) {
        return TuyaCommercialLightingArea.newAreaInstance(projectId, areaId).getCurrentAreaBeanCache();
    }

    @Override
    public void updateName(long projectId, long areaId, String name, ITuyaResultCallback<Boolean> callback) {
        TuyaCommercialLightingArea.newAreaInstance(projectId, areaId).updateName(name, callback);
    }

    @Override
    public void delete(long projectId, long areaId, ITuyaResultCallback<Boolean> callback) {
        TuyaCommercialLightingArea.newAreaInstance(projectId, areaId).delete(callback);
    }

    @Override
    public void onDestroy() {

    }
}
