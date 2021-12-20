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

package com.tuya.smart.commercial.lighting.demo.bean;

import com.tuya.smart.lighting.sdk.enums.AreaDpMode;
import com.tuya.smart.lighting.sdk.enums.LightDefaultSceneType;

public class LightingDpsWrapper {
    private int brightness;
    private boolean switchOpen;

    private boolean defaultArea;

    private int temperaturePercent;

    private AreaDpMode areaDpMode;

    private LightDefaultSceneType currentSceneType = LightDefaultSceneType.NONE;

    private String colorData;

    private String dps;

    private String name;

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public boolean isSwitchOpen() {
        return switchOpen;
    }

    public void setSwitchOpen(boolean switchOpen) {
        this.switchOpen = switchOpen;
    }

    public boolean isDefaultArea() {
        return defaultArea;
    }

    public void setDefaultArea(boolean defaultArea) {
        this.defaultArea = defaultArea;
    }

    public int getTemperaturePercent() {
        return temperaturePercent;
    }

    public void setTemperaturePercent(int temperaturePercent) {
        this.temperaturePercent = temperaturePercent;
    }

    public AreaDpMode getAreaDpMode() {
        return areaDpMode;
    }

    public void setAreaDpMode(AreaDpMode areaDpMode) {
        this.areaDpMode = areaDpMode;
    }

    public LightDefaultSceneType getCurrentSceneType() {
        return currentSceneType;
    }

    public void setCurrentSceneType(LightDefaultSceneType currentSceneType) {
        this.currentSceneType = currentSceneType;
    }

    public String getColorData() {
        return colorData;
    }

    public void setColorData(String colorData) {
        this.colorData = colorData;
    }

    public String getDps() {
        return dps;
    }

    public void setDps(String dps) {
        this.dps = dps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void reset() {
        brightness = 0;
        switchOpen = false;
//        viewType = 0;
    }
}
