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

import com.tuya.smart.commercial.lighting.demo.pages.area.model.AreaControlModel;
import com.tuya.smart.commercial.lighting.demo.pages.area.model.AreaInfoModel;
import com.tuya.smart.commercial.lighting.demo.pages.area.view.IAreaControlView;
import com.tuya.smart.commercial.lighting.demo.bean.AreaBeanWrapper;
import com.tuya.smart.commercial.lighting.demo.pages.area.util.AreaBeanConverter;
import com.tuya.smart.lighting.sdk.bean.AreaBean;
import com.tuya.smart.lighting.sdk.enums.AreaDpMode;
import com.tuya.smart.sdk.api.IResultCallback;

public class AreaControlPresenter {
    private long projectId;
    private long areaId;
    private AreaBeanWrapper areaBeanWrapper;
    private AreaControlModel areaControlModel = new AreaControlModel();
    private final AreaInfoModel areaInfoModel = new AreaInfoModel(null);
    private IAreaControlView view;

    public AreaControlPresenter(long projectId, long areaId, IAreaControlView view) {
        this.projectId = projectId;
        this.areaId = areaId;
        this.view = view;
        init();
    }

    private void init() {
        if (areaBeanWrapper == null) {
            AreaBean areaBean = areaInfoModel.getAreaBeanInCache(projectId, areaId);
            if (areaBean == null) {
                throw new IllegalArgumentException("areaBean cannot be null");
            }

            areaBeanWrapper = AreaBeanConverter.convertAreaBean2Wrapper(projectId, areaBean);

            view.setAreaInfo(areaBeanWrapper);
        }
    }

    public void setDpMode(AreaDpMode areaDpMode) {
        init();
        areaControlModel.setDpMode(projectId, areaBeanWrapper, areaDpMode, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                view.toast(error);
            }

            @Override
            public void onSuccess() {
                areaBeanWrapper.setAreaDpMode(areaDpMode);
                view.setAreaInfo(areaBeanWrapper);
            }
        });
    }

    /**
     * 设置开关
     *
     * @param open
     */
    public void setSwitch(boolean open) {
        init();
        areaControlModel.setSwitch(projectId, areaBeanWrapper, open, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                view.toast(error);
            }

            @Override
            public void onSuccess() {
                areaBeanWrapper.setSwitchOpen(open);
                view.setAreaInfo(areaBeanWrapper);
            }
        });
    }

    /**
     * 设置亮度
     *
     * @param value
     */
    public void setBrightness(int value) {
        init();
        areaControlModel.setBrightness(projectId, areaBeanWrapper, value, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                view.toast(error);
            }

            @Override
            public void onSuccess() {
                areaBeanWrapper.setBrightness(value);
                view.setAreaInfo(areaBeanWrapper);
            }
        });
    }

    /**
     * 设置色温
     *
     * @param temp
     */
    public void setTemperature(int temp) {
        init();
        areaControlModel.setTemperature(projectId, areaBeanWrapper, temp, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                view.toast(error);
            }

            @Override
            public void onSuccess() {
                areaBeanWrapper.setTemperaturePercent(temp);
                view.setAreaInfo(areaBeanWrapper);
            }
        });
    }

    /**
     * 设置彩光
     *
     * @param colorData
     */
    public void setColorData(String colorData) {
        init();
        areaControlModel.setColorData(projectId, areaBeanWrapper, colorData, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                view.toast(error);
            }

            @Override
            public void onSuccess() {
                areaBeanWrapper.setColorData(colorData);
                view.setAreaInfo(areaBeanWrapper);
            }
        });
    }
}
