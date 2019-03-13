package com.chungo.baseapp.di.module

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.chungo.base.di.scope.Scopes
import com.chungo.baseapp.adapter.UserAdapter
import com.chungo.baseapp.mvp.contract.UserContract
import com.chungo.baseapp.mvp.model.entity.User
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides

@Module
class UserModule {

//    @Scopes.Activity
//    @Provides
//    fun provideRxPermissions(activity: DetailActivity): UserContract.View {
//        return activity
//    }

//    @Scopes.Activity
//    @Provides
//    fun provideRxPermissions(activity: Activity): UserContract.View {
//        return activity as UserContract.View
//    }


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
    fun provideUserAdapter(list: List<User>): UserAdapter {
        return UserAdapter(list)
    }

}
