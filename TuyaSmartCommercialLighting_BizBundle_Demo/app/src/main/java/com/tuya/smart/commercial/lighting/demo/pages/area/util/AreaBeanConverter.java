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

package com.tuya.smart.commercial.lighting.demo.pages.area.util;

import com.tuya.smart.commercial.lighting.demo.bean.AreaBeanDps;
import com.tuya.smart.commercial.lighting.demo.bean.AreaBeanWrapper;
import com.tuya.smart.lighting.sdk.api.ILightingStandardAreaDpsManager;
import com.tuya.smart.lighting.sdk.bean.AreaBean;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingArea;

public class AreaBeanConverter {

    public static AreaBeanWrapper convertAreaBean2Wrapper(long projectId, AreaBean bean) {
        AreaBeanWrapper wrapper = new AreaBeanWrapper(bean);
        AreaBeanDps areaDps = getAreaBeanDps(projectId, bean);

        if (areaDps != null) {
            wrapper.setBrightness(areaDps.getBrightnessPercent());
            wrapper.setSwitchOpen(areaDps.getSwitchStatus());
            wrapper.setCurrentSceneType(areaDps.getCurrentSceneType());
            wrapper.setTemperaturePercent(areaDps.getTemperaturePercent());
            wrapper.setAreaDpMode(areaDps.getAreaDpMode());
            wrapper.setColorData(areaDps.getColorData());
            wrapper.setDps(bean.getDps());
            wrapper.setName(bean.getName());
        }
        return wrapper;
    }

    /**
     * 获取区域dps
     *
     * @param areaBean
     */
    public static AreaBeanDps getAreaBeanDps(long projectId, AreaBean areaBean) {
        AreaBeanDps areaBeanDps = new AreaBeanDps();
        try {
            ILightingStandardAreaDpsManager areaDpsManager = TuyaCommercialLightingArea
                    .newAreaInstance(projectId, areaBean.getAreaId()).getLightingStandardAreaDpsManagerInstance();
            areaBeanDps.setBrightnessPercent(areaDpsManager.getBrightPercent());
            areaBeanDps.setSwitchStatus(areaDpsManager.getSwitchStatus());
            areaBeanDps.setTemperaturePercent(areaDpsManager.getTemperaturePercent());
            areaBeanDps.setAreaDpMode(areaDpsManager.getCurrentMode());
            areaBeanDps.setCurrentSceneType(areaDpsManager.getSceneStatus());
            areaBeanDps.setColorData(areaDpsManager.getColorData());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return areaBeanDps;
    }
}
