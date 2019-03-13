package com.chungo.baseapp.di.module

import com.chungo.base.di.scope.Scopes
import com.chungo.baseapp.mvp.contract.UserContract
import com.chungo.baseapp.mvp.model.UserModel
import dagger.Binds
import dagger.Module

@Module
abstract class UserModuleBinds {

    @Scopes.Activity
    @Binds
    abstract fun bindUserModel(model: UserModel): UserContract.Model

}