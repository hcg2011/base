/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * ================================================
 * 提供一些框架必须的实例的 [Module]
 *
 *
 * Created by JessYan on 8/4/2016.
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
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
