package com.chungo.base.http

import android.content.Context


/**
 * 用来管理网络请求层,以及数据缓存层,以后可能添加数据库请求层
 *
 */
interface IRepositoryManager {

    val context: Context

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param service
     * @param <T>
     * @return
    </T> */
    fun <T :Any> obtainRetrofitService(service: Class<T>): T

    /**
     * 根据传入的 Class 获取对应的 RxCache service
     *
     * @param cache
     * @param <T>
     * @return
    </T> */
    fun <T:Any> obtainCacheService(cache: Class<T>): T

    /**
     * 清理所有缓存
     */
    fun clearAllCache()

}
