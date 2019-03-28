package com.chungo.base.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.InflateException
import com.alibaba.android.arouter.launcher.ARouter
import com.chungo.base.delegate.IActivity
import com.chungo.base.integration.cache.Cache
import com.chungo.base.integration.cache.CacheType
import com.chungo.base.lifecycle.rx.IActivityLifecycleable
import com.chungo.base.mvp.IPresenter
import com.chungo.base.utils.AppUtils
import com.trello.rxlifecycle2.android.ActivityEvent
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject


/**
 * 因为 Java 只能单继承, 所以如果要用到需要继承特定 [Activity] 的三方库, 那你就需要自己自定义 [Activity]
 * 继承于这个特定的 [Activity], 然后再按照 [BaseActivity] 的格式, 将代码复制过去, 记住一定要实现[IActivity]
 *
 */
abstract class BaseActivity<P : IPresenter>() : AppCompatActivity(), IActivity, IActivityLifecycleable, HasFragmentInjector, HasSupportFragmentInjector {
    protected val TAG = this.javaClass.simpleName
    protected val mLifecycleSubject = BehaviorSubject.create<ActivityEvent>()
    protected var mCache: Cache<*, *>? = null
    protected var mCompositeDisposable: CompositeDisposable? = null
    @Inject
    lateinit var mPresenter: P
    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var frameworkFragmentInjector: DispatchingAndroidInjector<android.app.Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        try {
            val layoutResID = initView(savedInstanceState)
            if (layoutResID != 0)
                setContentView(layoutResID)
        } catch (e: Exception) {
            if (e is InflateException)
                throw e
            e.printStackTrace()
        }
        initData(savedInstanceState)
    }

    protected fun inject() {
        AndroidInjection.inject(this)
        if (injectRouter())
            ARouter.getInstance().inject(this)
    }

    @Synchronized
    override fun provideCache(): Cache<*, *> {
        if (mCache == null) {
            mCache = AppUtils.obtainAppComponentFromContext(this as Context).cacheFactory().build(CacheType.ACTIVITY_CACHE)
        }
        return mCache!!
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? = supportFragmentInjector
    override fun fragmentInjector(): AndroidInjector<android.app.Fragment>? = frameworkFragmentInjector
    override fun provideLifecycleSubject(): Subject<ActivityEvent> = mLifecycleSubject
    override fun useEventBus(): Boolean = false
    override fun useFragment(): Boolean = false
    protected fun injectRouter(): Boolean = false

    protected fun addDispose(disposable: Disposable) {
        if (mCompositeDisposable == null)
            mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable!!.add(disposable)
    }

    protected fun unDispose() = mCompositeDisposable?.clear()

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
        unDispose()
    }
}
