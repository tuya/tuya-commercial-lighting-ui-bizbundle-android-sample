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

package com.tuya.smart.commercial.lighting.demo.pages.group.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.app.IntentExtra;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.bean.DeviceBeanWrapper;
import com.tuya.smart.commercial.lighting.demo.pages.device.adapter.GroupDeviceListAdapter;
import com.tuya.smart.commercial.lighting.demo.pages.group.presenter.PackedGroupListPresenter;
import com.tuya.smart.commercial.lighting.demo.utils.ToastUtil;
import com.tuya.smart.lighting.sdk.bean.ComplexDeviceBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GroupDeviceListActivity extends BaseActivity implements IGroupDeviceListView {

    private Unbinder unbinder;

    @BindView(R.id.lv_device_list)
    ListView recyclerView;

    @BindView(R.id.btn_create)
    Button performCreateButton;

    private PackedGroupListPresenter presenter;
    private long mProjectId;
    private long mAreaId;
    private GroupDeviceListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_device_list);
        unbinder = ButterKnife.bind(this);

        initToolbar();
        setTitle("Group Device List");

        setDisplayHomeAsUpEnabled();

        Intent intent = getIntent();
        mProjectId = intent.getLongExtra(IntentExtra.KEY_PROJECT_ID, 0);
        mAreaId = intent.getLongExtra(IntentExtra.KEY_AREA_ID, 0);

        presenter = new PackedGroupListPresenter(this);
        presenter.setData(mProjectId, mAreaId, "");
        presenter.getData(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void refresh(List<DeviceBeanWrapper> respBean) {
        //refresh list
        runOnUiThread(() -> {
            if (adapter == null) {
                adapter = new GroupDeviceListAdapter(this);
                recyclerView.setAdapter(adapter);
                recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        DeviceBeanWrapper item = adapter.getItem(position);
                        item.setChecked(!item.isChecked());

                        adapter.notifyDataSetChanged();
                    }
                });
            }
            adapter.setData(respBean);
            adapter.notifyDataSetChanged();
        });
    }

    @OnClick(R.id.btn_create)
    public void performCreate() {
        if (adapter == null || adapter.getData() == null) {
            return;
        }

        List<ComplexDeviceBean> checkedDeviceBeans = new ArrayList<>();
        for (DeviceBeanWrapper item : adapter.getData()) {
            if (item.isChecked() && item.getDeviceBean().getIsOnline()) {
                checkedDeviceBeans.add(item.getDeviceRespBean());
            }
        }

        if (checkedDeviceBeans.size() == 0) {
            toast("No checked devices");
            return;
        }

        presenter.createPackedGroup(String.valueOf(System.currentTimeMillis()), checkedDeviceBeans);
    }

    @Override
    public void toast(String message) {
        ToastUtil.showToast(this, message);
    }
}
