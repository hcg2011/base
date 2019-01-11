package com.chungo.base.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.InflateException
import butterknife.ButterKnife
import butterknife.Unbinder
import com.chungo.base.delegate.IActivity
import com.chungo.base.integration.cache.Cache
import com.chungo.base.integration.cache.CacheType
import com.chungo.base.lifecycle.rx.IActivityLifecycleable
import com.chungo.base.mvp.IPresenter
import com.chungo.base.utils.AppUtils
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

    @Synchronized
    override fun provideCache(): Cache<*, *> {
        if (mCache == null) {
            mCache = AppUtils.obtainAppComponentFromContext(this as Context).cacheFactory().build(CacheType.ACTIVITY_CACHE)
        }
        return mCache!!
    }

    override fun provideLifecycleSubject(): Subject<ActivityEvent> {
        return mLifecycleSubject
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val layoutResID = initView(savedInstanceState)
            if (layoutResID != 0) {  //如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
                setContentView(layoutResID)
                //绑定到butterknife
                mUnbinder = ButterKnife.bind(this as Activity)
            }
        } catch (e: Exception) {
            if (e is InflateException)
                throw e
            e.printStackTrace()
        }

        initData(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mUnbinder != null && mUnbinder !== Unbinder.EMPTY)
            mUnbinder!!.unbind()
        this.mUnbinder = null
        mPresenter.onDestroy()
    }

    override fun useEventBus(): Boolean = true
    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册[android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks]
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 [BaseFragment] 的Fragment将不起任何作用
     *
     * @return
     */
    override fun useFragment(): Boolean = true
}
