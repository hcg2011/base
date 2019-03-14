package com.chungo.baseapp.di.module

import com.chungo.base.di.scope.Scopes
import com.chungo.baseapp.activity.DetailActivity
import com.chungo.baseapp.activity.MainActivity
import com.chungo.baseapp.di.component.ActivityModule
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
abstract class ActivityBindModule {

    @Scopes.Activity
    @ContributesAndroidInjector(modules = [
        ActivityModule.Main::class,
        UserModule::class,
        UserModuleBinds::class])
    abstract fun UserActivityInjector(): MainActivity

    @Scopes.Activity
    @ContributesAndroidInjector(modules = [
        ActivityModule.Detail::class,
        UserModule::class,
        UserModuleBinds::class])
    abstract fun DetailActivityInjector(): DetailActivity
}