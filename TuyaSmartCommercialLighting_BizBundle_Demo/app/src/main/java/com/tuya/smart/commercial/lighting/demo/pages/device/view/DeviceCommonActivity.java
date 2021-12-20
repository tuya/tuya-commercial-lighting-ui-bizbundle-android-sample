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

package com.tuya.smart.commercial.lighting.demo.pages.device.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.pages.device.adapter.SchemaListAdapter;
import com.tuya.smart.commercial.lighting.demo.pages.device.presenter.DeviceCommonPresenter;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.android.device.bean.SchemaBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DeviceCommonActivity extends BaseActivity implements IDeviceCommonView {

    @BindView(R.id.lv_dp_list)
    public ListView mListView;
    private SchemaListAdapter mAdapter;
    private DeviceCommonPresenter mPresenter;
    private Unbinder mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_common);
        mBind = ButterKnife.bind(this);
        initToolbar();
        initPresenter();
        initTitle();
        initMenu();
        initAdapter();
        getSchemaData();

    }


    protected void setDisplayHomeAsUpEnabled() {
        setDisplayHomeAsUpEnabled(R.drawable.tysmart_back_white, null);
    }

    private void getSchemaData() {
        mPresenter.getData();
    }

    private void initTitle() {
        setTitle(mPresenter.getDevName());
    }

    private void initMenu() {
        setDisplayHomeAsUpEnabled();
        setMenu(R.menu.toolbar_top_smart_device, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_test_mode:
                        break;
                    case R.id.action_rename:
                        mPresenter.renameDevice();
                        break;
                    case R.id.action_close:
                        finish();
                        break;
                    case R.id.action_resume_factory_reset:
                        mPresenter.resetFactory();
                        break;
                    case R.id.action_unconnect:
                        mPresenter.removeDevice();
                        break;
                }
                return false;
            }
        });
    }

    private void initPresenter() {
        mPresenter = new DeviceCommonPresenter(this, this);
    }

    private void initAdapter() {
        mAdapter = new SchemaListAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.onItemClick(mAdapter.getItem(position));
            }
        });
    }

    @Override
    public void setSchemaData(List<SchemaBean> schemaList) {
        mAdapter.setData(schemaList);
    }

    @Override
    public void updateTitle(String titleName) {
        setTitle(titleName);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        mPresenter.onDestroy();
    }

}

