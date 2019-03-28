package com.chungo.baseapp.di.module

import com.chungo.base.di.scope.Scopes
import com.chungo.baseapp.activity.DemoService
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @Description
 *
 * @Author huangchangguo
 * @Created  2019/3/12 10:46
 *
 */
@Module
abstract class ServiceBindModule {

    @Scopes.Service
    @ContributesAndroidInjector(modules = [UserServiceModule::class])
    abstract fun UserServiceInjector(): DemoService
}