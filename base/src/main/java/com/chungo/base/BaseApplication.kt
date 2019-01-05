package com.chungo.base

import android.app.Application
import android.content.Context
import com.chungo.base.base.delegate.App
import com.chungo.base.base.delegate.AppDelegate
import com.chungo.base.di.component.AppComponent
import com.chungo.base.lifecycle.IAppLifecycles
import com.chungo.base.utils.ArmsUtils
import com.chungo.base.utils.Preconditions


/**
 * MVPArms 是一个整合了大量主流开源项目的 Android MVP 快速搭建框架, 其中包含 Dagger2、Retrofit、RxJava 以及
 * RxLifecycle、RxCache 等 Rx 系三方库, 并且提供 UI 自适应方案, 本框架将它们结合起来, 并全部使用 Dagger2 管理
 * 并提供给开发者使用, 使用本框架开发您的项目, 就意味着您已经拥有一个 MVP + Dagger2 + Retrofit + RxJava 项目
 *
 */
class BaseApplication : Application(), App {
    private var mAppDelegate: IAppLifecycles? = null

    /**
     * 将 [AppComponent] 返回出去, 供其它地方使用, [AppComponent] 接口中声明的方法所返回的实例, 在 [.getAppComponent] 拿到对象后都可以直接使用
     *
     * @see ArmsUtils.obtainAppComponentFromContext
     * @return AppComponent
     */
    override val appComponent: AppComponent
        get() {
            Preconditions.checkNotNull<IAppLifecycles>(mAppDelegate, "%s cannot be null", AppDelegate::class.java.name)
            Preconditions.checkState(mAppDelegate is App, "%s must be implements %s", mAppDelegate!!.javaClass.name, App::class.java.name)
            return (mAppDelegate as App).appComponent
        }

    /**
     * 这里会在 [BaseApplication.onCreate] 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (mAppDelegate == null)
            this.mAppDelegate = AppDelegate(base)
        this.mAppDelegate!!.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        if (mAppDelegate != null)
            this.mAppDelegate!!.onCreate(this)
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    override fun onTerminate() {
        super.onTerminate()
        if (mAppDelegate != null)
            this.mAppDelegate!!.onTerminate(this)
    }

}
