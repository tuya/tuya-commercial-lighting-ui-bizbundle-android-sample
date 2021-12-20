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

package com.tuya.smart.commercial.lighting.demo.pages.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tuya.smart.android.blemesh.api.ITuyaBlueMeshDevice;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.bean.AlertPickBean;
import com.tuya.smart.commercial.lighting.demo.utils.DialogUtil;
import com.tuya.smart.commercial.lighting.demo.utils.ProgressUtil;
import com.tuya.smart.commercial.lighting.demo.utils.SchemaMapper;
import com.tuya.smart.commercial.lighting.demo.utils.ToastUtil;
import com.tuya.smart.commercial.lighting.demo.widget.AlertPickDialog;
import com.tuya.smart.commercial.lighting.demo.bean.DpLogBean;
import com.tuya.smart.commercial.lighting.demo.pages.device.view.ICommonDeviceDebugView;
import com.tuya.smart.android.device.bean.EnumSchemaBean;
import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.android.device.enums.ModeEnum;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.lighting.sdk.api.ILightingDevice;
import com.tuya.sdk.os.TuyaOSDevice;
import com.tuya.sdk.os.TuyaOSMesh;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingDevice;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingProject;
import com.tuya.smart.sdk.api.IDevListener;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;


public class CommonDeviceDebugPresenter extends BasePresenter implements IDevListener {

    private static final String TAG = "CommonDeviceDebugPresenter";
    private Context mContext;
    private ICommonDeviceDebugView mView;
    public static final String INTENT_DEVID = "intent_devId";
    private String mDevId;
    private DeviceBean mDevBean;
    private ITuyaDevice mTuyaDevice;

    private ITuyaBlueMeshDevice mTuyaBlueMeshDevice;

    public CommonDeviceDebugPresenter(Context context, ICommonDeviceDebugView view) {
        mContext = context;
        mView = view;
        initData();
        initListener();
    }

    private void initListener() {
        mTuyaDevice = TuyaOSDevice.newDeviceInstance(mDevId);
        mTuyaDevice.registerDevListener(this);


        DeviceBean deviceBean = TuyaCommercialLightingDevice.getDeviceManager().getDeviceBean(mDevId);
        if (deviceBean.isSigMesh()) {
            mTuyaBlueMeshDevice = TuyaOSMesh.newDevice(deviceBean.getMeshId());
        }
    }

    private void initData() {
        mDevId = ((Activity) mContext).getIntent().getStringExtra(INTENT_DEVID);
        mDevBean = TuyaCommercialLightingDevice.getDeviceManager().getDeviceBean(mDevId);
        if (mDevBean == null) {
            ((Activity) mContext).finish();
        }
    }

    public String getTitle() {
        return mDevBean == null ? "" : mDevBean.getName();
    }

    public DeviceBean getDevBean() {
        return mDevBean;
    }

    public List<SchemaBean> getSchemaList() {
        if (mDevBean == null) return new ArrayList<>();
        Map<String, SchemaBean> schemaMap = mDevBean.getSchemaMap();
        List<SchemaBean> schemaBeanArrayList = new ArrayList<>();
        for (Map.Entry<String, SchemaBean> entry : schemaMap.entrySet()) {
            schemaBeanArrayList.add(entry.getValue());
        }

        Collections.sort(schemaBeanArrayList, new Comparator<SchemaBean>() {
            @Override
            public int compare(SchemaBean lhs, SchemaBean rhs) {
                return Integer.parseInt(lhs.getId()) < Integer.parseInt(rhs.getId()) ? -1 : 1;
            }
        });
        return schemaBeanArrayList;
    }

    public void onClick(View view) {
        String dpID = (String) view.getTag(R.id.schemaId);
        switch (view.getId()) {
            case R.id.iv_sub:
                TextView subTV = (TextView) view.getTag(R.id.schemaView);
                String subV = subTV.getText().toString();
                break;
            case R.id.iv_add:
                TextView addTV = (TextView) view.getTag(R.id.schemaView);
                String addV = addTV.getText().toString();
                sendCommand(dpID, Integer.parseInt(addV) + 1);
                break;
            case R.id.tv_input_send:
                EditText inputSend = (EditText) view.getTag(R.id.schemaView);
                String inputV = inputSend.getText().toString();
                sendCommand(dpID, inputV);
                break;
            case R.id.tv_enum:
                String schemaId = (String) view.getTag(R.id.schemaId);
                SchemaBean schemaBean = mDevBean.getSchemaMap().get(schemaId);
                EnumSchemaBean enumSchemaBean = SchemaMapper.toEnumSchema(schemaBean.getProperty());
                showEnumDialog(schemaBean, enumSchemaBean.getRange());
                break;
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position) {
        String dpID = (String) adapterView.getTag(R.id.schemaId);
        SchemaBean schemaBean = mDevBean.getSchemaMap().get(dpID);
        EnumSchemaBean enumSchemaBean = SchemaMapper.toEnumSchema(schemaBean.getProperty());
        Set<String> range = enumSchemaBean.getRange();
        String[] result = new String[range.size()];
        result = range.toArray(result);
        sendCommand(dpID, result[position]);
    }


    public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
        sendCommand((String) compoundButton.getTag(R.id.schemaId), value);
    }

    private void sendCommand(String dpId, Object value) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put(dpId, value);
        String commandStr = JSON.toJSONString(stringObjectHashMap);

        control(commandStr);
    }

    private void control(String commandStr) {
        DeviceBean deviceBean = TuyaCommercialLightingDevice.getDeviceManager().getDeviceBean(mDevId);
        if (deviceBean == null) {
            L.e(TAG, "deviceBean is null");
            return;
        }

        if (deviceBean.isBleMesh()) {
            meshControl(deviceBean, commandStr);
        } else {
            commonControl(commandStr);
        }
    }

    private void meshControl(DeviceBean deviceBean, String commandStr) {
        mTuyaBlueMeshDevice.publishDps(deviceBean.getNodeId(), deviceBean.getCategory(), commandStr,
                new IResultCallback() {
                    @Override

                    public void onError(String code, String error) {
                        mView.logError(error);
                    }

                    @Override
                    public void onSuccess() {
                        L.e(TAG, "quick control send onSuccess ");
                    }
                });
    }

    private void commonControl(String commandStr) {
        mTuyaDevice.publishDps(commandStr, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                mView.logError(error);
            }

            @Override
            public void onSuccess() {
                Log.i("CommonDevice", "onSuccess");
            }
        });
    }

    private void showEnumDialog(final SchemaBean schemaBean, Set<String> range) {
        ArrayList<String> rangesKey = new ArrayList<>();
        ArrayList<String> rangesValue = new ArrayList<>();
        for (String str : range) {
            rangesValue.add(str);
            rangesKey.add(str);
        }
        final AlertPickBean alertPickBean = new AlertPickBean();
        alertPickBean.setLoop(true);
        alertPickBean.setCancelText(mContext.getString(R.string.cancel));
        alertPickBean.setConfirmText(mContext.getString(R.string.confirm));
        alertPickBean.setRangeKeys(rangesKey);
        alertPickBean.setRangeValues(rangesValue);
        alertPickBean.setTitle(String.format(mContext.getString(R.string.choose_dp_value), schemaBean.getId()));
        AlertPickDialog.showAlertPickDialog((Activity) mContext, alertPickBean, new AlertPickDialog.AlertPickCallBack() {
            @Override
            public void confirm(String value) {
                String dpId = schemaBean.getId();
                sendCommand(dpId, value);
            }

            @Override
            public void cancel() {

            }
        });
    }

    @Override
    public void onDpUpdate(String devId, String dpStr) {
        mView.updateView(dpStr);
        boolean isFromCloud = true;

        DeviceBean deviceBean = TuyaCommercialLightingDevice.getDeviceManager().getDeviceBean(devId);
        if (deviceBean != null && deviceBean.getIsLocalOnline()) {
            isFromCloud = false;
        }
        mView.logDpReport((isFromCloud ? "Cloud" : "local area network") + " " + dpStr);
    }

    public JSONObject getDpValueWithOutROMode(String devId, String value) {
        JSONObject jsonObject = JSONObject.parseObject(value);
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String dpId = entry.getKey();

            Map<String, SchemaBean> schema = TuyaCommercialLightingDevice.getDeviceManager().getSchema(devId);
            ;
            if (schema != null) {
                SchemaBean schemaBean = schema.get(dpId);
                if (schemaBean != null && TextUtils.equals(schemaBean.getMode(), ModeEnum.RO.getType())) {
                    list.add(dpId);
                }
            }
        }
        for (String dpId : list) {
            jsonObject.remove(dpId);
        }
        return jsonObject;
    }


    public static class LogCountDownLatch extends CountDownLatch {
        private int status = STATUS_TIME_OUT;
        public static final int STATUS_SUCCESS = 1;
        public static final int STATUS_FAILURE = 2;
        public static final int STATUS_TIME_OUT = 3;
        private long timeStart;
        private long timeEnd;
        private String dpSend;
        private String dpReturn;
        private String mDpId;
        private String mErrorMsg;

        public LogCountDownLatch(int time) {
            super(time);
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getTimeStart() {
            return timeStart;
        }

        public void setTimeStart(long timeStart) {
            this.timeStart = timeStart;
        }

        public long getTimeEnd() {
            return timeEnd;
        }

        public void setTimeEnd(long timeEnd) {
            this.timeEnd = timeEnd;
        }

        public String getDpSend() {
            return dpSend;
        }

        public void setDpSend(String dpSend) {
            this.dpSend = dpSend;
        }

        public String getDpReturn() {
            return dpReturn;
        }

        public void setDpReturn(String dpReturn) {
            this.dpReturn = dpReturn;
        }

        public void setDpId(String dpId) {
            mDpId = dpId;
        }

        public String getDpId() {
            return mDpId;
        }

        public DpLogBean getLogBean() {
            return new DpLogBean(timeStart, timeEnd, dpSend, dpReturn, mErrorMsg);
        }

        public void setErrorMsg(String errorMsg) {
            mErrorMsg = errorMsg;
        }

        public String getErrorMsg() {
            return mErrorMsg;
        }
    }

    @Override
    public void onRemoved(String devId) {
        mView.deviceRemoved();
    }

    @Override
    public void onStatusChanged(String devId, boolean online) {
        mView.deviceOnlineStatusChanged(online);
    }

    @Override
    public void onNetworkStatusChanged(String devId, boolean status) {
        mView.onNetworkStatusChanged(status);
    }

    @Override
    public void onDevInfoUpdate(String devId) {
        mView.devInfoUpdate();
    }


    public void renameDevice() {
        DialogUtil.simpleInputDialog(mContext, mContext.getString(R.string.rename), getTitle(), false, new DialogUtil.SimpleInputDialogInterface() {
            @Override
            public void onPositive(DialogInterface dialog, String inputText) {
                int limit = mContext.getResources().getInteger(R.integer.change_device_name_limit);
                if (inputText.length() > limit) {
                    ToastUtil.showToast(mContext, R.string.ty_modify_device_name_length_limit);
                } else {
                    renameTitleToServer(inputText);
                }
            }

            @Override
            public void onNegative(DialogInterface dialog) {

            }
        });
    }

    private void renameTitleToServer(final String titleName) {
        ProgressUtil.showLoading(mContext, R.string.loading);
        mTuyaDevice.renameDevice(titleName, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                ProgressUtil.hideLoading();
                ToastUtil.showToast(mContext, error);
            }

            @Override
            public void onSuccess() {
                ProgressUtil.hideLoading();
                mView.updateTitle(titleName);
            }
        });

    }


    public void resetFactory() {
        DialogUtil.simpleConfirmDialog(mContext, mContext.getString(R.string.ty_control_panel_factory_reset_info),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            ProgressUtil.showLoading(mContext, R.string.ty_control_panel_factory_reseting);
                            mTuyaDevice.resetFactory(new IResultCallback() {
                                @Override
                                public void onError(String code, String error) {
                                    ProgressUtil.hideLoading();
                                    ToastUtil.shortToast(mContext, R.string.ty_control_panel_factory_reset_fail);
                                }

                                @Override
                                public void onSuccess() {
                                    ProgressUtil.hideLoading();
                                    ToastUtil.shortToast(mContext, R.string.ty_control_panel_factory_reset_succ);
                                    ((Activity) mContext).finish();
                                }
                            });
                        }
                    }
                });
    }


    public void removeDevice() {
        ProgressUtil.showLoading(mContext, R.string.loading);
        mTuyaDevice.removeDevice(new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                ProgressUtil.hideLoading();
                ToastUtil.showToast(mContext, error);
            }

            @Override
            public void onSuccess() {
                ProgressUtil.hideLoading();
                ((Activity) mContext).finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTuyaDevice != null) mTuyaDevice.onDestroy();

    }
}
