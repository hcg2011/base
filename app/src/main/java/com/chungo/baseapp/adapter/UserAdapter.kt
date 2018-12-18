/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chungo.baseapp.adapter

import android.view.View

import com.chungo.baseapp.R
import com.chungo.baseapp.holder.UserItemHolder
import com.chungo.basemore.mvp.model.entity.User


/**
 * ================================================
 * 展示 [DefaultAdapter] 的用法
 *
 *
 * Created by JessYan on 09/04/2016 12:57
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class UserAdapter(infos: List<User>) : DefaultAdapter<User>(infos) {

    override fun getHolder(v: View, viewType: Int): BaseHolder<User> {
        return UserItemHolder(v)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.recycle_list
    }
}
