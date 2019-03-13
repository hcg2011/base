package com.chungo.baseapp.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.InflateException
import butterknife.ButterKnife
import butterknife.Unbinder
import com.chungo.base.integration.cache.Cache
import com.chungo.base.integration.cache.CacheType
import com.chungo.base.lifecycle.rx.IActivityLifecycleable
import com.chungo.base.mvp.IPresenter
import com.chungo.baseapp.lifecycle.IActivity
import com.chungo.baseapp.utils.AppUtils
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject


/**
 * 因为 Java 只能单继承, 所以如果要用到需要继承特定 [Activity] 的三方库, 那你就需要自己自定义 [Activity]
 * 继承于这个特定的 [Activity], 然后再按照 [BaseActivity] 的格式, 将代码复制过去, 记住一定要实现[IActivity]
 *
 */
abstract class BaseActivity<P : IPresenter> : AppCompatActivity(), IActivity, IActivityLifecycleable {
    protected val TAG = this.javaClass.simpleName
    private val mLifecycleSubject = BehaviorSubject.create<ActivityEvent>()
    private var mCache: Cache<*, *>? = null
    private var mUnbinder: Unbinder? = null

    @Inject
    lateinit var mPresenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val layoutResID = initView(savedInstanceState)
            if (layoutResID != 0) {  //如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
                setContentView(layoutResID)
                mUnbinder = ButterKnife.bind(this as Activity) //绑定到butterknife
            }
        } catch (e: Exception) {
            if (e is InflateException)
                throw e
            e.printStackTrace()
        }

        initData(savedInstanceState)
    }

    @Synchronized
    override fun provideCache(): Cache<*, *> {
        if (mCache == null) {
            mCache = AppUtils.obtainAppComponentFromContext(this as Context).cacheFactory().build(CacheType.ACTIVITY_CACHE)
        }
        return mCache!!
    }

    override fun provideLifecycleSubject(): Subject<ActivityEvent> = mLifecycleSubject
    //是否用到eventBus，会自动注册
    override fun useEventBus(): Boolean = false

    /**
     * 是否需要绑定fragment，如果在Activity中绑定继承于 [BaseFragment] 的Fragment，会注册[android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks]
     */
    override fun useFragment(): Boolean = false

    override fun onDestroy() {
        super.onDestroy()
        if (mUnbinder != null && mUnbinder !== Unbinder.EMPTY)
            mUnbinder!!.unbind()
        this.mUnbinder = null
        mPresenter.onDestroy()
    }
}
