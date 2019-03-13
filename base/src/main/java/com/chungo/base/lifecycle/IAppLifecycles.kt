package com.chungo.base.lifecycle

import android.app.Activity
import android.app.Application
import android.content.Context
import dagger.android.AndroidInjector

/**
 * 用于代理 [Application] 的生命周期
 */
interface IAppLifecycles {
    fun attachBaseContext(base: Context)

    fun onCreate(application: Application)

    fun onTerminate(application: Application)

    fun activityInjector(): AndroidInjector<Activity>?
}
