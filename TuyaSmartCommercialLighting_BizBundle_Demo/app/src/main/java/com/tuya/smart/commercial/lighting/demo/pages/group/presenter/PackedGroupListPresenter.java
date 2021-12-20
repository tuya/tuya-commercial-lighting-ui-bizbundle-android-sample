package com.tuya.smart.commercial.lighting.demo.pages.group.presenter;

import com.tuya.sdk.os.TuyaOSDevice;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingDevice;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingGroupPack;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.commercial.lighting.demo.bean.DeviceBeanWrapper;
import com.tuya.smart.commercial.lighting.demo.pages.group.view.IGroupDeviceListView;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.interior.device.bean.DeviceRespBean;
import com.tuya.smart.lighting.sdk.bean.ComplexDeviceBean;
import com.tuya.smart.lighting.sdk.bean.GroupDeviceListRespBean;
import com.tuya.smart.lighting.sdk.bean.TransferResultSummary;
import com.tuya.smart.lighting.sdk.impl.DefaultDeviceTransferListener;
import com.tuya.smart.sdk.api.IDevListener;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.sdk.bean.GroupDeviceBean;

import java.util.ArrayList;
import java.util.List;

public class PackedGroupListPresenter implements IPackedGroupDeviceListPresenter {
    private long mProjectId;
    private long mAreaId;
    private String mPackedGroupId;
    private IGroupDeviceListView mView;

    public PackedGroupListPresenter(IGroupDeviceListView view) {
        this.mView = view;
    }

    public void setData(long projectId, long areaId, String groupPackId) {
        this.mProjectId = projectId;
        this.mAreaId = areaId;
        this.mPackedGroupId = groupPackId;
    }

    @Override
    public void getData(boolean createGroup) {
        TuyaCommercialLightingGroupPack.newGroupPackInstance(mProjectId, mPackedGroupId)
                .getAvailableDevices2JoinGroupPack(mAreaId, "zm", 20, "1", new ITuyaResultCallback<GroupDeviceListRespBean>() {
                    @Override
                    public void onSuccess(GroupDeviceListRespBean result) {
                        if (result != null && result.devices != null) {
                            transformData(result.devices);
                        } else {
                            mView.toast("Error response");
                        }
                    }

                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        mView.toast(errorMessage);
                    }
                });
    }

    @Override
    public void createPackedGroup(String packedGroupName, List<ComplexDeviceBean> deviceBeans) {
        TuyaCommercialLightingGroupPack.getGroupPackManager().createPackedGroup(mProjectId, mAreaId, packedGroupName, deviceBeans, "zm","", new DefaultDeviceTransferListener() {
            @Override
            public void handleResult(TransferResultSummary transferResultSummary) {
                mView.toast("Create Packed group finished");
            }

            @Override
            public void onError(String errorMsg, String errorCode) {
                mView.toast("Create Packed group failed:" + errorMsg);
            }
        });
    }


    private void transformData(List<GroupDeviceBean> devices) {
        if (devices == null) {
            return;
        }

        ArrayList<DeviceBeanWrapper> deviceBeans = new ArrayList<>();

        for (GroupDeviceBean deviceRespBean : devices) {
            if (deviceRespBean == null) {
                continue;
            }

            DeviceBean deviceBean = TuyaCommercialLightingDevice.getDeviceManager().getDeviceBean(deviceRespBean.getDevId());
            if (deviceBean == null) {
                continue;
            }

            DeviceBeanWrapper deviceBeanWrapper = new DeviceBeanWrapper();
            deviceBeanWrapper.setDeviceBean(deviceBean);
            deviceBeanWrapper.setChecked(deviceRespBean.isChecked());
            deviceBeanWrapper.setDeviceRespBean(deviceRespBean);
            deviceBeans.add(deviceBeanWrapper);

            ITuyaDevice lightingDevice = TuyaOSDevice.newDeviceInstance(deviceRespBean.getDevId());
            lightingDevice.registerDevListener(new IDevListener() {
                @Override
                public void onDpUpdate(String devId, String dpStr) {

                }

                @Override
                public void onRemoved(String devId) {
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

        mView.refresh(deviceBeans);
    }
}
