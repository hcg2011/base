package com.chungo.base.config

import android.app.Application
import android.content.Context
import android.support.v4.app.FragmentManager
import com.chungo.base.di.module.GlobalConfigModule
import com.chungo.base.lifecycle.IAppLifecycles

/**
 * [ConfigModule] 可以给框架配置一些参数,需要实现 [ConfigModule] 后,在 AndroidManifest 中声明该实现类
 *
 */
interface ConfigModule {
    /**
     * 使用[GlobalConfigModule.Builder]给框架配置一些配置参数
     */
    fun applyOptions(context: Context, builder: GlobalConfigModule)

    /**
     * 使用[IAppLifecycles]在Application的生命周期中注入一些操作
     *
     */
    fun injectAppLifecycle(context: Context, lifecycles: MutableList<IAppLifecycles>)

    /**
     * 使用[Application.ActivityLifecycleCallbacks]在Activity的生命周期中注入一些操作
     */
    fun injectActivityLifecycle(context: Context, lifecycles: MutableList<Application.ActivityLifecycleCallbacks>)

    /**
     * 使用[FragmentManager.FragmentLifecycleCallbacks]在Fragment的生命周期中注入一些操作
     */
    fun injectFragmentLifecycle(context: Context, lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>)
}
