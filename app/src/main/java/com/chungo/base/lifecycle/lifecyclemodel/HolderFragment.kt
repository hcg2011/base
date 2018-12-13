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

import android.app.Activity
import android.os.Bundle
import android.support.annotation.RestrictTo
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks
import android.util.Log

/**
 * @hide 此类为实现 [LifecycleModel] 的核心类
 *
 *
 * Created by JessYan on 21/11/2017 16:57
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class HolderFragment : Fragment() {

    val lifecycleModelStore = LifecycleModelStore()

    init {
        retainInstance = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sHolderFragmentManager.holderFragmentCreated(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleModelStore.clear()
    }

    internal class HolderFragmentManager {
        private val mNotCommittedActivityHolders = mutableMapOf<Activity, HolderFragment>()
        private val mNotCommittedFragmentHolders = mutableMapOf<Fragment, HolderFragment>()

        private val mActivityCallbacks = object : EmptyActivityLifecycleCallbacks() {
            override fun onActivityDestroyed(activity: Activity) {
                val fragment = mNotCommittedActivityHolders.remove(activity)
                if (fragment != null) {
                    Log.e(LOG_TAG, "Failed to save a LifecycleModel for $activity")
                }
            }
        }

        private var mActivityCallbacksIsAdded = false

        private val mParentDestroyedCallback = object : FragmentLifecycleCallbacks() {
            override fun onFragmentDestroyed(fm: FragmentManager, parentFragment: Fragment) {
                super.onFragmentDestroyed(fm, parentFragment)
                val fragment = mNotCommittedFragmentHolders.remove(
                        parentFragment)
                if (fragment != null) {
                    Log.e(LOG_TAG, "Failed to save a LifecycleModel for $parentFragment")
                }
            }
        }

        fun holderFragmentCreated(holderFragment: Fragment) {
            val parentFragment = holderFragment.parentFragment
            if (parentFragment != null) {
                mNotCommittedFragmentHolders.remove(parentFragment)
                parentFragment.fragmentManager!!.unregisterFragmentLifecycleCallbacks(
                        mParentDestroyedCallback)
            } else {
                mNotCommittedActivityHolders.remove(holderFragment.activity as Activity)
            }
        }

        private fun findHolderFragment(manager: FragmentManager): HolderFragment? {
            if (manager.isDestroyed) {
                throw IllegalStateException("Can't access LifecycleModels from onDestroy")
            }

            val fragmentByTag = manager.findFragmentByTag(HOLDER_TAG)
            if (fragmentByTag != null && fragmentByTag !is HolderFragment) {
                throw IllegalStateException("Unexpected " + "fragment instance was returned by HOLDER_TAG")
            }
            return fragmentByTag as HolderFragment?
        }

        private fun createHolderFragment(fragmentManager: FragmentManager): HolderFragment {
            val holder = HolderFragment()
            fragmentManager.beginTransaction().add(holder, HOLDER_TAG).commitAllowingStateLoss()
            return holder
        }

        fun holderFragmentFor(activity: FragmentActivity): HolderFragment {
            val fm = activity.supportFragmentManager
            var holder = findHolderFragment(fm)
            if (holder != null) {
                return holder
            }
            holder = mNotCommittedActivityHolders[activity]
            if (holder != null) {
                return holder
            }

            if (!mActivityCallbacksIsAdded) {
                mActivityCallbacksIsAdded = true
                activity.application.registerActivityLifecycleCallbacks(mActivityCallbacks)
            }
            holder = createHolderFragment(fm)
            mNotCommittedActivityHolders[activity] = holder
            return holder
        }

        fun holderFragmentFor(parentFragment: Fragment): HolderFragment {
            val fm = parentFragment.childFragmentManager
            var holder = findHolderFragment(fm)
            if (holder != null) {
                return holder
            }
            holder = mNotCommittedFragmentHolders[parentFragment]
            if (holder != null) {
                return holder
            }

            parentFragment.fragmentManager!!
                    .registerFragmentLifecycleCallbacks(mParentDestroyedCallback, false)
            holder = createHolderFragment(fm)
            mNotCommittedFragmentHolders[parentFragment] = holder
            return holder
        }
    }

    companion object {
        private val LOG_TAG = "LifecycleModelStores"

        private val sHolderFragmentManager = HolderFragmentManager()

        /**
         * @hide
         */
        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
        val HOLDER_TAG = "state.StateProviderHolderFragment"

        /**
         * @hide
         */
        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
        fun holderFragmentFor(activity: FragmentActivity): HolderFragment {
            return sHolderFragmentManager.holderFragmentFor(activity)
        }

        /**
         * @hide
         */
        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
        fun holderFragmentFor(fragment: Fragment): HolderFragment {
            return sHolderFragmentManager.holderFragmentFor(fragment)
        }
    }
}
