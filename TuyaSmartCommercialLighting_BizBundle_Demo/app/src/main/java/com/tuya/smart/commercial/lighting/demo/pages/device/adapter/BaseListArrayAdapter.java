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
import android.widget.ArrayAdapter;

import java.util.List;

public abstract class BaseListArrayAdapter<E> extends ArrayAdapter<E> {

    protected Context mContext;

    protected int currentPosition;

    protected int mResource;

    protected LayoutInflater mInflater;

    protected List<E> mData;

    public BaseListArrayAdapter(Context context, int resource, List<E> data) {
        super(context, resource, data);
        mContext = context;
        mResource = resource;
        mData = data;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    protected abstract ViewHolder view2Holder(View view);

    protected abstract void bindView(ViewHolder viewHolder, E data);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = mInflater.inflate(mResource, null);
            convertView.setTag(view2Holder(convertView));
        }
        currentPosition = position;
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        bindView(viewHolder, getItem(position));
        return convertView;
    }

    public List<E> getData() {
        return mData;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public String getString(int id) {
        return mContext.getString(id);
    }

    public void setData(List<E> personBeans) {
        mData = personBeans;
        clear();
        addAll(personBeans);
    }
}
