package com.chungo.base.base

import android.app.Activity
import android.app.Application
import android.app.Fragment
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ContentProvider
import android.content.Context
import com.chungo.base.delegate.IApp
import com.chungo.base.lifecycle.IAndroidInjectorLifecycles
import dagger.android.*
import dagger.android.support.HasSupportFragmentInjector


/**
 * 基于MVPArms，改造为 kotlin版本 是一个整合了 MVP + Dagger2 + Retrofit + RxJava
 */
abstract class BaseApplication : Application(), IApp,
        HasActivityInjector,
        HasBroadcastReceiverInjector,
        HasFragmentInjector,
        HasServiceInjector,
        HasContentProviderInjector,
        HasSupportFragmentInjector {
    protected var mAppDelegate: IAndroidInjectorLifecycles? = null

    override fun activityInjector(): AndroidInjector<Activity>? = mAppDelegate!!.activityInjector()
    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver>? = mAppDelegate!!.broadcastReceiverInjector()
    override fun fragmentInjector(): AndroidInjector<Fragment>? = mAppDelegate!!.fragmentInjector()
    override fun serviceInjector(): AndroidInjector<Service>? = mAppDelegate!!.serviceInjector()
    override fun contentProviderInjector(): AndroidInjector<ContentProvider>? = mAppDelegate!!.contentProviderInjector()
    override fun supportFragmentInjector(): AndroidInjector<android.support.v4.app.Fragment>? = mAppDelegate!!.supportFragmentInjector()

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
