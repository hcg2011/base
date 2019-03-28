package com.chungo.baseapp.di.component

import android.app.Application
import com.chungo.base.di.component.IComponent
import com.chungo.base.di.module.*
import com.chungo.base.utils.AppUtils
import com.chungo.baseapp.activity.AppDelegate
import com.chungo.baseapp.di.module.ActivityBindModule
import com.chungo.baseapp.di.module.ServiceBindModule
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
    InterceptorModuleBinds::class,
    GlobalConfigModule::class,
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,

    ServiceBindModule::class,
    ActivityBindModule::class])
interface AppComponent : IComponent {
    fun inject(delegate: AppDelegate)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun globalConfigModule(globalConfigModule: GlobalConfigModule): Builder
        fun build(): AppComponent
    }
}
