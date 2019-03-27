package com.chungo.base.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chungo.base.delegate.IFragment
import com.chungo.base.integration.cache.Cache
import com.chungo.base.integration.cache.CacheType
import com.chungo.base.lifecycle.rx.IFragmentLifecycleable
import com.chungo.base.mvp.IPresenter
import com.chungo.base.utils.AppUtils
import com.trello.rxlifecycle2.android.FragmentEvent
import dagger.android.support.AndroidSupportInjection
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
abstract class BaseFragment<P : IPresenter> : Fragment(), IFragment, IFragmentLifecycleable {
    protected val TAG = this.javaClass.simpleName
    private val mLifecycleSubject = BehaviorSubject.create<FragmentEvent>()
    private var mCache: Cache<String, Any>? = null
    protected var mContext: Context? = null
    @Inject
    lateinit var mPresenter: P

    override fun useEventBus(): Boolean = false
    override fun provideLifecycleSubject(): Subject<FragmentEvent> = mLifecycleSubject
    @Synchronized
    override fun provideCache(): Cache<String, Any> {
        if (mCache == null)
            mCache = AppUtils.obtainAppComponentFromContext(mContext!!).cacheFactory().build(CacheType.FRAGMENT_CACHE) as Cache<String, Any>
        return mCache!!
    }

    protected fun inject() {
        AndroidSupportInjection.inject(this)
//    if (injectRouter())
//        ARouter.getInstance().inject(this)
    }

    protected fun injectRouter(): Boolean {
        return false
    }

    override fun onAttach(context: Context?) {
        inject()
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initView(inflater, container, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()//释放资源
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }
}
