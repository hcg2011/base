package com.chungo.base.widget.rv

import android.view.View
import android.view.ViewGroup

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @Description 针对多类型的item，通过代理的形式来分发。代理管理类
 * @Author huangchangguo
 * @Created 2018/7/13 14:51
 */

abstract class BaseItemDelegate<T : MultiItemEntity, V : BaseViewHolder> {
    /**
     * 布局形式的item，和 [.getItemView]每个代理只能使用其中一种
     */
    abstract val layoutId: Int

    abstract val viewType: Int

    /**
     * 自定义view的item，和 [.getLayoutId]每个代理只能使用其中一种。
     * 用于动态判断，不使用请返回null
     */
    abstract fun getItemView(parent: ViewGroup): View

    //bindview的时候调用
    abstract fun convert(holder: V, t: T, position: Int)

    //用于判断当前的代理item和数据是否匹配
    fun isForViewType(item: T): Boolean {
        return item.itemType == viewType
    }

    //子类若想实现条目点击事件则重写该方法
    //Subclasses override this method if you want to implement an item click event
    fun onClick(helper: V, data: T, position: Int) {

    }

    //子类若想实现条目长按事件则重写该方法，如果adapter里面已经设置了itemClick，则该方法自动失效
    //Subclasses override this method if you want to implement an item long press event
    fun onLongClick(helper: V, data: T, position: Int): Boolean {
        return false
    }

    //子类若想实现条目触摸事件则重写该方法，如果adapter里面已经设置了itemClick，则该方法自动失效
    fun onTouch(helper: V, data: T, position: Int): Boolean {
        return false
    }
}
