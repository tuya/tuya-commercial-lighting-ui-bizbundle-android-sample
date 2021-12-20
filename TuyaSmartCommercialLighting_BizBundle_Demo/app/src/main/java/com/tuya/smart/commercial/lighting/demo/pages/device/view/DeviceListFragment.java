package com.tuya.smart.commercial.lighting.demo.pages.device.view;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tuya.smart.android.common.utils.NetworkUtil;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseFragment;
import com.tuya.smart.commercial.lighting.demo.pages.device.presenter.DeviceListFragmentPresenter;
import com.tuya.smart.commercial.lighting.demo.utils.AnimationUtil;
import com.tuya.smart.commercial.lighting.demo.utils.DialogUtil;
import com.tuya.smart.commercial.lighting.demo.utils.ProgressUtil;
import com.tuya.smart.commercial.lighting.demo.utils.ToastUtil;
import com.tuya.smart.commercial.lighting.demo.pages.device.adapter.CommonDeviceAdapter;
import com.tuya.smart.lighting.sdk.bean.TransferResultSummary;
import com.tuya.smart.lighting.sdk.impl.DefaultDeviceTransferListener;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingArea;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;


public class DeviceListFragment extends BaseFragment implements IDeviceListFragmentView {

    private static final String TAG = "DeviceListFragment";
    private volatile static DeviceListFragment mDeviceListFragment;
    private View mContentView;
    private DeviceListFragmentPresenter mDeviceListFragmentPresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CommonDeviceAdapter mCommonDeviceAdapter;
    private ListView mDevListView;
    private TextView mNetWorkTip;
    private View mRlView;
    private View mBackgroundView;
    private long mProjectId;
    private long mAreaId;

    public static Fragment newInstance() {
        if (mDeviceListFragment == null) {
            synchronized (DeviceListFragment.class) {
                if (mDeviceListFragment == null) {
                    mDeviceListFragment = new DeviceListFragment();
                }
            }
        }
        return mDeviceListFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            mProjectId = getArguments().getLong("project_id");
            mAreaId = getArguments().getLong("area_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_device_list, container, false);
        initToolbar(mContentView);
        initView();
        initAdapter();
        initSwipeRefreshLayout();
        setDisplayHomeAsUpEnabled();
        setTitle("Devices List");
        mToolBar.setTitleTextColor(Color.WHITE);

        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPresenter();
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtil.isNetworkAvailable(getContext())) {
                    mDeviceListFragmentPresenter.getDataFromServer();
                } else {
                    loadFinish();
                }
            }
        });
    }

    private void initAdapter() {
        mCommonDeviceAdapter = new CommonDeviceAdapter(getActivity());
        mCommonDeviceAdapter.setDeviceClickListener(new CommonDeviceAdapter.DeviceClickListener() {
            @Override
            public void onMasterJoinStatusClicked(DeviceBean deviceBean) {
                handleMasterGroupStatus(deviceBean);
            }
        });
        mDevListView.setAdapter(mCommonDeviceAdapter);
        mDevListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return mDeviceListFragmentPresenter.onDeviceLongClick((DeviceBean) parent.getAdapter().getItem(position));
            }
        });
        mDevListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDeviceListFragmentPresenter.onDeviceClick((DeviceBean) parent.getAdapter().getItem(position));
            }
        });
    }

    private void handleMasterGroupStatus(DeviceBean deviceBean) {
        DialogUtil.customDialog(getActivity(), "",
                "The device has no equipment to be configured, it needs to be added to the space group again, whether to add it again？",
                "Confirm",
                "Cancel", "", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == Dialog.BUTTON_POSITIVE) {
                            dialog.dismiss();
                            ProgressUtil.showLoading(getActivity(), "LOADING");

                            ArrayList<DeviceBean> deviceBeans = new ArrayList<>();
                            deviceBeans.add(deviceBean);

                            boolean localConnectSigMesh = deviceBean.isSigMesh()
                                    && TextUtils.isEmpty(deviceBean.getCommunicationId())
                                    && !deviceBean.isSigMeshWifi();

                            if (localConnectSigMesh) {
                                TuyaCommercialLightingArea.newAreaInstance(mProjectId, mAreaId).putLocalConnectDevicesInAreaGroup(deviceBeans,
                                        new DefaultDeviceTransferListener() {
                                            @Override
                                            public void handleResult(TransferResultSummary transferResultSummary) {
                                                ProgressUtil.hideLoading();
                                                mDeviceListFragmentPresenter.getDataFromServer();
                                            }

                                            @Override
                                            public void onError(String errorMsg, String errorCode) {
                                                ToastUtil.showToast(getActivity(), errorMsg);
                                                ProgressUtil.hideLoading();
                                            }
                                        });
                            } else {
                                TuyaCommercialLightingArea.newAreaInstance(mProjectId, mAreaId).putRemoteConnectDevicesInAreaGroup(deviceBeans,
                                        new DefaultDeviceTransferListener() {
                                            @Override
                                            public void handleResult(TransferResultSummary transferResultSummary) {
                                                ProgressUtil.hideLoading();
                                                mDeviceListFragmentPresenter.getDataFromServer();
                                            }

                                            @Override
                                            public void onError(String errorMsg, String errorCode) {
                                                ToastUtil.showToast(getActivity(), errorMsg);
                                                ProgressUtil.hideLoading();
                                            }
                                        });

                            }
                        } else {
                            dialog.dismiss();
                        }
                    }
                }).show();
    }


    @Override
    public void updateDeviceData(List<DeviceBean> myDevices) {
        if (mCommonDeviceAdapter != null) {
            mCommonDeviceAdapter.setData(myDevices);
        }
    }

    @Override
    public void loadStart() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    protected void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mContentView.findViewById(R.id.swipe_container);
        mNetWorkTip = (TextView) mContentView.findViewById(R.id.network_tip);
        mDevListView = (ListView) mContentView.findViewById(R.id.lv_device_list);
        mRlView = mContentView.findViewById(R.id.rl_list);
        mBackgroundView = mContentView.findViewById(R.id.list_background_tip);
    }

    protected void initPresenter() {
        mDeviceListFragmentPresenter = new DeviceListFragmentPresenter(this, this);
        mDeviceListFragmentPresenter.setParams(mProjectId, mAreaId);
        mDeviceListFragmentPresenter.getDataFromServer();
    }

    @Override
    public void loadFinish() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void showNetWorkTipView(int tipRes) {
        mNetWorkTip.setText(tipRes);
        if (mNetWorkTip.getVisibility() != View.VISIBLE) {
            AnimationUtil.translateView(mRlView, 0, 0, -mNetWorkTip.getHeight(), 0, 300, false, null);
            mNetWorkTip.setVisibility(View.VISIBLE);
        }
    }

    public void hideNetWorkTipView() {
        if (mNetWorkTip.getVisibility() != View.GONE) {
            AnimationUtil.translateView(mRlView, 0, 0, mNetWorkTip.getHeight(), 0, 300, false, null);
            mNetWorkTip.setVisibility(View.GONE);
        }
    }

    @Override
    public void showBackgroundView() {
        BaseActivity.setViewGone(mDevListView);
        BaseActivity.setViewVisible(mBackgroundView);
    }

    @Override
    public void hideBackgroundView() {
        BaseActivity.setViewVisible(mDevListView);
        BaseActivity.setViewGone(mBackgroundView);
    }

    @Override
    public void gotoCreateHome() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDeviceListFragmentPresenter.onDestroy();
    }

}
