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

package com.tuya.smart.commercial.lighting.demo.pages.area.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuya.smart.android.demo.R
import com.tuya.smart.commercial.lighting.demo.bean.GroupPackBeanWrapper
import com.tuya.smart.commercial.lighting.demo.pages.adapter.GroupListAdapter
import com.tuya.smart.commercial.lighting.demo.pages.area.presenter.GroupListPresenter
import com.tuya.smart.commercial.lighting.demo.widget.WrapContentLinearLayoutManager
import com.tuya.smart.uispecs.component.ProgressUtils
import com.tuya.smart.uispecs.component.dialog.BooleanConfirmAndCancelListener
import com.tuya.smart.uispecs.component.util.FamilyDialogUtils
import com.tuya.smart.utils.ToastUtil
import com.tuyasmart.stencil.base.fragment.BaseFragment

public class GroupListFragment : BaseFragment(), IGroupListView {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: View
    private var groupListAdapter: GroupListAdapter? = null

    private val grouListPresenter by lazy(LazyThreadSafetyMode.NONE) {
        GroupListPresenter(this)
    }

    override fun getPageName(): String {
        return TAG
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.cl_recycler_view, container, false)
        recyclerView = rootView.findViewById<RecyclerView>(R.id.rv_recycler_view)
        emptyView = rootView.findViewById<View>(R.id.emptyView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initRecyclerView()
        initData()
        grouListPresenter.refreshGroupList(false)
    }

    private fun initData() {
        grouListPresenter.setBundle(arguments)
    }

    private fun initRecyclerView() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager
                    if (layoutManager is LinearLayoutManager) {
                        val lastVisbileItem = layoutManager.findLastVisibleItemPosition()
                        if (grouListPresenter != null) {
                            if (lastVisbileItem == groupListAdapter!!.getItemCount() - 1) {
                                //last item is visible,and load more data
                                grouListPresenter.loadMoreData()
                            }
                        }
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

    }

    private fun initAdapter() {
        groupListAdapter = GroupListAdapter(activity)
        groupListAdapter?.setClickListener(object : GroupListAdapter.GroupClickListener {
            override fun onGroupClick(groupBean: GroupPackBeanWrapper?) {
            }

            override fun turnOn(groupBean: GroupPackBeanWrapper?) {
                grouListPresenter.turnOn(groupBean)
            }

            override fun turnOff(groupBean: GroupPackBeanWrapper?) {
                grouListPresenter.turnOff(groupBean)
            }

        });
        groupListAdapter?.setChangeListener { size ->
            if (size <= 0) {
                emptyView.visibility = View.VISIBLE
            }
        }
        recyclerView.layoutManager = WrapContentLinearLayoutManager(activity)
        recyclerView.adapter = groupListAdapter
    }

    override fun refreshGroupItem(groupInfo: GroupPackBeanWrapper) {
        groupListAdapter?.refreshGroupDevCount(groupInfo)
    }

    public fun toggleAreaId(areaId: Long) {
        grouListPresenter.toggleAreaId(areaId)
    }

    companion object {
        var TAG: String = "GroupListFragment"

        @JvmStatic
        fun newInstance(): GroupListFragment {
            return GroupListFragment()
        }
    }

    override fun loadMoreGroupList(result: MutableList<GroupPackBeanWrapper>?) {
        groupListAdapter?.addData(result)
        emptyView.visibility = View.GONE
    }

    override fun toast(msg: String?) {
        if (activity == null) {
            return
        }
        ToastUtil.showToast(activity, msg)
    }

    override fun toast(resId: Int) {
        if (activity == null) {
            return
        }
        ToastUtil.showToast(activity, resId)
    }

    override fun showEmptyView() {
        groupListAdapter?.clearData()
        groupListAdapter?.setNotifyDataSetChanged()
    }

    override fun clearGroupList() {
        groupListAdapter?.clearData()
    }

    override fun refreshGroupName(groupPackId: String, newName: String?) {
        groupListAdapter?.refreshGroupName(groupPackId, newName)
    }

    override fun addGroup(groupBean: GroupPackBeanWrapper?) {
        groupListAdapter?.addData(groupBean)
    }

    override fun hideProgress() {
        ProgressUtils.hideLoadingViewInPage()
    }

    override fun removeGroup(groupId: String) {
        groupListAdapter?.removeData(groupId)
    }

    override fun showProgress() {
        ProgressUtils.showLoadViewInPage(activity)
    }

    override fun onDestroy() {
        super.onDestroy()
        grouListPresenter.onDestroy()
    }

    override fun showEmptyGroupDialog(groupBean: GroupPackBeanWrapper, dismissGroup: Boolean) {

        if (activity == null || requireActivity().isFinishing || context == null) {
            return
        }

        var content = activity?.getString(R.string.group_no_device)
        FamilyDialogUtils.showConfirmAndCancelDialog(activity,
            if (dismissGroup) requireActivity().resources.getString(R.string.group_dismiss) else "",
            content,
            if (dismissGroup) requireActivity().resources.getString(R.string.group_dismiss) else requireActivity().resources.getString(R.string.ty_confirm),
            if (dismissGroup) requireContext().resources.getString(R.string.ty_cancel) else "", true,
            object : BooleanConfirmAndCancelListener {
                override fun onCancel(o: Any?): Boolean {
                    return true
                }

                override fun onConfirm(o: Any?): Boolean {
                    if (dismissGroup) {
                        groupBean?.groupPackBean?.groupPackageId?.let { grouListPresenter.dismissGroupPack(it) };
                    }
                    return true
                }
            });
    }

    override fun showGroupPackControlDialog(areaBeanWrapper: GroupPackBeanWrapper?) {
        if (activity == null || requireActivity().isFinishing) {
            return
        }
//        AreaDpControlDialog().newInstance(activity, areaBeanWrapper)
//                .show((activity as AppCompatActivity).supportFragmentManager, "");
    }
}