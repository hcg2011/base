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
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.chungo.base.http.BaseUrl
import com.chungo.base.http.GlobalHttpHandler
import com.chungo.base.http.imageloader.BaseImageLoaderStrategy
import com.chungo.base.http.imageloader.glide.GlideImageLoaderStrategy
import com.chungo.base.http.log.DefaultFormatPrinter
import com.chungo.base.http.log.FormatPrinter
import com.chungo.base.http.log.RequestInterceptor
import com.chungo.base.integration.cache.Cache
import com.chungo.base.integration.cache.CacheFactory
import com.chungo.base.rxerrorhandler.handler.listener.ResponseErrorListener
import com.chungo.base.utils.DataHelper
import com.chungo.base.utils.Preconditions
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.internal.Util
import java.io.File
import java.util.*
import java.util.concurrent.*
import javax.inject.Singleton

/**
 * ================================================
 * 框架独创的建造者模式 [Module],可向框架中注入外部配置的自定义参数
 *
 * @see [GlobalConfigModule Wiki 官方文档](https://github.com/JessYanCoding/MVPArms/wiki.3.1)
 * Created by JessYan on 2016/3/14.
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
@Module
class GlobalConfigModule private constructor(builder: Builder) {
    private val mApiUrl: HttpUrl?
    private val mBaseUrl: BaseUrl?
    private val mLoaderStrategy: BaseImageLoaderStrategy<*>?
    private val mHandler: GlobalHttpHandler?
    private val mInterceptors: MutableList<Interceptor>?
    private val mErrorListener: ResponseErrorListener?
    private val mCacheFile: File?
    private val mRetrofitConfiguration: ClientModule.RetrofitConfiguration?
    private val mOkhttpConfiguration: ClientModule.OkhttpConfiguration?
    private val mRxCacheConfiguration: ClientModule.RxCacheConfiguration?
    private val mGsonConfiguration: AppModule.GsonConfiguration?
    private val mPrintHttpLogLevel: RequestInterceptor.Level?
    private val mFormatPrinter: FormatPrinter?
    private val mCacheFactory: Cache.Factory?
    private val mExecutorService: ExecutorService?

    init {
        this.mApiUrl = builder.apiUrl
        this.mBaseUrl = builder.baseUrl
        this.mLoaderStrategy = builder.loaderStrategy
        this.mHandler = builder.handler
        this.mInterceptors = builder.interceptors
        this.mErrorListener = builder.responseErrorListener
        this.mCacheFile = builder.cacheFile
        this.mRetrofitConfiguration = builder.retrofitConfiguration
        this.mOkhttpConfiguration = builder.okhttpConfiguration
        this.mRxCacheConfiguration = builder.rxCacheConfiguration
        this.mGsonConfiguration = builder.gsonConfiguration
        this.mPrintHttpLogLevel = builder.printHttpLogLevel
        this.mFormatPrinter = builder.formatPrinter
        this.mCacheFactory = builder.cacheFactory
        this.mExecutorService = builder.executorService
    }


    @Singleton
    @Provides
    fun provideInterceptors(): MutableList<Interceptor>? {
        return mInterceptors
    }


    /**
     * 提供 BaseUrl,默认使用 <"https://api.github.com/">
     *
     * @return
     */
    @Singleton
    @Provides
    fun provideBaseUrl(): HttpUrl? {
        if (mBaseUrl != null) {
            val httpUrl = mBaseUrl.url()
            if (httpUrl != null) {
                return httpUrl
            }
        }
        return if (mApiUrl != null) mApiUrl else HttpUrl.parse("https://api.github.com/")
    }


    /**
     * 提供图片加载框架,默认使用 [Glide]
     *
     * @return
     */
    @Singleton
    @Provides
    fun provideImageLoaderStrategy(): GlideImageLoaderStrategy {
        return mLoaderStrategy as GlideImageLoaderStrategy
    }


    /**
     * 提供处理 Http 请求和响应结果的处理类
     *
     * @return
     */
    @Singleton
    @Provides
    fun provideGlobalHttpHandler(): GlobalHttpHandler {
        return mHandler!!
    }


    /**
     * 提供缓存文件
     */
    @Singleton
    @Provides
    fun provideCacheFile(application: Application): File {
        return mCacheFile ?: DataHelper.getCacheFile(application)
    }


    /**
     * 提供处理 RxJava 错误的管理器的回调
     *
     * @return
     */
    @Singleton
    @Provides
    fun provideResponseErrorListener(): ResponseErrorListener {
        return mErrorListener ?: ResponseErrorListener.EMPTY
    }


    @Singleton
    @Provides
    fun provideRetrofitConfiguration(): ClientModule.RetrofitConfiguration? {
        return mRetrofitConfiguration
    }

    @Singleton
    @Provides
    fun provideOkhttpConfiguration(): ClientModule.OkhttpConfiguration? {
        return mOkhttpConfiguration
    }

    @Singleton
    @Provides
    fun provideRxCacheConfiguration(): ClientModule.RxCacheConfiguration? {
        return mRxCacheConfiguration
    }

    @Singleton
    @Provides
    fun provideGsonConfiguration(): AppModule.GsonConfiguration? {
        return mGsonConfiguration
    }

    @Singleton
    @Provides
    fun providePrintHttpLogLevel(): RequestInterceptor.Level {
        return mPrintHttpLogLevel ?: RequestInterceptor.Level.ALL
    }

    @Singleton
    @Provides
    fun provideFormatPrinter(): FormatPrinter {
        return mFormatPrinter ?: DefaultFormatPrinter()
    }

    @Singleton
    @Provides
    fun provideCacheFactory(application: Application): Cache.Factory {
        return if (mCacheFactory != null) mCacheFactory else CacheFactory(application)
    }

//    @Singleton
//    @Provides
//    fun provideCacheFactory(application: Application): Cache<*, *> {
//        val size = CacheType.ACTIVITY_CACHE.calculateCacheSize(application)
//        return IntelligentCache<Any>(size)
//    }

    /**
     * 返回一个全局公用的线程池,适用于大多数异步需求。
     * 避免多个线程池创建带来的资源消耗。
     *
     * @return [Executor]
     */
    @Singleton
    @Provides
    fun provideExecutorService(): ExecutorService {
        return mExecutorService ?: ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                SynchronousQueue(), Util.threadFactory("Arms Executor", false))
    }

    companion object {

        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {
        var handler: GlobalHttpHandler? = null
        var apiUrl: HttpUrl? = null
        var baseUrl: BaseUrl? = null
        var loaderStrategy: BaseImageLoaderStrategy<*>? = null
        var interceptors: MutableList<Interceptor>? = null
        var responseErrorListener: ResponseErrorListener? = null
        var cacheFile: File? = null
        var retrofitConfiguration: ClientModule.RetrofitConfiguration? = null
        var okhttpConfiguration: ClientModule.OkhttpConfiguration? = null
        var rxCacheConfiguration: ClientModule.RxCacheConfiguration? = null
        var gsonConfiguration: AppModule.GsonConfiguration? = null
        var printHttpLogLevel: RequestInterceptor.Level? = null
        var formatPrinter: FormatPrinter? = null
        var cacheFactory: Cache.Factory? = null
        var executorService: ExecutorService? = null

        fun baseurl(baseUrl: String): Builder {//基础url
            if (TextUtils.isEmpty(baseUrl)) {
                throw NullPointerException("BaseUrl can not be empty")
            }
            this.apiUrl = HttpUrl.parse(baseUrl)
            return this
        }

        fun baseurl(baseUrl: BaseUrl): Builder {
            this.baseUrl = Preconditions.checkNotNull(baseUrl, BaseUrl::class.java.canonicalName!! + "can not be null.")
            return this
        }

        fun imageLoaderStrategy(loaderStrategy: BaseImageLoaderStrategy<*>): Builder {//用来请求网络图片
            this.loaderStrategy = loaderStrategy
            return this
        }

        fun globalHttpHandler(handler: GlobalHttpHandler?): Builder {//用来处理http响应结果
            this.handler = handler
            return this
        }

        fun addInterceptor(interceptor: Interceptor): Builder {//动态添加任意个interceptor
            if (interceptors == null)
                interceptors = ArrayList()
            this.interceptors!!.add(interceptor)
            return this
        }


        fun responseErrorListener(listener: ResponseErrorListener): Builder {//处理所有RxJava的onError逻辑
            this.responseErrorListener = listener
            return this
        }


        fun cacheFile(cacheFile: File): Builder {
            this.cacheFile = cacheFile
            return this
        }

        fun retrofitConfiguration(retrofitConfiguration: ClientModule.RetrofitConfiguration): Builder {
            this.retrofitConfiguration = retrofitConfiguration
            return this
        }

        fun okhttpConfiguration(okhttpConfiguration: ClientModule.OkhttpConfiguration): Builder {
            this.okhttpConfiguration = okhttpConfiguration
            return this
        }

        fun rxCacheConfiguration(rxCacheConfiguration: ClientModule.RxCacheConfiguration): Builder {
            this.rxCacheConfiguration = rxCacheConfiguration
            return this
        }

        fun gsonConfiguration(gsonConfiguration: AppModule.GsonConfiguration): Builder {
            this.gsonConfiguration = gsonConfiguration
            return this
        }

        fun printHttpLogLevel(printHttpLogLevel: RequestInterceptor.Level): Builder {//是否让框架打印 Http 的请求和响应信息
            this.printHttpLogLevel = Preconditions.checkNotNull(printHttpLogLevel, "The printHttpLogLevel can not be null, use RequestInterceptor.Level.NONE instead.")
            return this
        }

        fun formatPrinter(formatPrinter: FormatPrinter): Builder {
            this.formatPrinter = Preconditions.checkNotNull(formatPrinter, FormatPrinter::class.java.canonicalName!! + "can not be null.")
            return this
        }

        fun cacheFactory(cacheFactory: Cache.Factory): Builder {
            this.cacheFactory = cacheFactory
            return this
        }

        fun executorService(executorService: ExecutorService): Builder {
            this.executorService = executorService
            return this
        }

        fun build(): GlobalConfigModule {
            return GlobalConfigModule(this)
        }


    }
}
