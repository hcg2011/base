package com.chungo.base.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.InflateException
import android.view.View
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
import me.jessyan.autosize.AutoSize
import javax.inject.Inject


/**
 * 因为 Java 只能单继承, 所以如果要用到需要继承特定 [Activity] 的三方库, 那你就需要自己自定义 [Activity]
 * 继承于这个特定的 [Activity], 然后再按照 [BaseActivity] 的格式, 将代码复制过去, 记住一定要实现[IActivity]
 *
 */
abstract class BaseActivity<P : IPresenter>() : AppCompatActivity(), IActivity, IActivityLifecycleable, HasFragmentInjector, HasSupportFragmentInjector {
    protected val TAG = this.javaClass.simpleName
    private val mLifecycleSubject = BehaviorSubject.create<ActivityEvent>()
    private var mCache: Cache<*, *>? = null
    abstract fun obtainActivity(): Activity
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
            if (layoutResID != 0) {  //如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
                setContentView(layoutResID)
            }
        } catch (e: Exception) {
            if (e is InflateException)
                throw e
            e.printStackTrace()
        }

        initData(savedInstanceState)
    }

    protected fun inject() {
        AndroidInjection.inject(obtainActivity())
//        if (injectRouter())
//            ARouter.getInstance().inject(this)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? = supportFragmentInjector
    override fun fragmentInjector(): AndroidInjector<android.app.Fragment>? = frameworkFragmentInjector

    override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View? {
        return super.onCreateView(name, context, attrs)
        //由于某些原因, 屏幕旋转后 Fragment 的重建, 会导致框架对 Fragment 的自定义适配参数失去效果
        //所以如果您的 Fragment 允许屏幕旋转, 则请在 onCreateView 手动调用一次 AutoSize.autoConvertDensity()
        //如果您的 Fragment 不允许屏幕旋转, 则可以将下面调用 AutoSize.autoConvertDensity() 的代码删除掉
        AutoSize.autoConvertDensity(this, 360f, true)
    }

    @Synchronized
    override fun provideCache(): Cache<*, *> {
        if (mCache == null) {
            mCache = AppUtils.obtainAppComponentFromContext(this as Context).cacheFactory().build(CacheType.ACTIVITY_CACHE)
        }
        return mCache!!
    }

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
