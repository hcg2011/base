package com.chungo.base.di.module

import android.app.Application
import android.content.Context
import com.chungo.base.utils.DataHelper
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.rx_cache2.internal.RxCache
import io.victoralbertos.jolyglot.GsonSpeaker
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Module
class RxCacheModule {
    /**
     * 若想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 fastjson
     * 请 `return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());`, 否则请 `return null;`
     *
     * @param context
     * @param builder
     * @return [RxCache]
     */
    interface RxCacheConfig {
        fun configRxCache(context: Context, builder: RxCache.Builder): RxCache?
    }

    /**
     * 提供 [RxCache]
     *
     * @param application
     * @param configuration
     * @param cacheDirectory cacheDirectory RxCache缓存路径
     * @return [RxCache]
     */
    @Singleton
    @Provides
    fun provideRxCache(application: Application, configuration: RxCacheConfig?, @Named("RxCacheDirectory") cacheDirectory: File, gson: Gson): RxCache {
        val builder = RxCache.Builder()
        var rxCache: RxCache? = null
        if (configuration != null) {
            rxCache = configuration.configRxCache(application, builder)
        }
        return rxCache ?: builder.persistence(cacheDirectory, GsonSpeaker(gson))
    }

    /**
     * 需要单独给 [RxCache] 提供缓存路径
     *
     * @param cacheDir
     * @return [File]
     */
    @Singleton
    @Provides
    @Named("RxCacheDirectory")
    fun provideRxCacheDirectory(cacheDir: File): File {
        val cacheDirectory = File(cacheDir, "RxCache")
        return DataHelper.makeDirs(cacheDirectory)
    }
}
