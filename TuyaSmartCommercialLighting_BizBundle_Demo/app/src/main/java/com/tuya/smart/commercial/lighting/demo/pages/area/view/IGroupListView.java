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


import com.tuya.smart.commercial.lighting.demo.bean.GroupPackBeanWrapper;

import java.util.List;

public interface IGroupListView {
    void clearGroupList();

    void refreshGroupName(String groupPackId, String newName);

    void refreshGroupItem(GroupPackBeanWrapper groupInfo);

    void loadMoreGroupList(List<GroupPackBeanWrapper> result);

    void removeGroup(String groupPackId);

    void addGroup(GroupPackBeanWrapper groupBean);

    void showProgress();

    void hideProgress();

    void toast(String msg);

    void toast(int resId);

    void showEmptyView();

    void showEmptyGroupDialog(GroupPackBeanWrapper groupBean, boolean dismissGroup);

    void showGroupPackControlDialog(GroupPackBeanWrapper areaBeanWrapper);
}
