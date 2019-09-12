package com.chungo.base.base

import android.app.Application
import android.content.Context
import com.chungo.base.delegate.BaseAppDelegate
import com.chungo.base.di.component.IComponent
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector


/**
 * 基于MVPArms，改造为 kotlin版本 是一个整合了 MVP + Dagger2 + Retrofit + RxJava
 */
abstract class BaseApplication : Application(), IApp, HasAndroidInjector {
    protected var mAppDelegate: BaseAppDelegate? = null
    override fun androidInjector(): AndroidInjector<Any>? = mAppDelegate!!.androidInjector()

    override var mAppComponent: IComponent? = null
        get() = mAppDelegate?.mAppComponent

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        attachAppDelegate(context)
        mAppDelegate?.attachBaseContext(context)
    }

    /**代理Application 的生命周期**/
    abstract fun attachAppDelegate(context: Context)

    override fun onCreate() {
        super.onCreate()
        mAppDelegate?.onCreate(this)
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    override fun onTerminate() {
        super.onTerminate()
        mAppDelegate?.onTerminate(this)
    }
}
