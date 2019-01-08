package com.chungo.base.lifecycle.lifecyclemodel

import android.app.Activity
import android.support.v4.app.Fragment
import com.chungo.base.lifecycle.lifecyclemodel.cache.LruCache


/**
 * 这个类的作用为存储 [LifecycleModel]
 *
 *
 * 此类的实例可以在配置更改或者屏幕旋转导致的 [Activity] 重建的情况下被完好无损的保留下来, 所以重建后的新 [Activity]
 * 持有的实例和重建前持有实例为同一对象
 *
 *
 * 如果当 [Activity] 被真正的 [Activity.finish] 不再重建, [.clear] 会被调用
 * 并且之前存储的所有 [LifecycleModel] 的  [LifecycleModel.onCleared] 方法也会被调用
 *
 *
 * 通过 [LifecycleModelStores] 可向 [Activity] 和 [Fragment] 提供 `LifecycleModelStore`
 *
 */
class LifecycleModelStore {

    private val mCache = LruCache<String, LifecycleModel>(80)

    /**
     * 将 `lifecycleModel` 以 `key` 作为键进行存储
     *
     * @param key
     * @param lifecycleModel 如果这个 {code key} 之前还存储有其他 [LifecycleModel] 对象则返回, 否则返回 `null`
     */
    fun put(key: String, lifecycleModel: LifecycleModel) {
        val oldLifecycleModel = mCache.get(key)
        oldLifecycleModel?.onCleared()
        mCache.put(key, lifecycleModel)
    }

    /**
     * 根据给定的 `key` 获取存储的 [LifecycleModel] 实现类
     *
     * @param key
     * @param <T>
     * @return
    </T> */
    operator fun <T : LifecycleModel> get(key: String): T? {
        return mCache.get(key) as T?
    }

    /**
     * 根据给定的 `key` 移除存储的 [LifecycleModel] 实现类
     *
     * @param key
     * @param <T>
     * @return
    </T> */
    fun <T : LifecycleModel> remove(key: String): T? {
        return mCache.remove(key) as T?
    }

    /**
     * 清理存储的 [LifecycleModel], 并且通知 [LifecycleModel.onCleared]
     */
    fun clear() {
        for (key in mCache.keySet()) {
            mCache.get(key)!!.onCleared()
        }
        mCache.clear()
    }
}
