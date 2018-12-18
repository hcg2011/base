package com.chungo.base.lifecycle

import android.app.Application
import android.content.Context

/**
 * ================================================
 * 用于代理 [Application] 的生命周期
 *
 * @see AppDelegate
 */
interface AppLifecycles {
    fun attachBaseContext(base: Context)

    fun onCreate(application: Application)

    fun onTerminate(application: Application)
}
