package com.chungo.base.lifecycle

import android.app.Activity
import android.app.Fragment
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ContentProvider
import dagger.android.AndroidInjector

/**
 * @Description dagger2-安卓管理的注入类
 *
 * @Author huangchangguo
 * @Created  2019/3/27 15:23
 *
 */
interface IAndroidInjectorLifecycles : IAppLifecycles {

    fun activityInjector(): AndroidInjector<Activity>?

    fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver>?

    fun fragmentInjector(): AndroidInjector<Fragment>?

    fun serviceInjector(): AndroidInjector<Service>?

    fun contentProviderInjector(): AndroidInjector<ContentProvider>?

    fun supportFragmentInjector(): AndroidInjector<android.support.v4.app.Fragment>?
}