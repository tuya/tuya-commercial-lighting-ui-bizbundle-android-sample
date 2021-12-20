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

package com.tuya.smart.commercial.lighting.demo.pages.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.lighting.sdk.bean.LightingRegionListBean;

import java.util.ArrayList;
import java.util.List;

public class RegionListAdapter extends RecyclerView.Adapter<RegionListAdapter.RegionViewHolder> {
    private final List<LightingRegionListBean> mRegionList;
    private final LayoutInflater mInflater;

    private int selectedPosition = -1;
    private OnClickListener clickListener;
    private final int selectedColor;
    private final int normalColor;

    public void refreshUI(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onClick(int position, LightingRegionListBean regionId);
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public RegionListAdapter(Context context) {
        super();
        selectedColor = ContextCompat.getColor(context, R.color.primary_button_bg_color);
        normalColor = ContextCompat.getColor(context, R.color.colorNormal);
        mRegionList = new ArrayList<>();
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RegionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.cl_recycler_region_list_item, null);
        return new RegionViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull RegionViewHolder holder, int position) {
        LightingRegionListBean bean = mRegionList.get(position);
        holder.regionName.setText(bean.name);
        if (selectedPosition == position) {
            holder.regionName.setTextColor(selectedColor);
        } else {
            holder.regionName.setTextColor(normalColor);
        }
        holder.regionName.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onClick(position, bean);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mRegionList.size();
    }

    public LightingRegionListBean getSelectedItem() {
        return mRegionList.get(selectedPosition);
    }

    public void setData(List<LightingRegionListBean> regionList) {
        mRegionList.clear();
        if (regionList != null) {
            mRegionList.addAll(regionList);
        }
        notifyDataSetChanged();
    }

    public static class RegionViewHolder extends RecyclerView.ViewHolder {
        public TextView regionName;

        public RegionViewHolder(View contentView) {
            super(contentView);
            regionName = (TextView) contentView.findViewById(R.id.tv_region_name);
        }
    }
}
