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
        return GridLayoutManager(view.obtainActivity(), 3)
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
