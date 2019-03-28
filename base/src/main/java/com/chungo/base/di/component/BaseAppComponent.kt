package com.chungo.base.di.component

import android.app.Application
import com.chungo.base.delegate.BaseAppDelegate
import com.chungo.base.di.module.*
import com.chungo.base.utils.AppUtils
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * 可通过 [AppUtils.obtainAppComponentFromContext] 拿到此接口的实现类
 * 拥有此接口的实现类即可调用对应的方法拿到 Dagger 提供的对应实例
 *
 */
@Singleton
@Component(modules = [
    AppModule::class,
    AppModuleBinds::class,
    NetModule::class,
    RxCacheModule::class,
    GlobalConfigModule::class,
    InterceptorModuleBinds::class,
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class])
interface BaseAppComponent : IComponent {

    fun inject(delegate: BaseAppDelegate)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun globalConfigModule(globalConfigModule: GlobalConfigModule): Builder

        fun build(): BaseAppComponent
    }
}
