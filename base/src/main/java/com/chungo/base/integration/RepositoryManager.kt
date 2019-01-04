package com.chungo.base.integration

import android.app.Application
import android.content.Context
import com.chungo.base.integration.cache.Cache
import com.chungo.base.integration.cache.CacheType
import com.chungo.base.utils.Preconditions
import dagger.Lazy
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.rx_cache2.internal.RxCache
import retrofit2.Retrofit
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 用来管理网络请求层,以及数据缓存层,以后可能添加数据库请求层
 * 提供给 [IModel] 层必要的 Api 做数据处理
 *
 */
@Singleton
class RepositoryManager @Inject
constructor() : IRepositoryManager {

    @Inject
    lateinit var mRetrofit: Lazy<Retrofit>
    @Inject
    lateinit var mRxCache: Lazy<RxCache>
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mCachefactory: Cache.Factory
    private var mRetrofitServiceCache: Cache<String, Any>? = null
    private var mCacheServiceCache: Cache<String, Any>? = null

    override val context: Context
        get() = mApplication

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param serviceClass ApiService class
     * @param <T>          ApiService class
     * @return ApiService
    </T> */
    @Synchronized
    override fun <T : Any> obtainRetrofitService(serviceClass: Class<T>): T {
        return createWrapperService(serviceClass)
    }

    /**
     * 根据 https://zhuanlan.zhihu.com/p/40097338 对 Retrofit 进行的优化
     *
     * @param serviceClass ApiService class
     * @param <T>          ApiService class
     * @return ApiService
    </T> */
    private fun <T : Any> createWrapperService(serviceClass: Class<T>): T {
        // 通过二次代理，对 Retrofit 代理方法的调用包进新的 Observable 里在 io 线程执行。
        return Proxy.newProxyInstance(serviceClass.classLoader,
                arrayOf<Class<*>>(serviceClass), InvocationHandler { proxy, method, args ->
            if (method.returnType == Observable::class.java) {
                // 如果方法返回值是 Observable 的话，则包一层再返回
                return@InvocationHandler Observable.defer {
                    val service = getRetrofitService(serviceClass)
                    // 执行真正的 Retrofit 动态代理的方法
                    (getRetrofitMethod(service, method)
                            .invoke(service, *args) as Observable<*>)
                            .subscribeOn(Schedulers.io())
                }.subscribeOn(Schedulers.single())
            }
            // 返回值不是 Observable 的话不处理
            val service = getRetrofitService(serviceClass)
            getRetrofitMethod(service, method).invoke(service, *args)
        }) as T
    }

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param serviceClass ApiService class
     * @param <T>          ApiService class
     * @return ApiService
    </T> */
    private fun <T> getRetrofitService(serviceClass: Class<T>): T {
        if (mRetrofitServiceCache == null) {
            mRetrofitServiceCache = mCachefactory!!.build(CacheType.RETROFIT_SERVICE_CACHE) as Cache<String, Any>
        }
        Preconditions.checkNotNull<Cache<String, Any>>(mRetrofitServiceCache,
                "Cannot return null from a Cache.Factory#build(int) method")
        var retrofitService = mRetrofitServiceCache!!.get(serviceClass.canonicalName)
        if (retrofitService == null) {
            retrofitService = mRetrofit!!.get().create(serviceClass)
            if (retrofitService != null) {
                mRetrofitServiceCache!!.put(serviceClass.canonicalName, retrofitService)
            }
        }
        return (retrofitService as T?)!!
    }

    @Throws(NoSuchMethodException::class)
    private fun <T : Any> getRetrofitMethod(service: T, method: Method): Method {
        return service.javaClass.getMethod(method.name, *method.parameterTypes)
    }

    /**
     * 根据传入的 Class 获取对应的 RxCache service
     *
     * @param cacheClass Cache class
     * @param <T>        Cache class
     * @return Cache
     */
    @Synchronized
    override fun <T : Any> obtainCacheService(cacheClass: Class<T>): T {
        if (mCacheServiceCache == null) {
            mCacheServiceCache = mCachefactory!!.build(CacheType.CACHE_SERVICE_CACHE) as Cache<String, Any>
        }
        Preconditions.checkNotNull<Cache<String, Any>>(mCacheServiceCache,
                "Cannot return null from a Cache.Factory#build(int) method")
        var cacheService = mCacheServiceCache!!.get(cacheClass.canonicalName) as T?
        if (cacheService == null) {
            cacheService = mRxCache!!.get().using(cacheClass)
            if (cacheService != null) {
                mCacheServiceCache!!.put(cacheClass.canonicalName, cacheService)
            }
        }
        return cacheService!!
    }

    /**
     * 清理所有缓存
     */
    override fun clearAllCache() {
        mRxCache!!.get().evictAll().subscribe()
    }
}
