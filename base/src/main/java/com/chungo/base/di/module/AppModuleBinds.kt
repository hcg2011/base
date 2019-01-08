package com.chungo.base.di.module

import android.app.Application
import android.support.v4.app.FragmentManager
import com.chungo.base.di.scope.Qualifiers
import com.chungo.base.lifecycle.ActivityLifecycle
import com.chungo.base.lifecycle.FragmentLifecycle
import com.chungo.base.http.IRepositoryManager
import com.chungo.base.http.RepositoryManager
import com.chungo.base.lifecycle.rx.ActivityLifecycleForRxLifecycle
import dagger.Binds
import dagger.Module

/**
 * 提供一些框架必须的实例的 [Module]
 *
 */
@Module
abstract class AppModuleBinds {

    @Binds
    abstract fun bindRepositoryManager(repositoryManager: RepositoryManager): IRepositoryManager

    @Binds
    @Qualifiers.Lifecycle
    abstract fun bindActivityLifecycle(activityLifecycle: ActivityLifecycle): Application.ActivityLifecycleCallbacks

    @Qualifiers.RxLifecycle
    @Binds
    abstract fun bindActivityForRxLifecycle(activityRxLifecycle: ActivityLifecycleForRxLifecycle): Application.ActivityLifecycleCallbacks

    @Binds
    abstract fun bindFragmentLifecycle(fragmentLifecycle: FragmentLifecycle): FragmentManager.FragmentLifecycleCallbacks
}
