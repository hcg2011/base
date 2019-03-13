package com.chungo.baseapp.activity

import android.app.Application
import android.content.Context
import com.chungo.base.lifecycle.IAppLifecycles
import com.chungo.base.utils.AppUtils
import com.chungo.base.utils.Preconditions
import com.chungo.baseapp.di.component.AppComponent
import com.chungo.baseapp.lifecycle.AppDelegate
import com.chungo.baseapp.lifecycle.IApp


/**
 * 基于MVPArms，改造为 kotlin版本 是一个整合了 MVP + Dagger2 + Retrofit + RxJava
 */
abstract class BaseApplication : Application(), IApp {
    protected var mAppDelegate: IAppLifecycles? = null

    /**
     * 将 [AppComponent] 返回出去, 供其它地方使用, [AppComponent] 接口中声明的方法所返回的实例, 在 [.getAppComponent] 拿到对象后都可以直接使用
     *
     * @see AppUtils.obtainAppComponentFromContext
     * @return AppComponent
     */
    override val appComponent: AppComponent
        get() {
            Preconditions.checkNotNull<IAppLifecycles>(mAppDelegate, "%s cannot be null", AppDelegate::class.java.name)
            Preconditions.checkState(mAppDelegate is IApp, "%s must be implements %s", mAppDelegate!!.javaClass.name, IApp::class.java.name)
            return (mAppDelegate as IApp).appComponent
        }

    /**
     * 这里会在 [BaseApplication.onCreate] 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param context
     */
    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        if (mAppDelegate == null)
            this.mAppDelegate = AppDelegate(context)
        this.mAppDelegate!!.attachBaseContext(context)
    }

    override fun onCreate() {
        super.onCreate()
        if (mAppDelegate != null)
            mAppDelegate!!.onCreate(this)
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    override fun onTerminate() {
        super.onTerminate()
        if (mAppDelegate != null)
            mAppDelegate!!.onTerminate(this)
    }

}
