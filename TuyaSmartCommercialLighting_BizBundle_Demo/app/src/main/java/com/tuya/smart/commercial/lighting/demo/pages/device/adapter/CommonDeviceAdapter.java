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

package com.tuya.smart.commercial.lighting.demo.pages.device.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;

public class CommonDeviceAdapter extends BaseAdapter {
    private final List<DeviceBean> mDevs;
    private final LayoutInflater mInflater;
    private Context mContext;
    private DeviceClickListener listener;

    public CommonDeviceAdapter(Context context) {
        mDevs = new ArrayList<>();
        mContext = context;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mDevs.size();
    }

    @Override
    public DeviceBean getItem(int position) {
        return mDevs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DeviceViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.list_common_device_item, null);
            holder = new DeviceViewHolder(convertView, listener);
            convertView.setTag(holder);
        } else {
            holder = (DeviceViewHolder) convertView.getTag();
        }
        holder.updateData(mDevs.get(position));
        return convertView;
    }

    public void setData(List<DeviceBean> myDevices) {
        mDevs.clear();
        if (myDevices != null) {
            mDevs.addAll(myDevices);
        }
        notifyDataSetChanged();
    }

    public void setDeviceClickListener(DeviceClickListener listener) {
        this.listener = listener;
    }

    public static interface DeviceClickListener {
        void onMasterJoinStatusClicked(DeviceBean deviceBean);
    }

    private static class DeviceViewHolder extends ViewHolder<DeviceBean> {
        ImageView connect;
        ImageView deviceIcon;
        TextView device;
        TextView joinStatusView;
        private DeviceClickListener listener;

        DeviceViewHolder(View contentView, DeviceClickListener listener) {
            super(contentView);
            this.listener = listener;
            connect = (ImageView) contentView.findViewById(R.id.iv_device_list_dot);
            deviceIcon = (ImageView) contentView.findViewById(R.id.iv_device_icon);
            device = (TextView) contentView.findViewById(R.id.tv_device);
            joinStatusView = contentView.findViewById(R.id.tv_join_status);
        }

        @Override
        public void updateData(DeviceBean deviceBean) {
            Picasso.with(TuyaSdk.getApplication()).load(deviceBean.getIconUrl()).into(deviceIcon);
            final int resId;
            if (deviceBean.getIsOnline()) {
                if (deviceBean.getIsShare() != null && deviceBean.getIsShare()) {
                    resId = R.drawable.ty_devicelist_share_green;
                } else {
                    resId = R.drawable.ty_devicelist_dot_green;
                }
            } else {
                if (deviceBean.getIsShare() != null && deviceBean.getIsShare()) {
                    resId = R.drawable.ty_devicelist_share_gray;
                } else {
                    resId = R.drawable.ty_devicelist_dot_gray;
                }
            }
            connect.setImageResource(resId);
            device.setText(deviceBean.getName());

            if (deviceBean.getMasterGroupJoinStatus() != null && deviceBean.getMasterGroupJoinStatus() == 0) {
                joinStatusView.setVisibility(View.VISIBLE);
            } else {
                joinStatusView.setVisibility(View.GONE);
            }

            joinStatusView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onMasterJoinStatusClicked(deviceBean);
                    }
                }
            });
        }
    }
}
