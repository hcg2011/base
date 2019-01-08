package com.chungo.baseapp.adapter

import android.view.View
import com.chungo.baseapp.R
import com.chungo.baseapp.holder.UserItemHolder
import com.chungo.basemore.mvp.model.entity.User

/**
 * 展示 [DefaultAdapter] 的用法
 *
 */
class UserAdapter(infos: List<User>) : DefaultAdapter<User>(infos) {

    override fun getHolder(v: View, viewType: Int): BaseHolder<User> {
        return UserItemHolder(v)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.recycle_list
    }
}
