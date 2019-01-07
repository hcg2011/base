package com.chungo.base.di.module

import android.app.Application
import android.support.v4.app.FragmentManager
import com.chungo.base.integration.AppManager
import com.chungo.base.integration.cache.Cache
import com.chungo.base.integration.cache.CacheType
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * 提供一些必须的实例的 [Module]
 */
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideAppManager(application: Application): AppManager? {
        return AppManager.getManager().init(application)
    }

    @Singleton
    @Provides
    fun provideExtras(cacheFactory: Cache.Factory): Cache<*, *> {
        return cacheFactory.build(CacheType.EXTRAS)
    }


    @Singleton
    @Provides
    fun provideFragmentLifecycles(): List<FragmentManager.FragmentLifecycleCallbacks> {
        return mutableListOf()
    }
}
