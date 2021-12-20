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

package com.tuya.smart.commercial.lighting.demo.utils;

import com.tuya.sdk.device.enums.RomUpgradeStatusEnum;
import com.tuya.sdk.device.enums.RomUpgradeTypeEnum;
import com.tuya.smart.commercial.lighting.demo.bean.UpgradeStatusEnum;
import com.tuya.smart.android.device.bean.HardwareUpgradeBean;
import com.tuya.smart.android.device.bean.UpgradeInfoBean;

public class FirmwareUtils {


    public static boolean hasHardwareUpdate(HardwareUpgradeBean upgradeInfoBean) {
        return hasGWHardwareUpdate(upgradeInfoBean) || hasDeviceHardwareUpdate(upgradeInfoBean);
    }

    public static boolean hasGWHardwareUpdateWithoutCheck(HardwareUpgradeBean upgradeBean) {
        return hasGWHardwareUpdate(upgradeBean) && !isHardwareCheck(upgradeBean.getGw());
    }

    public static boolean hasDeviceHardwareUpdateWithoutCheck(HardwareUpgradeBean upgradeBean) {
        return hasDeviceHardwareUpdate(upgradeBean) && !isHardwareCheck(upgradeBean.getDev());
    }

    public static boolean hasGWHardwareUpdate(HardwareUpgradeBean upgradeBean) {
        UpgradeInfoBean gw = upgradeBean.getGw();
        return gw != null && RomUpgradeStatusEnum.to(gw.getUpgradeStatus()) == RomUpgradeStatusEnum.NEW_VERSION;
    }

    public static boolean hasDeviceHardwareUpdate(HardwareUpgradeBean upgradeBean) {
        UpgradeInfoBean dev = upgradeBean.getDev();
        return dev != null && RomUpgradeStatusEnum.to(dev.getUpgradeStatus()) == RomUpgradeStatusEnum.NEW_VERSION;
    }

    public static boolean isHardwareUpdating(UpgradeInfoBean upgradeBean) {
        return upgradeBean != null && RomUpgradeStatusEnum.to(upgradeBean.getUpgradeStatus()) == RomUpgradeStatusEnum.UPGRADING;
    }

    public static boolean isHardwareUpdating(HardwareUpgradeBean upgradeBean) {
        return upgradeBean != null && (isHardwareUpdating(upgradeBean.getDev()) || isHardwareUpdating(upgradeBean.getGw()));
    }

    public static boolean isHardwareForced(UpgradeInfoBean upgradeInfoBean) {
        return upgradeInfoBean != null && RomUpgradeTypeEnum.to(upgradeInfoBean.getUpgradeType()) == RomUpgradeTypeEnum.FORCED;
    }

    public static boolean isHardwareCheck(UpgradeInfoBean upgradeInfoBean) {
        return upgradeInfoBean != null && RomUpgradeTypeEnum.to(upgradeInfoBean.getUpgradeType()) == RomUpgradeTypeEnum.CHECK;
    }


    public static UpgradeInfoBean getUpgradingDevice(HardwareUpgradeBean upgradeInfoBean) {
        UpgradeInfoBean dev = upgradeInfoBean.getDev();
        if (FirmwareUtils.isHardwareUpdating(dev)) {
            return dev;
        }
        UpgradeInfoBean gw = upgradeInfoBean.getGw();
        if (FirmwareUtils.isHardwareUpdating(gw)) {
            return gw;
        }
        return null;
    }

    public static boolean hasHardwareUpdateWithoutCheck(HardwareUpgradeBean upgradeInfoBean) {
        return hasGWHardwareUpdateWithoutCheck(upgradeInfoBean) || hasDeviceHardwareUpdateWithoutCheck(upgradeInfoBean);
    }

    public static UpgradeStatusEnum getHardwareUpgradeStatus(HardwareUpgradeBean upgradeBean) {
        UpgradeInfoBean upgradingDevice = FirmwareUtils.getUpgradingDevice(upgradeBean);
        if (upgradingDevice != null) {
            return UpgradeStatusEnum.UPGRADING;
        }
        if (!FirmwareUtils.hasHardwareUpdate(upgradeBean))
            return UpgradeStatusEnum.NO_UPGRADE;
        if (FirmwareUtils.hasHardwareUpdateWithoutCheck(upgradeBean)) {
            return UpgradeStatusEnum.HAS_FORCE_OR_REMIND_UPGRADE;
        }
        return UpgradeStatusEnum.HAS_CHECK_UPGRADE;
    }
}