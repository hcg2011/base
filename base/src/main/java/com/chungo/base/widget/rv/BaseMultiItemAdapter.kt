package com.chungo.base.widget.rv

import android.support.annotation.IntRange
import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.IExpandable
import com.chad.library.adapter.base.entity.MultiItemEntity
import java.util.*

/**
 * @Description 当有多种条目的时候，避免在convert()中做太多的业务逻辑，把逻辑放在对应的ItemProvider中
 * @Author huangchangguo
 * @Created 2018/7/13 14:51
 */

abstract class BaseMultiItemAdapter<T : MultiItemEntity, V : BaseViewHolder> : BaseQuickAdapter<T, V> {
    protected lateinit var mProvider: MultiDelegateProvider<T, BaseItemDelegate<T, BaseViewHolder>>
    protected lateinit var mClickTypes: MutableMap<Int, Boolean>


    @JvmOverloads
    constructor(@LayoutRes layoutResId: Int = 0, data: List<T>? = null) : super(layoutResId, data) {
        initialize()
    }

    /**
     * 注册多item的代理类
     */
    abstract fun registerDelegate()

    protected fun initialize() {
        if (mProvider == null)
            mProvider = MultiDelegateProvider()
        registerDelegate()
        setMultiTypeDelegate(mProvider)
    }

    /**
     * 添加item代理
     *
     * @param delegate
     * @return
     */
    fun addItemDelegate(delegate: BaseItemDelegate<T, V>): BaseMultiItemAdapter<T, V> {
        if (mProvider != null)
            mProvider.registerDelegate(delegate as BaseItemDelegate<T, BaseViewHolder>)
        return this
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): V {
        var layoutId = mLayoutResId
        val delegate = mProvider!!.getItemViewDelegate(viewType)
                ?: return createBaseViewHolder(parent, layoutId)
        val itemView = delegate.getItemView(parent)
        //是否使用的layout形式创建item。通过delegate.getItemView() 是否为空来判断
        if (itemView == null) {//layout形式
            if (!mProvider.isDefItemType(viewType))
                if (multiTypeDelegate != null) {
                    layoutId = multiTypeDelegate.getLayoutId(viewType)
                }
            return createBaseViewHolder(parent, layoutId)
        } else {//view形式
            return createBaseViewHolder(itemView)
        }
    }

    override fun convert(helper: V, item: T) {
        val position = helper.layoutPosition - headerLayoutCount

        val delegate = mProvider!!.convert(helper, item, position)

        bindClick(helper, item, position, delegate as BaseItemDelegate<T, V>)
    }

    private fun bindClick(helper: V, item: T, position: Int, delegate: BaseItemDelegate<T, V>?) {
        if (delegate == null || !isEnabled(delegate.viewType))
            return
        val clickListener = onItemClickListener
        val longClickListener = onItemLongClickListener
        val itemView = helper.itemView
        //设置touch
        itemView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                return delegate.onTouch(helper, item, position)
            }
        })

        //如果已经设置了子条目点击监听和子条目长按监听
        if (clickListener != null && longClickListener != null)
            return
        //如果没有设置长按监听，则回调给itemDelegate
        if (clickListener == null) {
            itemView.setOnClickListener { delegate.onClick(helper, item, position) }
        }
        //如果没有设置长按监听，则回调给itemDelegate
        if (longClickListener == null) {
            itemView.setOnLongClickListener { delegate.onLongClick(helper, item, position) }
        }
    }

    /**
     * 指定type的点击事件是否可用
     *
     * @param isEnable def true
     * @param viewType
     */
    fun setClickEnableForViewType(isEnable: Boolean, viewType: Int) {
        if (mClickTypes == null && mClickTypes!!.size <= 0)
            mClickTypes = HashMap()
        mClickTypes[viewType] = isEnable
    }

    /**
     * 判断item的类型是否可用
     *
     * @param viewType
     * @return
     */
    protected fun isEnabled(viewType: Int): Boolean {
        return (mClickTypes == null
                || mClickTypes!!.size <= 0
                || !mClickTypes!!.containsKey(viewType)
                || mClickTypes!![viewType]!!)
    }

    override fun remove(@IntRange(from = 0L) position: Int) {
        if (mData == null
                || position < 0
                || position >= mData.size)
            return

        val entity = mData[position]
        if (entity is IExpandable<*>) {
            removeAllChild(entity as IExpandable<*>, position)
        }
        removeDataFromParent(entity)
        super.remove(position)
    }

    /**
     * 移除子控件时，移除父控件实体类中相关子控件数据，避免关闭后再次展开数据重现
     *
     * @param child 子控件实体
     */
    protected fun removeDataFromParent(child: T) {
        val position = getParentPosition(child)
        if (position >= 0) {
            val parent = mData[position] as IExpandable<*>
            parent.subItems.remove(child)
        }
    }

    /**
     * 移除父控件时，若父控件处于展开状态，则先移除其所有的子控件
     *
     * @param parent         父控件实体
     * @param parentPosition 父控件位置
     */
    protected fun removeAllChild(parent: IExpandable<*>, parentPosition: Int) {
        if (parent.isExpanded) {
            val chidChilds = parent.subItems
            if (chidChilds == null || chidChilds.size == 0)
                return

            val childSize = chidChilds.size
            for (i in 0 until childSize) {
                remove(parentPosition + 1)
            }
        }
    }

    fun addAll(data: List<T>) {
        mData.addAll(data)
        notifyItemRangeInserted(itemCount, itemCount + data.size)
    }

    /**
     * 伴随清除缓存的添加数据。只能用于LinearLayoutManager
     *
     * @param rv
     * @param data
     */
    fun addAllAndClearCache(rv: RecyclerView, data: MutableList<T>) {
        if (mData.size > MAX_CACHE) {
            val scrollNum = dealItem(rv, data)
            notifyDataSetChanged()
            try {
                if (scrollNum != 0)
                    rv.scrollBy(0, scrollNum)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            System.gc()
        } else {
            addAll(data)
        }
    }

    private fun InsertedData(rv: RecyclerView, data: List<T>) {
        if (rv.scrollState == RecyclerView.SCROLL_STATE_IDLE && rv.isComputingLayout == false) {
            mData.addAll(data)
            notifyItemRangeInserted(itemCount,
                    itemCount + data.size)
        }
    }

    private fun dealItem(rv: RecyclerView, data: MutableList<T>): Int {
        val managers = rv.layoutManager
        if (managers !is RecyclerView.LayoutManager) {
            mData.addAll(data)
            return 0
        }
        try {
            val manager = managers as LinearLayoutManager?
            val position = manager!!.findFirstVisibleItemPosition()
            for (i in 1..mData.size - position) {
                val t = mData[mData.size - i]
                data.add(0, t)
            }
            val itemHeight = Math.abs(manager.findViewByPosition(position)!!
                    .top)
            mData.clear()
            mData.addAll(data)
            return itemHeight
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0
    }

    companion object {
        val MAX_CACHE = 30 //item的最大缓存数，超过则清除。用于控制无限下拉导致数据过多的情况
    }

}
