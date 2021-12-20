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

import android.app.Activity;

import com.tuya.sdk.core.PluginManager;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingArea;
import com.tuya.smart.api.service.MicroServiceManager;
import com.tuya.smart.home.sdk.api.ITuyaGroupModel;
import com.tuya.smart.home.sdk.bean.AreaGroupListRespBean;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.interior.api.ITuyaGroupPlugin;
import com.tuya.smart.lighting.sdk.api.ILightingArea;
import com.tuya.smart.lighting.sdk.bean.GroupPackListBean;
import com.tuya.smart.panelcaller.api.AbsPanelCallerService;
import com.tuya.smart.sdk.bean.GroupBean;

public class LightingGroupUseCase {
    ITuyaGroupPlugin mGroupPlugin = null;
    private final ITuyaGroupModel mGroupModel;
    private final AbsPanelCallerService mAbsPanelCallerService;

    public LightingGroupUseCase() {
        mGroupPlugin = PluginManager.service(ITuyaGroupPlugin.class);
        mGroupModel = mGroupPlugin.newGroupModelInstance();
        mAbsPanelCallerService = MicroServiceManager.getInstance().findServiceByInterface(AbsPanelCallerService.class.getName());
    }

    public void getGroupDeviceByAreaId(long homeId, long areaId, String offsetKey, ITuyaResultCallback<AreaGroupListRespBean> callback) {
        if (mGroupModel == null) {
            return;
        }
        mGroupModel.queryGroupListByAreaId(homeId, areaId, offsetKey, callback);
    }

    public void getGroupPackListByAreaId(long homeId, long areaId, String offsetKey, final ITuyaResultCallback<GroupPackListBean> callback) {
        ILightingArea lightingArea = TuyaCommercialLightingArea.newAreaInstance(homeId, areaId);
        if (lightingArea == null) {
            callback.onError("", "lighting area cannot be null");
            return;
        }

        lightingArea.getPackedGroupList(20, offsetKey, callback);
    }

    public void openGroupPanel(Activity activity, GroupBean groupBean, boolean isAdmin) {
        mAbsPanelCallerService.goPanel(activity, groupBean, true);
    }
}
