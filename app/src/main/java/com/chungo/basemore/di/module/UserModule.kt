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
package com.chungo.basemore.di.module

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.chungo.base.di.scope.Scopes
import com.chungo.baseapp.adapter.UserAdapter
import com.chungo.basemore.mvp.contract.UserContract
import com.chungo.basemore.mvp.model.entity.User
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides

/**
 * ================================================
 * 展示 Module 的用法
 *
 * @see [Module wiki 官方文档](https://github.com/JessYanCoding/MVPArms/wiki.2.4.5)
 * Created by JessYan on 09/04/2016 11:10
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
@Module
class UserModule {


    @Scopes.Activity
    @Provides
    fun provideRxPermissions(view: UserContract.View): RxPermissions {
        return RxPermissions(view.obtainActivity() as FragmentActivity)
    }

    @Scopes.Activity
    @Provides
    fun provideLayoutManager(view: UserContract.View): RecyclerView.LayoutManager {
        return GridLayoutManager(view.obtainActivity(), 3) as RecyclerView.LayoutManager
    }

    @Scopes.Activity
    @Provides
    fun provideUserList(): MutableList<User> {
        return mutableListOf()
    }

    @Scopes.Activity
    @Provides
    fun provideUserAdapter(list: List<User>): RecyclerView.Adapter<*> {
        return UserAdapter(list)
    }

}
