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

package com.tuya.smart.commercial.lighting.demo.pages.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.bean.GroupPackBeanWrapper;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupListViewHolder> {
    private List<GroupPackBeanWrapper> groupBeanList = new CopyOnWriteArrayList<>();
    private Context context;
    private GroupClickListener clickListener;
    private GroupListChangeListener changeListener;

    public GroupListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public GroupListAdapter.GroupListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.fragment_group_list_item,
                parent, false);

        return new GroupListViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupListAdapter.GroupListViewHolder holder, int position) {
        if (position >= groupBeanList.size()) {
            return;
        }
        GroupPackBeanWrapper groupBean = groupBeanList.get(position);
        holder.render(groupBean);
    }

    public void clearData() {
        groupBeanList.clear();
    }

    public void addData(List<GroupPackBeanWrapper> groupBeans) {
        if (groupBeans == null || groupBeans.size() <= 0) {
            return;
        }

        this.groupBeanList.addAll(groupBeans);
        setNotifyDataSetChanged();
    }

    public void addData(GroupPackBeanWrapper groupBean) {
        if (groupBean == null) {
            return;
        }

        this.groupBeanList.add(0, groupBean);
        setNotifyDataSetChanged();
    }

    public void removeData(String groupPackId) {
        if (groupBeanList.size() <= 0) {
            return;
        }

        Iterator<GroupPackBeanWrapper> iterator = groupBeanList.iterator();
        while (iterator.hasNext()) {
            GroupPackBeanWrapper next = iterator.next();
            if (next == null || next.getGroupPackBean() == null) {
                continue;
            }

            if (TextUtils.equals(next.getGroupPackBean().getGroupPackageId(), groupPackId)) {
                groupBeanList.remove(next);
                break;
            }
        }

        setNotifyDataSetChanged();
    }

    public void refreshGroupName(String groupPackId, String groupName) {
        if (groupBeanList.size() <= 0) {
            return;
        }

        Iterator<GroupPackBeanWrapper> iterator = groupBeanList.iterator();
        while (iterator.hasNext()) {
            GroupPackBeanWrapper next = iterator.next();
            if (TextUtils.equals(next.getGroupPackBean().getGroupPackageId(), groupPackId)) {
                next.setName(groupName);
                next.getGroupPackBean().setName(groupName);
                break;
            }
        }
        setNotifyDataSetChanged();
    }

    public void refreshGroupDevCount(GroupPackBeanWrapper groupBean) {
        setNotifyDataSetChanged();
    }

    public void setNotifyDataSetChanged() {
        notifyDataSetChanged();
        if (changeListener != null) {
            changeListener.onGroupChanged(groupBeanList.size());
        }
    }

    public void setClickListener(GroupClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setChangeListener(GroupListChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public int getItemCount() {
        return groupBeanList.size();
    }

    static class GroupListViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemNameView;
        private final TextView deviceCountView;
        private GroupClickListener clickListener;
        private final View turnOffView;
        private final View turnOnView;

        public GroupListViewHolder(@NonNull View itemView, GroupClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            itemNameView = itemView.findViewById(R.id.tv_name);
            deviceCountView = itemView.findViewById(R.id.tv_total_device);
            turnOffView = itemView.findViewById(R.id.tv_turn_off);
            turnOnView = itemView.findViewById(R.id.tv_turn_on);
        }

        public void render(final GroupPackBeanWrapper groupBean) {
            itemNameView.setText(groupBean.getGroupPackBean().getName());
            deviceCountView.setText(String.valueOf(groupBean.getGroupPackBean().getDeviceNum()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onGroupClick(groupBean);
                }
            });

            turnOffView.setOnClickListener(v -> {
                clickListener.turnOff(groupBean);
            });

            turnOnView.setOnClickListener(v -> {
                clickListener.turnOn(groupBean);
            });

        }
    }

    public static interface GroupClickListener {
        void onGroupClick(GroupPackBeanWrapper groupBean);

        void turnOn(GroupPackBeanWrapper groupBean);

        void turnOff(GroupPackBeanWrapper groupBean);
    }

    public interface GroupListChangeListener {
        void onGroupChanged(int listSize);
    }
}
