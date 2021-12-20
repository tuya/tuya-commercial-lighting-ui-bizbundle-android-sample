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


import com.tuya.smart.home.sdk.bean.SimpleAreaBean;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.lighting.sdk.bean.AreaConfig;

import java.util.List;

public interface IAreaAddModel {

    void getProjectConfig(long projectId, ITuyaResultCallback<List<AreaConfig>> callback);

    void createArea(long projectId, long currentAreaId, String name, int roomLevel, ITuyaResultCallback<SimpleAreaBean> callback);

    void createArea(long projectId, long currentAreaId, String name, int roomLevel, double longitude, double latitude, String address, ITuyaResultCallback<SimpleAreaBean> callback);

    void createSubArea(long projectId, long areaId, String subAreaName, ITuyaResultCallback<SimpleAreaBean> callback);

    void createSubArea(long projectId, long areaId, String subAreaName, double longitude, double latitude, String address, ITuyaResultCallback<SimpleAreaBean> callback);

    void createParentArea(long projectId, long areaId, String parentAreaName, ITuyaResultCallback<SimpleAreaBean> callback);

    void createParentArea(long projectId, long areaId, String parentAreaName, double longitude, double latitude, String address, ITuyaResultCallback<SimpleAreaBean> callback);
}