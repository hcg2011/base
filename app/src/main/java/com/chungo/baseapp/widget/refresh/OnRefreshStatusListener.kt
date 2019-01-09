package com.chungo.baseapp.widget.refresh


import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener

/**
 * @Description
 * @Author huangchangguo
 * @Created 2018/8/22 14:56
 */
abstract class OnRefreshStatusListener : OnMultiPurposeListener {

    abstract fun onHeaderPulling()

    abstract fun onHeaderFinish()

    override fun onHeaderMoving(header: RefreshHeader, isDragging: Boolean, percent: Float, offset: Int, headerHeight: Int, maxDragHeight: Int) {
        onHeaderPulling()
    }

    override fun onHeaderReleased(header: RefreshHeader, headerHeight: Int, maxDragHeight: Int) {

    }

    override fun onHeaderStartAnimator(header: RefreshHeader, headerHeight: Int, extendHeight: Int) {

    }

    override fun onHeaderFinish(header: RefreshHeader, success: Boolean) {
        onHeaderFinish()
    }

    override fun onFooterMoving(footer: RefreshFooter, isDragging: Boolean, percent: Float, offset: Int, footerHeight: Int, maxDragHeight: Int) {

    }

    override fun onFooterStartAnimator(footer: RefreshFooter, footerHeight: Int, extendHeight: Int) {

    }

    override fun onFooterFinish(footer: RefreshFooter, success: Boolean) {

    }

    override fun onFooterReleased(footer: RefreshFooter, footerHeight: Int, maxDragHeight: Int) {

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }

    override fun onRefresh(refreshlayout: RefreshLayout) {

    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {

    }
}
