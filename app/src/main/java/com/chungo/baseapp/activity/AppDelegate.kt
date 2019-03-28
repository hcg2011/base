package com.chungo.baseapp.activity

import android.app.Application
import android.content.Context
import android.util.Log
import com.chungo.base.delegate.BaseAppDelegate
import com.chungo.baseapp.di.component.DaggerAppComponent

/**
 * AppDelegate 代理 Application 的生命周期,在对应的生命周期,执行对应的逻辑,因为 Java 只能单继承
 * 所以当遇到某些三方库需要继承于它的 Application 的时候,就只有自定义 Application 并继承于三方库的 Application
 * 这时就不用再继承 BaseApplication,只用在自定义Application中对应的生命周期调用AppDelegate对应的方法
 * (Application一定要实现APP接口),框架就能照常运行
 *
 * @see BaseApplication
 *
 */
open class AppDelegate(context: Context) : BaseAppDelegate(context) {

    override fun onCreate(application: Application) {
        Log.d("hcg_log", "onCreate=" + this)
        mAppComponent = DaggerAppComponent
                .builder()
                .application(application)//提供application
                .globalConfigModule(getGlobalConfigModule(application, mModules))//全局配置
                .build()
        (mAppComponent as DaggerAppComponent).inject(this)
        super.onCreate(application)
    }
}

