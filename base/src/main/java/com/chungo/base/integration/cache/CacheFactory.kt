package com.chungo.base.integration.cache

import android.app.Application


/**
 * @Description
 *
 * @Author huangchangguo
 * @Created  2018/12/6 16:43
 *
 */
class CacheFactory constructor(var application: Application) : Cache.Factory {

    override fun build(type: CacheType): Cache<*, *> {
        //若想自定义 LruCache 的 size, 或者不想使用 LruCache, 想使用自己自定义的策略
        //使用 GlobalConfigModule.Builder#cacheFactory() 即可扩展
        return when (type.cacheTypeId) {
            //Activity、Fragment 以及 Extras 使用 IntelligentCache (具有 LruCache 和 可永久存储数据的 Map)
            CacheType.EXTRAS_TYPE_ID,
            CacheType.ACTIVITY_CACHE_TYPE_ID,
            CacheType.FRAGMENT_CACHE_TYPE_ID -> {
                val size = type.calculateCacheSize(application)
                IntelligentCache<Any>(size)
            }
            //其余使用 LruCache (当达到最大容量时可根据 LRU 算法抛弃不合规数据)
            else -> {
                LruCache<Any, Any>(type.calculateCacheSize(application))
            }
        }
    }
}