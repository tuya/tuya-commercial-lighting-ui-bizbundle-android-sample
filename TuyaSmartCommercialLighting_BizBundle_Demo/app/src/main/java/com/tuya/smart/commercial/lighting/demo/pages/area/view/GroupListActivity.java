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

package com.tuya.smart.commercial.lighting.demo.pages.area.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.app.IntentExtra;
import com.tuya.smart.commercial.lighting.demo.app.base.BaseActivity;
import com.tuya.smart.commercial.lighting.demo.pages.group.view.GroupDeviceListActivity;
import com.tuya.smart.commercial.lighting.demo.utils.ToastUtil;

public class GroupListActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cl_content_placeholder);

        initToolbar();
        setTitle("Packed Group List");
        setDisplayHomeAsUpEnabled();

        View createPackedGroup = findViewById(R.id.btn_create_packed_group);
        createPackedGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGroupDeviceListActivity();
            }
        });

        Intent intent = getIntent();
        long mProjectId = intent.getLongExtra(IntentExtra.KEY_PROJECT_ID, 0);
        long areaId = intent.getLongExtra(IntentExtra.KEY_AREA_ID, 0);

        Bundle bundle = new Bundle();
        bundle.putLong("areaId", areaId);
        GroupListFragment groupListFragment = GroupListFragment.Companion.newInstance();
        groupListFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_rootView, groupListFragment)
                .commitAllowingStateLoss();
    }

    private void gotoGroupDeviceListActivity() {
        Intent intent = new Intent(this, GroupDeviceListActivity.class);
        intent.putExtras(getIntent());
        startActivity(intent);
    }
}
