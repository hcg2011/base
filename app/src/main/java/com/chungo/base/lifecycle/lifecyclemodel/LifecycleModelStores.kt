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
