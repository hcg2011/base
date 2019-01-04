package com.chungo.base.lifecycle

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import com.chungo.base.integration.cache.Cache

import com.chungo.base.integration.cache.IntelligentCache
import com.chungo.base.utils.ArmsUtils
import com.squareup.leakcanary.RefWatcher

import timber.log.Timber


/**
 * ================================================
 * 展示 [FragmentManager.FragmentLifecycleCallbacks] 的用法
 *
 *
 * Created by JessYan on 23/08/2018 17:14
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class FragmentLifecycleCallbacksImpl : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        Timber.i(f.toString() + " - onFragmentAttached")
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        Timber.i(f.toString() + " - onFragmentCreated")
        // 在配置变化的时候将这个 Fragment 保存下来,在 Activity 由于配置变化重建时重复利用已经创建的 Fragment。
        // https://developer.android.com/reference/android/app/Fragment.html?hl=zh-cn#setRetainInstance(boolean)
        // 如果在 XML 中使用 <Fragment/> 标签,的方式创建 Fragment 请务必在标签中加上 android:id 或者 android:tag 属性,否则 setRetainInstance(true) 无效
        // 在 Activity 中绑定少量的 Fragment 建议这样做,如果需要绑定较多的 Fragment 不建议设置此参数,如 ViewPager 需要展示较多 Fragment
        f.retainInstance = true
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        Timber.i(f.toString() + " - onFragmentViewCreated")
    }

    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        Timber.i(f.toString() + " - onFragmentActivityCreated")
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        Timber.i(f.toString() + " - onFragmentStarted")
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        Timber.i(f.toString() + " - onFragmentResumed")
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        Timber.i(f.toString() + " - onFragmentPaused")
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        Timber.i(f.toString() + " - onFragmentStopped")
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        Timber.i(f.toString() + " - onFragmentSaveInstanceState")
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        Timber.i(f.toString() + " - onFragmentViewDestroyed")
    }

    @Suppress("UNCHECKED_CAST")
    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        Timber.i(f.toString() + " - onFragmentDestroyed")
        var cache = ArmsUtils
                .obtainAppComponentFromContext(f.activity as Context)
                .extras() as Cache<String, Any>

        (cache.get(IntelligentCache.getKeyOfKeep(RefWatcher::class.java.name)) as RefWatcher)
                .watch(f)
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        Timber.i(f.toString() + " - onFragmentDetached")
    }
}
