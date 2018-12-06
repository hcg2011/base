package com.chungo.base.widget.rv

import android.util.SparseArray

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.util.MultiTypeDelegate

/**
 * @Description
 * @Author huangchangguo
 * @Created 2018/7/12 17:59
 */
class MultiDelegateProvider<T : MultiItemEntity, K : BaseItemDelegate<T, BaseViewHolder>> : MultiTypeDelegate<T>() {
    var itemDelegates = SparseArray<K>()
        protected set

    val delegateCount: Int
        get() = itemDelegates.size()

    val defItemType: Int
        get() = DEFAULT_VIEW_TYPE

    override fun getItemType(t: T): Int {
        return if (!useItemDelegate()) defItemType else getItemViewType(t)
    }

    private fun useItemDelegate(): Boolean {
        return delegateCount > 0
    }

    fun registerDelegate(delegate: K): MultiDelegateProvider<T, K> {
        cheakRegister(delegate)
        val viewType = delegate.viewType
        val layoutId = delegate.layoutId
        itemDelegates.put(viewType, delegate)
        registerItemType(viewType, layoutId)
        return this
    }

    private fun cheakRegister(delegate: K) {
        cheackDelegate(delegate)
        val viewType = delegate.viewType
        if (itemDelegates.get(viewType) != null) {
            throw IllegalArgumentException(
                    "An ItemViewDelegate is already registered for the viewType = "
                            + viewType
                            + ". Already registered ItemViewDelegate is "
                            + itemDelegates.get(viewType))
        }
    }

    private fun cheackDelegate(delegate: K?) {
        if (delegate == null) {
            throw IllegalArgumentException("ItemProvider can not be null")
        }
    }

    fun unRegisterDelegate(delegate: K): MultiDelegateProvider<T, K> {
        cheackDelegate(delegate)
        val indexToRemove = itemDelegates.indexOfValue(delegate)
        if (indexToRemove >= 0) {
            itemDelegates.removeAt(indexToRemove)
        }
        return this
    }

    fun unRegisterDelegate(itemType: Int): MultiDelegateProvider<T, K> {
        val indexToRemove = itemDelegates.indexOfKey(itemType)
        if (indexToRemove >= 0) {
            itemDelegates.removeAt(indexToRemove)
        }
        return this
    }

    /**
     * viewType
     *
     * @param item
     * @return
     */
    fun getItemViewType(item: T): Int {
        val delegatesCount = itemDelegates.size()
        for (i in delegatesCount - 1 downTo 0) {
            val delegate = itemDelegates.valueAt(i)
            if (delegate.isForViewType(item)) {
                return itemDelegates.keyAt(i)
            }
        }
        return defItemType
    }

    fun isDefItemType(type: Int): Boolean {
        return type == DEFAULT_VIEW_TYPE
    }

    fun convert(holder: BaseViewHolder, item: T, position: Int): K? {
        val viewType = holder.itemViewType
        val delegate = itemDelegates.get(viewType)
        delegate?.convert(holder, item, position)
        return delegate
    }


    fun getItemViewDelegate(viewType: Int): K {
        return itemDelegates.get(viewType)
    }

    fun getItemLayoutId(viewType: Int): Int {
        return getItemViewDelegate(viewType).layoutId
    }

    fun getItemViewType(delegate: K): Int {
        return itemDelegates.indexOfValue(delegate)
    }

    companion object {
        protected val DEFAULT_VIEW_TYPE = -0xff
    }

}
