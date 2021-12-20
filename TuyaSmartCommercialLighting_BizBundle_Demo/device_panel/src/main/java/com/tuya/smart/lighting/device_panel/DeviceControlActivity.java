package com.tuya.smart.lighting.device_panel;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.commerciallighting.commonbiz.bizbundle.project.api.AbsBizBundleProjectService;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingArea;
import com.tuya.sdk.os.lighting.TuyaCommercialLightingDevice;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.api.MicroContext;
import com.tuya.smart.api.service.MicroServiceManager;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.interior.device.bean.DeviceRespBean;
import com.tuya.smart.lighting.sdk.bean.LightingDeviceListBean;
import com.tuya.smart.panelcaller.api.AbsPanelCallerService;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.utils.ProgressUtil;

import java.util.ArrayList;
import java.util.List;

public class DeviceControlActivity extends AppCompatActivity {

    public static final String TAG = DeviceControlActivity.class.getSimpleName();

    private HomeAdapter mAdapter;
    private long mProjectId;
    private long mAreaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();

        mProjectId = intent.getLongExtra("project_id", 0);
        mAreaId = intent.getLongExtra("area_id", 0);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Panel");
        RecyclerView homeRecycler = findViewById(R.id.home_recycler);
        homeRecycler.setLayoutManager(new LinearLayoutManager(this));
        homeRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new HomeAdapter();
        homeRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DeviceBean bean, int position) {

                if (bean == null) {
                    Toast.makeText(DeviceControlActivity.this, getResources().getString(R.string.no_device_found), Toast.LENGTH_LONG).show();
                    return;
                }
                AbsPanelCallerService service = MicroContext.getServiceManager().findServiceByInterface(AbsPanelCallerService.class.getName());
                service.goPanelWithCheckAndTip(DeviceControlActivity.this, bean.getDevId());
            }
        });
        getCurrentDeviceList();
    }

    /**
     * you must implementation AbsBizBundleProjectService
     *
     * @return AbsBizBundleProjectService
     */
    private AbsBizBundleProjectService getService() {
        return MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleProjectService.class.getName());
    }

    private void getCurrentDeviceList() {
        ProgressUtil.showLoading(this, "Loading...");
        TuyaCommercialLightingArea.newAreaInstance(mProjectId, mAreaId).queryDeviceListInArea("", 20,
                "1", new ITuyaResultCallback<LightingDeviceListBean>() {
                    @Override
                    public void onSuccess(LightingDeviceListBean result) {
                        ProgressUtil.hideLoading();
                        if (result == null) return;
                        transformData(result.getDevices());
                    }

                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        ProgressUtil.hideLoading();
                        Toast.makeText(DeviceControlActivity.this, errorCode + "\n" + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void transformData(List<DeviceRespBean> devices) {
        if (devices == null) return;
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
        }
        mAdapter.setData(deviceBeans);
    }
}