package com.chungo.basemore.di.module

import com.chungo.base.di.scope.Scopes
import com.chungo.basemore.mvp.contract.UserContract
import com.chungo.basemore.mvp.model.UserModel
import dagger.Binds
import dagger.Module

@Module
abstract class UserModuleBinds {

    @Scopes.Activity
    @Binds
    abstract fun bindUserModel(model: UserModel): UserContract.Model
}
