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
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.device.bean.SchemaBean;

import java.util.ArrayList;
import java.util.List;

public class SchemaListAdapter extends BaseAdapter {
    private List<SchemaBean> mSchemaBeen;
    private LayoutInflater mInflater;

    public SchemaListAdapter(Context context) {
        mSchemaBeen = new ArrayList<>();
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mSchemaBeen.size();
    }

    @Override
    public SchemaBean getItem(int position) {
        return mSchemaBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SchemaViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.list_schema, null);
            holder = new SchemaViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SchemaViewHolder) convertView.getTag();
        }
        holder.updateData(mSchemaBeen.get(position));
        return convertView;
    }

    public void setData(List<SchemaBean> schemaBeen) {
        mSchemaBeen.clear();
        if (schemaBeen != null) {
            mSchemaBeen.addAll(schemaBeen);
        }
        notifyDataSetChanged();
    }

    public static class SchemaViewHolder extends ViewHolder<SchemaBean> {
        public TextView schemaName;
        public TextView schemaId;
        private TextView schemaMode;

        public SchemaViewHolder(View contentView) {
            super(contentView);
            schemaName = (TextView) contentView.findViewById(R.id.tv_name);
            schemaId = (TextView) contentView.findViewById(R.id.tv_id);
            schemaMode = (TextView) contentView.findViewById(R.id.tv_mode);
        }

        @Override
        public void updateData(SchemaBean schemaBean) {
            schemaId.setText(schemaBean.getId());
            schemaName.setText(schemaBean.getName());
            schemaMode.setText(schemaBean.getMode().toString());
        }
    }
}
