package com.chungo.baseapp.activity

import android.app.Activity
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector

/**
 * @Description
 *
 * @Author huangchangguo
 * @Created  2019/1/11 18:03
 *
 */
class App : BaseApplication(), HasActivityInjector {
//    override val appComponent: AppComponent
//        get() = super.appComponent

    override fun activityInjector(): AndroidInjector<Activity>? {
        val injector = mAppDelegate!!.activityInjector()
        return injector
    }
}