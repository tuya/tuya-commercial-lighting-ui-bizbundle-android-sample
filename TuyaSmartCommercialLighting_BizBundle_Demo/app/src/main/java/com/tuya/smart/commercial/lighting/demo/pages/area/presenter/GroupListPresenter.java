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
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.tuya.sdk.core.PluginManager;
import com.tuya.sdk.home.cache.TuyaHomeRelationCacheManager;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingGroupPack;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.api.service.MicroServiceManager;
import com.tuya.smart.commercial.lighting.demo.bean.GroupPackBeanWrapper;
import com.tuya.smart.commercial.lighting.demo.pages.area.view.IGroupListView;
import com.tuya.smart.commonbiz.api.family.AbsFamilyService;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.interior.api.ITuyaDevicePlugin;
import com.tuya.smart.interior.api.ITuyaGroupPlugin;
import com.tuya.smart.interior.device.ITuyaDeviceDataCacheManager;
import com.tuya.smart.lighting.sdk.TuyaLightingKitSDK;
import com.tuya.smart.lighting.sdk.api.ILightingGroupPackDpsManager;
import com.tuya.smart.lighting.sdk.api.ILightingGroupPackListener;
import com.tuya.smart.lighting.sdk.bean.GroupPackBean;
import com.tuya.smart.lighting.sdk.bean.GroupPackListBean;
import com.tuya.smart.lighting.sdk.impl.DefaultGroupChangeObserver;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.api.ITuyaDataCallback;
import com.tuya.smart.sdk.bean.GroupBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GroupListPresenter {
    private static final String TAG = "GroupListPresenter";
    private IGroupListView mGroupListView;
    private final LightingGroupUseCase mGroupUseCase;
    private int offsetKey = 0;
    private long areaId = 0;
    private AbsFamilyService mAbsFamilyService;
    private final long mCurrentProjectId;
    private final ITuyaDevicePlugin mDevicePlugin;
    private final ITuyaDeviceDataCacheManager mDataCacheManager;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean loadMoreEnable = true;

    private final DefaultGroupChangeObserver groupChangeObserver = new DefaultGroupChangeObserver() {
        @Override
        public void onGroupInfoChanged(long groupId) {
            queryGroupInfo(groupId);
        }
    };

    ILightingGroupPackListener groupPackListener = new ILightingGroupPackListener() {
        @Override
        public void onGroupPackAdded(GroupPackBean groupPackBean) {
            postRefresh();
        }

        @Override
        public void onGroupPackDeleted(String groupPackId) {
            postRefresh();
        }

        @Override
        public void onGroupPackInfoChanged(String groupPackId, String name) {
            L.i(TAG, "groupPackId:" + groupPackId + " name:" + name);
            mGroupListView.refreshGroupName(groupPackId, name);
        }

        @Override
        public void onGroupPackEdit(String groupPackId) {
            reset();
            refreshGroupList(true);
        }
    };

    public GroupListPresenter(IGroupListView groupListView) {
        this.mGroupListView = groupListView;
        mGroupUseCase = new LightingGroupUseCase();
        mAbsFamilyService = MicroServiceManager.getInstance().findServiceByInterface(AbsFamilyService.class.getName());
        mCurrentProjectId = mAbsFamilyService.getCurrentHomeId();
        mDevicePlugin = PluginManager.service(ITuyaDevicePlugin.class);
        mDataCacheManager = mDevicePlugin.newTuyaDeviceDataCacheManager();
        TuyaCommercialLightingGroupPack.getGroupManager().registerObserver(groupChangeObserver);
        TuyaCommercialLightingGroupPack.getGroupPackManager().registerGroupPackListener(groupPackListener);


    }

    public void queryGroupInfo(long groupId) {
        TuyaCommercialLightingGroupPack.getGroupManager().getGroupInfo(mCurrentProjectId, groupId, new ITuyaResultCallback<GroupBean>() {
            @Override
            public void onSuccess(GroupBean result) {
//                mGroupListView.refreshGroupItem(result);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                //do nothing
            }
        });
    }

    public void toggleAreaId(long areaId) {
        this.areaId = areaId;
        mGroupListView.clearGroupList();
        reset();
        refreshGroupList(true);
    }

    private void postRefresh() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                reset();
                refreshGroupList(true);
            }
        }, 1000);
    }

    private void reset() {
        offsetKey = 0;
        loadMoreEnable = true;
    }

    public void loadMoreData() {
        if (!loadMoreEnable) {
            if (offsetKey > 1) {
                mGroupListView.toast(R.string.cl_no_more_data);
            }
            return;
        }

        refreshGroupList(false);
    }

    public void refreshGroupList(final boolean reset) {
        if (mCurrentProjectId == 0) {
            return;
        }

        offsetKey++;
        mGroupUseCase.getGroupPackListByAreaId(mCurrentProjectId, areaId, offsetKey + "",
                new ITuyaResultCallback<GroupPackListBean>() {
                    @Override
                    public void onSuccess(GroupPackListBean result) {
                        if (result == null) {
                            return;
                        }

                        if (reset) {
                            mGroupListView.clearGroupList();
                        }

                        if (offsetKey == 1
                                && (result.getList() == null || result.getList().size() <= 0)) {
                            mGroupListView.showEmptyView();
                            return;
                        }

                        if (result.isEnd()) {
                            loadMoreEnable = false;
                        }

                        List<GroupPackBeanWrapper> groupPackBeanWrappers = dataConvert(result.getList());
                        mGroupListView.loadMoreGroupList(groupPackBeanWrappers);
                    }

                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        mGroupListView.toast(errorMessage);
                    }
                });
    }

    private List<GroupPackBeanWrapper> dataConvert(List<GroupPackBean> list) {
        ArrayList<GroupPackBeanWrapper> groupPackBeanWrappers = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (GroupPackBean groupPackBean : list) {
                ILightingGroupPackDpsManager dpsManager = TuyaLightingKitSDK.getInstance().newGroupPackDpsManager(mCurrentProjectId, groupPackBean);
                if (dpsManager == null) {
                    continue;
                }

                GroupPackBeanWrapper wrapper = new GroupPackBeanWrapper(groupPackBean);
                wrapper.setName(groupPackBean.getName());
                wrapper.setDps(groupPackBean.getDps());
                wrapper.setAreaDpMode(dpsManager.getCurrentMode());
                wrapper.setBrightness(dpsManager.getBrightPercent());
                wrapper.setColorData(dpsManager.getColorData());
                wrapper.setCurrentSceneType(dpsManager.getSceneStatus());
                wrapper.setSwitchOpen(dpsManager.getSwitchStatus());
                wrapper.setTemperaturePercent(dpsManager.getTemperaturePercent());

                groupPackBeanWrappers.add(wrapper);
            }
        }
        return groupPackBeanWrappers;
    }

    public void openGroupPanel(final Activity activity, final GroupPackBeanWrapper groupBean) {
        if (groupBean == null) {
            return;
        }

        if (groupBean.getGroupPackBean().getDeviceNum() <= 0) {
            mGroupListView.showEmptyGroupDialog(groupBean, false);
            return;
        }

        TuyaLightingKitSDK.getInstance().newGroupPackInstance(mCurrentProjectId, groupBean.getGroupPackBean().getGroupPackageId())
                .getPackedGroupInfo(new ITuyaResultCallback<GroupPackBean>() {
                    @Override
                    public void onSuccess(final GroupPackBean groupPackResult) {
                        groupBean.setGroupPackBean(groupPackResult);
                        mGroupListView.refreshGroupItem(groupBean);

                        if (groupBean.getGroupPackBean().getType() == 1) {
                            mGroupListView.showEmptyGroupDialog(groupBean, true);
                        } else if (groupBean.getGroupPackBean().getType() == 2) {
                            if (groupBean.getGroupPackBean().getJoinedGroups() == null
                                    || groupBean.getGroupPackBean().getJoinedGroups().size() <= 0) {
                                return;
                            }
                            final GroupBean firstGroup = groupBean.getGroupPackBean().getJoinedGroups().get(0);
                            mDataCacheManager.getGroup(mCurrentProjectId, firstGroup.getId(),
                                    new ITuyaDataCallback<GroupBean>() {
                                        @Override
                                        public void onSuccess(GroupBean result) {
                                            mergeGroupBean(groupPackResult, firstGroup);

                                            GroupBean finalGroupBean = TuyaHomeRelationCacheManager.getInstance().getGroupBean(firstGroup.getId());
                                            mGroupUseCase.openGroupPanel(activity, finalGroupBean, true);
                                        }

                                        @Override
                                        public void onError(String errorCode, String errorMessage) {
                                            mGroupListView.toast(errorMessage);
                                        }
                                    });
                        } else {
                            mGroupListView.showGroupPackControlDialog(groupBean);
                        }
                    }

                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        mGroupListView.toast(errorMessage);
                    }
                });
    }

    private void mergeGroupBean(GroupPackBean groupPackResult, GroupBean groupBean) {
        GroupBean finalGroupBean = TuyaHomeSdk.getDataInstance().getGroupBean(groupBean.getId());
        finalGroupBean.setMasterDevId(groupBean.getMasterDevId());
        finalGroupBean.setMasterNodeId(groupBean.getMasterNodeId());
        if (groupPackResult != null) {
            finalGroupBean.setGroupPackId(groupPackResult.getGroupPackageId());
        }

        ITuyaGroupPlugin iTuyaGroupPlugin = PluginManager.service(ITuyaGroupPlugin.class);
        if (iTuyaGroupPlugin != null) {
            iTuyaGroupPlugin.getGroupCacheInstance().addGroup(finalGroupBean);
        }
    }

    public void setBundle(@Nullable Bundle bundle) {
        if (bundle == null) {
            return;
        }
        areaId = bundle.getLong("areaId", 0L);
    }

    public void onDestroy() {
        TuyaCommercialLightingGroupPack.getGroupManager().unregisterObserver(groupChangeObserver);
        mHandler.removeCallbacksAndMessages(null);
    }

    public void dismissGroupPack(@NotNull final String groupPackId) {
        TuyaLightingKitSDK.getInstance().newGroupPackInstance(mCurrentProjectId, groupPackId)
                .dismiss(new IResultCallback() {
                    @Override
                    public void onError(String code, String error) {
                        mGroupListView.toast(error);
                    }

                    @Override
                    public void onSuccess() {
                        mGroupListView.removeGroup(groupPackId);
                    }
                });
    }

    public void turnOn(@Nullable GroupPackBeanWrapper groupBean) {
        TuyaCommercialLightingGroupPack.newGroupPackDpsManager(mCurrentProjectId, groupBean.getGroupPackBean())
                .publishSwitchStatus(true, new IResultCallback() {
                    @Override
                    public void onError(String code, String error) {
                        mGroupListView.toast(error);
                    }

                    @Override
                    public void onSuccess() {

                    }
                });
    }

    public void turnOff(@Nullable GroupPackBeanWrapper groupBean) {
        TuyaCommercialLightingGroupPack.newGroupPackDpsManager(mCurrentProjectId, groupBean.getGroupPackBean())
                .publishSwitchStatus(false, new IResultCallback() {
                    @Override
                    public void onError(String code, String error) {
                        mGroupListView.toast(error);
                    }

                    @Override
                    public void onSuccess() {

                    }
                });
    }
}
