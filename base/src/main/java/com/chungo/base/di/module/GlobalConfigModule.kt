package com.chungo.base.di.module

import android.app.Application
import com.bumptech.glide.Glide
import com.chungo.base.http.imageloader.BaseImageLoaderStrategy
import com.chungo.base.http.imageloader.glide.GlideImageLoaderStrategy
import com.chungo.base.http.interceptor.GlobalHttpHandler
import com.chungo.base.http.log.DefaultFormatPrinter
import com.chungo.base.http.log.FormatPrinter
import com.chungo.base.http.log.RequestInterceptor
import com.chungo.base.integration.cache.Cache
import com.chungo.base.integration.cache.CacheFactory
import com.chungo.base.rxerrorhandler.handler.listener.ResponseErrorListener
import com.chungo.base.utils.DataHelper
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.internal.Util
import java.io.File
import java.util.concurrent.*
import javax.inject.Singleton

@Module
class GlobalConfigModule {
    var mApiUrl: HttpUrl? = null
    var mLoaderStrategy: BaseImageLoaderStrategy<*>? = null
    var mHandler: GlobalHttpHandler? = null
    var mInterceptors: MutableList<Interceptor>? = null
    var mErrorListener: ResponseErrorListener? = null
    var mCacheFile: File? = null

    var mRetrofitConfiguration: NetModule.RetrofitConfig? = null
    var mOkhttpConfiguration: NetModule.OkhttpConfig? = null
    var mGsonConfiguration: NetModule.GsonConfig? = null
    var mMoshiConfiguration: NetModule.MoshiConfig? = null

    var mRxCacheConfiguration: RxCacheModule.RxCacheConfig? = null

    var mPrintHttpLogLevel: RequestInterceptor.Level? = null
    var mFormatPrinter: FormatPrinter? = null
    var mCacheFactory: Cache.Factory? = null
    var mExecutorService: ExecutorService? = null

    @Singleton
    @Provides
    fun provideInterceptors(): MutableList<Interceptor>? {
        return mInterceptors
    }

    @Singleton
    @Provides
    fun provideBaseUrl(): HttpUrl {
        return if (mApiUrl != null)
            mApiUrl!!
        else
            HttpUrl.parse("https://api.github.com/")!!
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
    fun provideRetrofitConfig(): NetModule.RetrofitConfig? {
        return mRetrofitConfiguration
    }

    @Singleton
    @Provides
    fun provideOkhttpConfig(): NetModule.OkhttpConfig? {
        return mOkhttpConfiguration
    }

    @Singleton
    @Provides
    fun provideGsonConfig(): NetModule.GsonConfig? {
        return mGsonConfiguration
    }

    @Singleton
    @Provides
    fun provideMoshiConfig(): NetModule.MoshiConfig? {
        return mMoshiConfiguration
    }

    @Singleton
    @Provides
    fun provideRxCacheConfig(): RxCacheModule.RxCacheConfig? {
        return mRxCacheConfiguration
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
        return if (mCacheFactory != null) mCacheFactory!! else CacheFactory(application)
    }

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
}