package com.chungo.base.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chungo.base.base.delegate.IFragment
import com.chungo.base.integration.cache.Cache
import com.chungo.base.integration.cache.CacheType
import com.chungo.base.integration.lifecycle.FragmentLifecycleable
import com.chungo.base.mvp.IPresenter
import com.chungo.base.utils.ArmsUtils
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

abstract class BaseFragment<P : IPresenter> : Fragment(), IFragment, FragmentLifecycleable {
    protected val TAG = this.javaClass.simpleName
    private val mLifecycleSubject = BehaviorSubject.create<FragmentEvent>()
    private var mCache: Cache<String, Any>? = null
    protected var mContext: Context? = null
    @Inject
    protected lateinit var mPresenter: P//如果当前页面逻辑简单, Presenter 可以为 null

    @Synchronized
    override fun provideCache(): Cache<String, Any> {
        if (mCache == null) {
            mCache = ArmsUtils.obtainAppComponentFromContext(this.activity!!).cacheFactory().build(CacheType.FRAGMENT_CACHE) as Cache<String, Any>
        }
        return mCache!!
    }

    override fun provideLifecycleSubject(): Subject<FragmentEvent> {
        return mLifecycleSubject
    }

    override fun onAttach(context: Context?) {
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

    /**
     * 是否使用 EventBus
     * Arms 核心库现在并不会依赖某个 EventBus, 要想使用 EventBus, 还请在项目中自行依赖对应的 EventBus
     * 现在支持两种 EventBus, greenrobot 的 EventBus 和畅销书 《Android源码设计模式解析与实战》的作者 何红辉 所作的 AndroidEventBus
     * 确保依赖后, 将此方法返回 true, Arms 会自动检测您依赖的 EventBus, 并自动注册
     * 这种做法可以让使用者有自行选择三方库的权利, 并且还可以减轻 Arms 的体积
     *
     * @return 返回 `true` (默认为使用 `true`), Arms 会自动注册 EventBus
     */
    override fun useEventBus(): Boolean {
        return true
    }

}
