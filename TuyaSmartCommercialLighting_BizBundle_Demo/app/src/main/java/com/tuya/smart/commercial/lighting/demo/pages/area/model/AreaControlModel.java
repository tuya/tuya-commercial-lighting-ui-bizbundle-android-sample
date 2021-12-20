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

import com.tuya.smart.commercial.lighting.demo.bean.AreaBeanWrapper;
import com.tuya.smart.lighting.sdk.api.ILightingStandardAreaDpsManager;
import com.tuya.smart.lighting.sdk.enums.AreaDpMode;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingArea;
import com.tuya.smart.sdk.api.IResultCallback;

public class AreaControlModel {

    public void setDpMode(long projectId, AreaBeanWrapper areaBeanWrapper, AreaDpMode areaDpMode, IResultCallback listener) {
        ILightingStandardAreaDpsManager dpsManager = TuyaCommercialLightingArea
                .newAreaInstance(projectId, areaBeanWrapper.getAreaBean().getAreaId())
                .getLightingStandardAreaDpsManagerInstance();
        dpsManager.requestMode(areaDpMode, listener);
    }


    public void setSwitch(long projectId, AreaBeanWrapper areaBeanWrapper, boolean open, IResultCallback listener) {
        if (null == areaBeanWrapper.getAreaBean()) {
            return;
        }
        ILightingStandardAreaDpsManager lightingStandardAreaDpsManager = TuyaCommercialLightingArea
                .newAreaInstance(projectId, areaBeanWrapper.getAreaBean().getAreaId())
                .getLightingStandardAreaDpsManagerInstance();
        lightingStandardAreaDpsManager.requestSwitchStatus(open, listener);
    }

    public void setBrightness(long projectId, AreaBeanWrapper areaBeanWrapper, int value, IResultCallback listener) {
        if (null == areaBeanWrapper.getAreaBean()) {
            return;
        }
        ILightingStandardAreaDpsManager lightingStandardAreaDpsManager = TuyaCommercialLightingArea
                .newAreaInstance(projectId, areaBeanWrapper.getAreaBean().getAreaId()).getLightingStandardAreaDpsManagerInstance();
        lightingStandardAreaDpsManager.requestBrightPercent(value, listener);

    }

    public void setTemperature(long projectId, AreaBeanWrapper areaBeanWrapper, int temp, IResultCallback listener) {
        if (null == areaBeanWrapper.getAreaBean()) {
            return;
        }
        ILightingStandardAreaDpsManager dpsManager = TuyaCommercialLightingArea
                .newAreaInstance(projectId, areaBeanWrapper.getAreaBean().getAreaId()).getLightingStandardAreaDpsManagerInstance();
        dpsManager.requestTemperaturePercent(temp, listener);
    }

    public void setColorData(long projectId, AreaBeanWrapper areaBeanWrapper, String colorData, IResultCallback listener) {
        if (null == areaBeanWrapper.getAreaBean()) {
            return;
        }
        ILightingStandardAreaDpsManager dpsManager = TuyaCommercialLightingArea
                .newAreaInstance(projectId, areaBeanWrapper.getAreaBean().getAreaId())
                .getLightingStandardAreaDpsManagerInstance();
        dpsManager.requestSetColorData(colorData, listener);
    }
}
