package com.chungo.base.di.module

import android.app.Application
import android.text.TextUtils
import com.bumptech.glide.Glide
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

@Module
class GlobalConfigModule private constructor(builder: Builder) {
    private val mApiUrl: HttpUrl?
    private val mLoaderStrategy: BaseImageLoaderStrategy<*>?
    private val mHandler: GlobalHttpHandler?
    private val mInterceptors: MutableList<Interceptor>?
    private val mErrorListener: ResponseErrorListener?
    private val mCacheFile: File?

    private val mRetrofitConfiguration: NetModule.RetrofitConfig?
    private val mOkhttpConfiguration: NetModule.OkhttpConfig?
    private val mGsonConfiguration: NetModule.GsonConfig?

    private val mRxCacheConfiguration: RxCacheModule.RxCacheConfig?

    private val mPrintHttpLogLevel: RequestInterceptor.Level?
    private val mFormatPrinter: FormatPrinter?
    private val mCacheFactory: Cache.Factory?
    private val mExecutorService: ExecutorService?

    init {
        this.mApiUrl = builder.apiUrl
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

    @Singleton
    @Provides
    fun provideBaseUrl(): HttpUrl {
        return if (mApiUrl != null)
            mApiUrl
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
        return if (mCacheFactory != null) mCacheFactory else CacheFactory(application)
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

    companion object {

        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {
        internal var handler: GlobalHttpHandler? = null
        internal var apiUrl: HttpUrl? = null
        internal var loaderStrategy: BaseImageLoaderStrategy<*>? = null
        internal var interceptors: MutableList<Interceptor>? = null
        internal var responseErrorListener: ResponseErrorListener? = null
        internal var cacheFile: File? = null
        internal var retrofitConfiguration: NetModule.RetrofitConfig? = null
        internal var okhttpConfiguration: NetModule.OkhttpConfig? = null
        internal var rxCacheConfiguration: RxCacheModule.RxCacheConfig? = null
        internal var gsonConfiguration: NetModule.GsonConfig? = null
        internal var printHttpLogLevel: RequestInterceptor.Level? = null
        internal var formatPrinter: FormatPrinter? = null
        internal var cacheFactory: Cache.Factory? = null
        internal var executorService: ExecutorService? = null

        fun baseurl(baseUrl: String): Builder {
            if (TextUtils.isEmpty(baseUrl)) {
                throw NullPointerException("BaseUrl can not be empty")
            }
            this.apiUrl = HttpUrl.parse(baseUrl)
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

        fun retrofitConfiguration(retrofitConfiguration: NetModule.RetrofitConfig): Builder {
            this.retrofitConfiguration = retrofitConfiguration
            return this
        }

        fun okhttpConfiguration(okhttpConfiguration: NetModule.OkhttpConfig): Builder {
            this.okhttpConfiguration = okhttpConfiguration
            return this
        }

        fun rxCacheConfiguration(rxCacheConfiguration: RxCacheModule.RxCacheConfig): Builder {
            this.rxCacheConfiguration = rxCacheConfiguration
            return this
        }

        fun gsonConfiguration(gsonConfiguration: NetModule.GsonConfig): Builder {
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
