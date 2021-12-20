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

package com.tuya.smart.commercial.lighting.demo.pages.config;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.tuya.smart.android.common.utils.NetworkUtil;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.pages.config.view.ECActivity;
import com.tuya.smart.commercial.lighting.demo.pages.config.view.ECBindActivity;
import com.tuya.smart.commercial.lighting.demo.pages.config.view.IECView;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;
import com.tuya.smart.commercial.lighting.demo.utils.DialogUtil;
import com.tuya.smart.commercial.lighting.demo.utils.ToastUtil;
import com.tuya.smart.android.device.utils.WiFiUtil;
import com.tuya.smart.android.mvp.presenter.BasePresenter;

import static com.tuya.smart.commercial.lighting.demo.app.IntentExtra.KEY_AREA_ID;
import static com.tuya.smart.commercial.lighting.demo.app.IntentExtra.KEY_PROJECT_ID;

public class ECPresenter extends BasePresenter {
    //    public static final int REQUEST_CODE = 12;
    public static final int PRIVATE_CODE = 1315;//开启GPS权限
    public static final int CODE_FOR_LOCATION_PERMISSION = 222;
    public static final String TAG = "ECPresenter";
    private final int mMode;

    private Activity mActivity;
    private IECView mView;
    private long projectId;
    private long areaId;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, final Intent intent) {
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                checkWifiNetworkStatus();
            }
        }
    };

    private boolean isWifiDisabled() {
        final WifiManager mWifiManager = (WifiManager) mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return mWifiManager != null && !mWifiManager.isWifiEnabled();
    }

    public void showLocationError() {
        String ssid = mView.getWifiSsId();
        if (!TextUtils.isEmpty(ssid)) {
            return;
        } else {
            if (isWifiDisabled()) {
                return;
            }
            if (!checkSystemGPSLocation()) {
                return;
            } else {
                checkSinglePermission(Manifest.permission.ACCESS_FINE_LOCATION, CODE_FOR_LOCATION_PERMISSION);
            }
        }
    }

    public boolean checkSystemGPSLocation() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isOpen) {
            new AlertDialog.Builder(mActivity)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle(R.string.ty_simple_confirm_title)
                    .setMessage(R.string.ty_notify_location_setup)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.setup, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            mActivity.startActivityForResult(intent, PRIVATE_CODE);
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        }
        return isOpen;
    }

    public boolean checkSinglePermission(String permission, int resultCode) {
        boolean hasPermission;
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        } else {
            hasPermission = hasPermission(permission);
        }

        if (!hasPermission) {
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
//                new AlertDialog.Builder(mActivity)
//                        .setIcon(android.R.drawable.ic_dialog_info)
//                        .setTitle(R.string.ty_simple_confirm_title)
//                        .setMessage(R.string.wifi_to_reopen_permission_location)
//                        .setNegativeButton(R.string.cancel,null)
//                        .setPositiveButton(R.string.setup, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //引导用户至设置页手动授权
//                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                Uri uri = Uri.fromParts("package", mActivity.getApplicationContext().getPackageName(), null);
//                                intent.setData(uri);
//                                mActivity.startActivity(intent);
//                                dialogInterface.dismiss();
//                            }
//                        })
//                        .show();
//                return false;
//            }
            ActivityCompat.requestPermissions(mActivity, new String[]{permission},
                    resultCode);
            return false;
        }

        return true;
    }

    public boolean hasPermission(String permission) {
        int targetSdkVersion = 0;
        try {
            final PackageInfo info = mActivity.getPackageManager().getPackageInfo(
                    mActivity.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        boolean result = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = ContextCompat.checkSelfPermission(mActivity, permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(mActivity, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;
    }


    public void checkWifiNetworkStatus() {
        if (NetworkUtil.isNetworkAvailable(mActivity)) {
            String currentSSID = WiFiUtil.getCurrentSSID(mActivity);
            if (!TextUtils.isEmpty(currentSSID)) {
                mView.setWifiSSID(currentSSID);
                if (is5GHz(currentSSID, mActivity)) {
                    mView.show5gTip();
                } else {
                    mView.hide5gTip();
                }
                return;
            }
        }
        mView.showNoWifi();
    }

    public ECPresenter(Activity activity, IECView view, long projectId, long areaId) {
        mActivity = activity;
        mView = view;
        mMode = activity.getIntent().getIntExtra(ECActivity.CONFIG_MODE, ECActivity.EC_MODE);
        this.projectId = projectId;
        this.areaId = areaId;
        initWifi();
    }

    public void closeECModeActivity() {
        mActivity.onBackPressed();
    }

    private void initWifi() {
        registerWifiReceiver();
    }

    public void goNextStep() {
        final String passWord = mView.getWifiPass();
        final String ssid = WiFiUtil.getCurrentSSID(mActivity);
        if (!NetworkUtil.isNetworkAvailable(mActivity) || TextUtils.isEmpty(ssid)) {
            ToastUtil.showToast(mActivity, R.string.connect_phone_to_network);
        } else {
            if (!is5GHz(ssid, mActivity)) {
                gotoBindDeviceActivity(ssid, passWord);
            } else {
                DialogUtil.customDialog(mActivity, null, mActivity.getString(R.string.ez_notSupport_5G_tip)
                        , mActivity.getString(R.string.ez_notSupport_5G_change), mActivity.getString(R.string.ez_notSupport_5G_continue), null, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        userOtherWifi();
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        gotoBindDeviceActivity(ssid, passWord);
                                        break;
                                }
                            }
                        }).show();
            }
        }
    }

    public static boolean is5GHz(String ssid, Context context) {
        WifiManager wifiManger = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManger.getConnectionInfo();
        if (wifiInfo != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int freq = wifiInfo.getFrequency();
            return freq > 4900 && freq < 5900;
        } else return ssid.toUpperCase().endsWith("5G");
    }

    private void gotoBindDeviceActivity(String ssid, String passWord) {
        Intent intent = new Intent(mActivity, ECBindActivity.class);
        intent.putExtra(ECActivity.CONFIG_PASSWORD, passWord);
        intent.putExtra(ECActivity.CONFIG_SSID, ssid);
        intent.putExtra(ECActivity.CONFIG_MODE, mMode);
        intent.putExtra(KEY_AREA_ID, areaId);
        intent.putExtra(KEY_PROJECT_ID, projectId);
        ActivityUtils.startActivity(mActivity, intent, ActivityUtils.ANIMATE_FORWARD, true);
    }

    private void registerWifiReceiver() {
        try {
            mActivity.registerReceiver(mBroadcastReceiver, new IntentFilter(
                    WifiManager.NETWORK_STATE_CHANGED_ACTION));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unRegisterWifiReceiver() {
        try {
            if (mBroadcastReceiver != null) {
                mActivity.unregisterReceiver(mBroadcastReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterWifiReceiver();
    }

    public void userOtherWifi() {
        Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
        if (null != wifiSettingsIntent.resolveActivity(mActivity.getPackageManager())) {
            mActivity.startActivity(wifiSettingsIntent);
        } else {
            wifiSettingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            if (null != wifiSettingsIntent.resolveActivity(mActivity.getPackageManager())) {
                mActivity.startActivity(wifiSettingsIntent);
            }
        }
    }
}
