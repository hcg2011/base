package com.chungo.baseapp.di.component

import com.chungo.base.di.scope.Scopes
import com.chungo.baseapp.activity.DetailActivity
import com.chungo.baseapp.activity.MainActivity
import com.chungo.baseapp.mvp.contract.UserContract
import dagger.Module
import dagger.Provides

/**
 * @Description
 *
 * @Author huangchangguo
 * @Created  2019/3/14 16:25
 *
 */
interface ActivityModule {
    @Module
    class Detail {
        @Scopes.Activity
        @Provides
        fun provideDetail(activity: DetailActivity): UserContract.View {
            return activity
        }
    }

    @Module
    class Main {
        @Scopes.Activity
        @Provides
        fun provideDetail(activity: MainActivity): UserContract.View {
            return activity
        }
    }
}