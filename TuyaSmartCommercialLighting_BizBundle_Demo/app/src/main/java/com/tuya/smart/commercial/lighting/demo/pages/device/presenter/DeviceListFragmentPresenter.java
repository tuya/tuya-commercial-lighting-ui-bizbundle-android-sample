package com.tuya.smart.commercial.lighting.demo.pages.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.tuya.smart.android.base.event.NetWorkStatusEvent;
import com.tuya.smart.android.base.event.NetWorkStatusEventModel;
import com.tuya.smart.android.base.utils.PreferencesUtil;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.api.MicroContext;
import com.tuya.smart.commercial.lighting.demo.app.Constant;
import com.tuya.smart.commercial.lighting.demo.pages.BrowserActivity;
import com.tuya.smart.commercial.lighting.demo.pages.device.view.DeviceListFragment;
import com.tuya.smart.commercial.lighting.demo.pages.device.view.IDeviceListFragmentView;
import com.tuya.smart.commercial.lighting.demo.utils.ActivityUtils;
import com.tuya.smart.commercial.lighting.demo.utils.DialogUtil;
import com.tuya.smart.commercial.lighting.demo.utils.ToastUtil;
import com.tuya.smart.commercial.lighting.demo.pages.device.view.CommonDeviceDebugActivity;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.commercial.lighting.demo.utils.ProgressUtil;
import com.tuya.smart.commercial.lighting.demo.pages.config.view.AddDeviceTypeActivity;
import com.tuya.smart.commercial.lighting.demo.pages.config.CommonConfig;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.interior.device.bean.DeviceRespBean;
import com.tuya.smart.lighting.sdk.api.ILightingDevice;
import com.tuya.smart.lighting.sdk.bean.LightingDeviceListBean;
import com.tuya.sdk.os.TuyaOSDevice;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingArea;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingDevice;
import com.tuya.smart.panelcaller.api.AbsPanelCallerService;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.sdk.api.IDevListener;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;

public class DeviceListFragmentPresenter extends BasePresenter implements NetWorkStatusEvent {

    private static final String TAG = "DeviceListFragmentPresenter";
    private static final int WHAT_JUMP_GROUP_PAGE = 10212;
    protected Activity mActivity;
    protected IDeviceListFragmentView mView;
    private long mProjectId;
    private long mAreaId;

    public DeviceListFragmentPresenter(DeviceListFragment fragment, IDeviceListFragmentView view) {
        mActivity = fragment.getActivity();
        mView = view;
        TuyaSdk.getEventBus().register(this);
        Constant.HOME_ID = PreferencesUtil.getLong("homeId", Constant.HOME_ID);
    }

    public void setParams(long projectId, long areaId) {
        this.mProjectId = projectId;
        this.mAreaId = areaId;
    }

    public void getData() {
        mView.loadStart();
        getDataFromServer();
    }

    private void showDevIsNotOnlineTip(final DeviceBean deviceBean) {
        final boolean isShared = deviceBean.isShare;
        DialogUtil.customDialog(mActivity, mActivity.getString(R.string.title_device_offline), mActivity.getString(R.string.content_device_offline),
                mActivity.getString(isShared ? R.string.ty_offline_delete_share : R.string.cancel_connect),
                mActivity.getString(R.string.right_button_device_offline), mActivity.getString(R.string.left_button_device_offline), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                DialogUtil.simpleConfirmDialog(mActivity, mActivity.getString(R.string.device_confirm_remove), new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == DialogInterface.BUTTON_POSITIVE) {
                                            unBindDevice(deviceBean);
                                        }
                                    }
                                });
                                break;
                            case DialogInterface.BUTTON_NEUTRAL:
//                                //重置说明
                                Intent intent = new Intent(mActivity, BrowserActivity.class);
                                intent.putExtra(BrowserActivity.EXTRA_LOGIN, false);
                                intent.putExtra(BrowserActivity.EXTRA_REFRESH, true);
                                intent.putExtra(BrowserActivity.EXTRA_TOOLBAR, true);
                                intent.putExtra(BrowserActivity.EXTRA_TITLE, mActivity.getString(R.string.left_button_device_offline));
                                intent.putExtra(BrowserActivity.EXTRA_URI, CommonConfig.RESET_URL);
                                mActivity.startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                }).show();

    }

    protected void onItemClick(DeviceBean devBean) {
        if (devBean == null) {
            ToastUtil.showToast(mActivity, R.string.no_device_found);
            return;
        }
        gotoDeviceCommonActivity(devBean);
    }

    private void gotoDeviceCommonActivity(DeviceBean devBean) {
//        Intent intent = new Intent(mActivity, CommonDeviceDebugActivity.class);
//        intent.putExtra(CommonDeviceDebugPresenter.INTENT_DEVID, devBean.getDevId());
//        mActivity.startActivity(intent);

        AbsPanelCallerService service = MicroContext.getServiceManager().findServiceByInterface(AbsPanelCallerService.class.getName());
        service.goPanelWithCheckAndTip(mActivity, devBean.getDevId());
    }

    public void getDataFromServer() {
        TuyaCommercialLightingArea.newAreaInstance(mProjectId, mAreaId).queryDeviceListInArea("", 20,
                "1", new ITuyaResultCallback<LightingDeviceListBean>() {
                    @Override
                    public void onSuccess(LightingDeviceListBean result) {
                        if (result == null) {
                            return;
                        }

                        transformData(result.getDevices());
                    }

                    @Override
                    public void onError(String errorCode, String errorMessage) {

                    }
                });
    }

    private void transformData(List<DeviceRespBean> devices) {
        if (devices == null) {
            return;
        }

        ArrayList<DeviceBean> deviceBeans = new ArrayList<>();

        for (DeviceRespBean deviceRespBean : devices) {
            if (deviceRespBean == null) {
                continue;
            }

            DeviceBean deviceBean = TuyaCommercialLightingDevice.getDeviceManager().getDeviceBean(deviceRespBean.getDevId());
            if (deviceBean == null) {
                L.e(TAG, "deviceBean not exist");
                continue;
            }
            deviceBeans.add(deviceBean);

            ITuyaDevice lightingDevice = TuyaOSDevice.newDeviceInstance(deviceRespBean.getDevId());
            lightingDevice.registerDevListener(new IDevListener() {
                @Override
                public void onDpUpdate(String devId, String dpStr) {

                }

                @Override
                public void onRemoved(String devId) {
                    getDataFromServer();
                }

                @Override
                public void onStatusChanged(String devId, boolean online) {

                }

                @Override
                public void onNetworkStatusChanged(String devId, boolean status) {

                }

                @Override
                public void onDevInfoUpdate(String devId) {

                }
            });
        }

        updateDeviceData(deviceBeans);
    }

    public void gotoAddDevice() {
//        ActivityUtils.gotoActivity(mActivity, AddDeviceTypeActivity.class, ActivityUtils.ANIMATE_SLIDE_TOP_FROM_BOTTOM, false);
    }

    public void addDevice() {
        final WifiManager mWifiManager = (WifiManager) mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!mWifiManager.isWifiEnabled()) {
            DialogUtil.simpleConfirmDialog(mActivity, mActivity.getString(R.string.open_wifi), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            mWifiManager.setWifiEnabled(true);
                            gotoAddDevice();
                            break;
                    }
                }
            });
        } else {
            gotoAddDevice();
        }
    }


    public void onDeviceClick(DeviceBean deviceBean) {
        if (!deviceBean.getIsOnline()) {
            showDevIsNotOnlineTip(deviceBean);
            return;
        }
        onItemClick(deviceBean);
    }

    public boolean onDeviceLongClick(final DeviceBean deviceBean) {
        if (deviceBean.getIsShare()) {
            return false;
        }
        DialogUtil.simpleConfirmDialog(mActivity, mActivity.getString(R.string.device_confirm_remove), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    unBindDevice(deviceBean);
                }
            }
        });
        return true;
    }

    private void unBindDevice(final DeviceBean deviceBean) {
        ProgressUtil.showLoading(mActivity, R.string.loading);
        TuyaOSDevice.newDeviceInstance(deviceBean.getDevId()).removeDevice(new IResultCallback() {
            @Override
            public void onError(String s, String s1) {
                ProgressUtil.hideLoading();
                ToastUtil.showToast(mActivity, s1);
            }

            @Override
            public void onSuccess() {
                ProgressUtil.hideLoading();

                getDataFromServer();
            }
        });

    }

    private void updateDeviceData(List<DeviceBean> list) {
        if (list.size() == 0) {
            mView.showBackgroundView();
        } else {
            mView.hideBackgroundView();
            mView.updateDeviceData(list);
            mView.loadFinish();
        }
    }

    @Override
    public void onEvent(NetWorkStatusEventModel eventModel) {
        netStatusCheck(eventModel.isAvailable());
    }

    public boolean netStatusCheck(boolean isNetOk) {
        networkTip(isNetOk, R.string.ty_no_net_info);
        return true;
    }

    private void networkTip(boolean networkok, int tipRes) {
        if (networkok) {
            mView.hideNetWorkTipView();
        } else {
            mView.showNetWorkTipView(tipRes);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        TuyaSdk.getEventBus().unregister(this);
    }

}
