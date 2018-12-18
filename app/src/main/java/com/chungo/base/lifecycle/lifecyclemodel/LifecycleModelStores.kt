package com.chungo.base.lifecycle.lifecyclemodel

import android.support.annotation.MainThread
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity


/**
 * Factory methods for [LifecycleModelStore] class.
 *
 *
 * Created by JessYan on 21/11/2017 16:57
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 */
object LifecycleModelStores {

    /**
     * Returns the [LifecycleModelStore] of the given activity.
     *
     * @param activity an activity whose `LifecycleModelStore` is requested
     * @return a `LifecycleModelStore`
     */
    @MainThread
    fun of(activity: FragmentActivity): LifecycleModelStore {
        return HolderFragment.holderFragmentFor(activity).lifecycleModelStore
    }

    /**
     * Returns the [LifecycleModelStore] of the given fragment.
     *
     * @param fragment a fragment whose `LifecycleModelStore` is requested
     * @return a `LifecycleModelStore`
     */
    @MainThread
    fun of(fragment: Fragment): LifecycleModelStore {
        return HolderFragment.holderFragmentFor(fragment).lifecycleModelStore
    }
}
