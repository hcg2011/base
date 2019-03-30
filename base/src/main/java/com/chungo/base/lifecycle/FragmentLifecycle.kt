package com.chungo.base.lifecycle

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View

import com.chungo.base.delegate.FragmentDelegate
import com.chungo.base.delegate.FragmentDelegateImpl
import com.chungo.base.base.IFragment
import com.chungo.base.integration.cache.Cache
import com.chungo.base.integration.cache.IntelligentCache
import com.chungo.base.utils.Preconditions

import javax.inject.Inject
import javax.inject.Singleton

/**
 * [FragmentManager.FragmentLifecycleCallbacks] 默认实现类
 * 通过 [FragmentDelegate] 管理 [Fragment]
 *
 */
@Singleton
class FragmentLifecycle @Inject
constructor() : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        if (f is IFragment) {
            var fragmentDelegate = fetchFragmentDelegate(f)
            if (fragmentDelegate == null || !fragmentDelegate.isAdded) {
                val cache = getCacheFromFragment(f as IFragment)
                fragmentDelegate = FragmentDelegateImpl(fm, f)
                //使用 IntelligentCache.KEY_KEEP 作为 key 的前缀, 可以使储存的数据永久存储在内存中
                //否则存储在 LRU 算法的存储空间中, 前提是 Fragment 使用的是 IntelligentCache (框架默认使用)
                cache.put(IntelligentCache.getKeyOfKeep(FragmentDelegate.FRAGMENT_DELEGATE), fragmentDelegate)
            }
            fragmentDelegate.onAttach(context)
        }
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onCreate(savedInstanceState)
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onCreateView(v, savedInstanceState)
    }

    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onActivityCreate(savedInstanceState)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onStart()
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onResume()
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onPause()
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onStop()
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onSaveInstanceState(outState)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onDestroyView()
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onDestroy()
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onDetach()
    }

    private fun fetchFragmentDelegate(fragment: Fragment): FragmentDelegate? {
        if (fragment is IFragment) {
            val cache = getCacheFromFragment(fragment as IFragment)
            return cache.get(IntelligentCache.getKeyOfKeep(FragmentDelegate.FRAGMENT_DELEGATE)) as FragmentDelegate?
        }
        return null
    }

    private fun getCacheFromFragment(fragment: IFragment): Cache<String, Any> {
        val cache = fragment.provideCache()
        Preconditions.checkNotNull(cache, "%s cannot be null on Fragment", cache::class.java.name)
        return cache
    }

}
