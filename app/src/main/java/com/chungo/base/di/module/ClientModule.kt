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
import com.chungo.base.http.GlobalHttpHandler
import com.chungo.base.rxerrorhandler.core.RxErrorHandler
import com.chungo.base.rxerrorhandler.handler.listener.ResponseErrorListener
import com.chungo.base.utils.DataHelper
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.rx_cache2.internal.RxCache
import io.victoralbertos.jolyglot.GsonSpeaker
import okhttp3.Dispatcher
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * ================================================
 * 提供一些三方库客户端实例的 [Module]
 *
 *
 * Created by JessYan on 2016/3/14.
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
@Module
class ClientModule {
    private val TIME_OUT = 10

    interface RetrofitConfiguration {
        fun configRetrofit(context: Context, builder: Retrofit.Builder)
    }

    interface OkhttpConfiguration {
        fun configOkhttp(context: Context, builder: OkHttpClient.Builder)
    }

    interface RxCacheConfiguration {
        /**
         * 若想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 fastjson
         * 请 `return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());`, 否则请 `return null;`
         *
         * @param context
         * @param builder
         * @return [RxCache]
         */
        fun configRxCache(context: Context, builder: RxCache.Builder): RxCache?
    }

    /**
     * 提供 [Retrofit]
     *
     * @param application
     * @param configuration
     * @param builder
     * @param client
     * @param httpUrl
     * @param gson
     * @return [Retrofit]
     */
    @Singleton
    @Provides
    fun provideRetrofit(application: Application, configuration: RetrofitConfiguration?, builder: Retrofit.Builder, client: OkHttpClient, httpUrl: HttpUrl?, gson: Gson): Retrofit {
        builder.baseUrl(httpUrl)//域名
                .client(client)//设置okhttp

        configuration?.configRetrofit(application, builder)

        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用 Rxjava
                .addConverterFactory(GsonConverterFactory.create(gson))//使用 Gson
        return builder.build()
    }

    /**
     * 提供 [OkHttpClient]
     *
     * @param application
     * @param configuration
     * @param builder
     * @param intercept
     * @param interceptors
     * @param handler
     * @return [OkHttpClient]
     */
    @Singleton
    @Provides
    fun provideClient(application: Application, configuration: OkhttpConfiguration?, intercept: Interceptor?, interceptors: MutableList<Interceptor>?, handler: GlobalHttpHandler?, executorService: ExecutorService): OkHttpClient {
        var builder = OkHttpClient.Builder()
        builder
                .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .addNetworkInterceptor(intercept)

//        if (handler != null)
//            builder.addInterceptor { chain -> chain.proceed(handler.onHttpRequestBefore(chain, chain.request())) }

        if (interceptors != null) {//如果外部提供了interceptor的集合则遍历添加
            for (interceptor in interceptors) {
                builder.addInterceptor(interceptor)
            }
        }

        // 为 OkHttp 设置默认的线程池。
        builder.dispatcher(Dispatcher(executorService))

        configuration?.configOkhttp(application, builder)
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
    }

    @Singleton
    @Provides
    fun provideClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
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
    fun provideRxCache(application: Application, configuration: RxCacheConfiguration?, @Named("RxCacheDirectory") cacheDirectory: File, gson: Gson): RxCache {
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

    /**
     * 提供处理 RxJava 错误的管理器
     *
     * @param application
     * @param listener
     * @return [RxErrorHandler]
     */
    @Singleton
    @Provides
    fun proRxErrorHandler(application: Application, listener: ResponseErrorListener): RxErrorHandler {
        return RxErrorHandler
                .builder()
                .with(application)
                .responseErrorListener(listener)
                .build()
    }

}
