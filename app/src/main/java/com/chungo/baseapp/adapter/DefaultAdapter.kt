package com.chungo.baseapp.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * 基类 [RecyclerView.Adapter] ,如果需要实现非常复杂的 [RecyclerView] ,请尽量使用其他优秀的三方库
 *
 *
 */
abstract class DefaultAdapter<T> : RecyclerView.Adapter<BaseHolder<T>>() {
    var infos: List<T>?=null
    protected var mOnItemClickListener: OnRecyclerViewItemClickListener<T>? = null
    private lateinit var mHolder: BaseHolder<T>

    /**
     * 创建 [BaseHolder]
     *
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<T> {
        val view = LayoutInflater.from(parent.context).inflate(getLayoutId(viewType), parent, false)
        mHolder = getHolder(view, viewType)
        mHolder.setOnItemClickListener(object : BaseHolder.OnViewClickListener {
            override fun onViewClick(view: View, position: Int) {
                //设置Item点击事件
                if (mOnItemClickListener != null && infos!!.size > 0) {
                    mOnItemClickListener!!.onItemClick(view, viewType, infos!![position], position)
                }
            }

        })
        return mHolder
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: BaseHolder<T>, position: Int) {
        holder.setData(infos!![position], position)
    }


    /**
     * 返回数据的个数
     *
     * @return
     */
    override fun getItemCount(): Int {
        return infos!!.size
    }

    /**
     * 获得某个 `position` 上的 item 的数据
     *
     * @param position
     * @return
     */
    fun getItem(position: Int): T? = if (infos == null) null else infos?.let { it[position] }

    /**
     * 让子类实现用以提供 [BaseHolder]
     *
     * @param v
     * @param viewType
     * @return
     */
    abstract fun getHolder(v: View, viewType: Int): BaseHolder<T>

    /**
     * 提供用于 `item` 布局的 `layoutId`
     *
     * @param viewType
     * @return
     */
    abstract fun getLayoutId(viewType: Int): Int


    interface OnRecyclerViewItemClickListener<T> {
        fun onItemClick(view: View, viewType: Int, data: T, position: Int)
    }

    fun setOnItemClickListener(listener: OnRecyclerViewItemClickListener<T>) {
        this.mOnItemClickListener = listener
    }

    companion object {


        /**
         * 遍历所有[BaseHolder],释放他们需要释放的资源
         *
         * @param recyclerView
         */
        fun releaseAllHolder(recyclerView: RecyclerView?) {
            if (recyclerView == null) return
            for (i in recyclerView.childCount - 1 downTo 0) {
                val view = recyclerView.getChildAt(i)
                val viewHolder = recyclerView.getChildViewHolder(view)
                if (viewHolder != null && viewHolder is BaseHolder<*>) {
                    viewHolder.onRelease()
                }
            }
        }
    }
}
