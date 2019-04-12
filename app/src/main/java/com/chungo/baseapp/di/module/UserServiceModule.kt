package com.chungo.baseapp.di.module

import com.chungo.base.di.scope.Scopes
import com.chungo.baseapp.adapter.UserAdapter
import com.chungo.baseapp.mvp.model.entity.User
import dagger.Module
import dagger.Provides

@Module
class UserServiceModule {

    @Scopes.Service
    @Provides
    fun provideUserList(): MutableList<User> {
        return mutableListOf()
    }

    @Scopes.Service
    @Provides
    fun provideUserAdapter(): UserAdapter {
        return UserAdapter()
    }
}
