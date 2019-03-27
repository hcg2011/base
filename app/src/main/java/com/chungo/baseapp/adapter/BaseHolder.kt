package com.chungo.baseapp.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    protected var mOnViewClickListener: OnViewClickListener? = null
    protected val TAG = this.javaClass.simpleName

    init {
        itemView.setOnClickListener(this)//点击事件
    }


    /**
     * 设置数据
     *
     * @param data
     * @param position
     */
    abstract fun setData(data: T, position: Int)


    /**
     * 在 Activity 的 onDestroy 中使用 [DefaultAdapter.releaseAllHolder] 方法 (super.onDestroy() 之前)
     * [BaseHolder.onRelease] 才会被调用, 可以在此方法中释放一些资源
     */
    open fun onRelease() {

    }

    override fun onClick(view: View) {
        if (mOnViewClickListener != null) {
            mOnViewClickListener!!.onViewClick(view, layoutPosition)
        }
    }

    interface OnViewClickListener {
        fun onViewClick(view: View, position: Int)
    }

    fun setOnItemClickListener(listener: OnViewClickListener) {
        this.mOnViewClickListener = listener
    }
}
