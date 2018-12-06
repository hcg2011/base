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
import android.content.Context
import android.support.v4.app.FragmentManager
import com.chungo.base.di.component.AppComponent
import com.chungo.base.integration.AppManager
import com.chungo.base.integration.cache.Cache
import com.chungo.base.integration.cache.CacheType
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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

    interface GsonConfiguration {
        fun configGson(context: Context, builder: GsonBuilder)
    }

//    @Singleton
//    @Provides
//    fun provideGson(application: Application, configuration: GsonConfiguration): Gson {
//        val builder = GsonBuilder()
//        configuration.configGson(application, builder)
//        return builder.create()
//    }

    @Singleton
    @Provides
    fun provideGson(application: Application): Gson {
        val configuration = object : AppModule.GsonConfiguration {
            //这里可以自己自定义配置Gson的参数
            override fun configGson(context: Context, builder: GsonBuilder) {
                builder.serializeNulls()//支持序列化null的参数
                        .enableComplexMapKeySerialization()//支持将序列化key为object的map,默认只能序列化key为string的map
            }
        }
        val builder = GsonBuilder()
        configuration.configGson(application, builder)
        return builder.create()
    }

    /**
     * 之前 [AppManager] 使用 Dagger 保证单例, 只能使用 [AppComponent.appManager] 访问
     * 现在直接将 AppManager 独立为单例类, 可以直接通过静态方法 [AppManager.getAppManager] 访问, 更加方便
     * 但为了不影响之前使用 [AppComponent.appManager] 获取 [AppManager] 的项目, 所以暂时保留这种访问方式
     *
     * @param application
     * @return
     */
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

//        @Singleton
//        @Provides
//        fun provideExtras(application: Application): IntelligentCache<Any> {
//            val size = CacheType.EXTRAS.calculateCacheSize(application)
//            return IntelligentCache<Any>(size)
//        }


    @Singleton
    @Provides
    fun provideFragmentLifecycles(): List<FragmentManager.FragmentLifecycleCallbacks> {
        return mutableListOf()
    }

}
