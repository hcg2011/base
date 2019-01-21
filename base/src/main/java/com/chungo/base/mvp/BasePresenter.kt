package com.chungo.base.mvp

import android.app.Activity
import android.app.Service
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v4.app.Fragment
import android.view.View
import com.chungo.base.eventbus.EventBusManager
import com.trello.rxlifecycle2.RxLifecycle
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter<V : IView, M : IModel> @JvmOverloads constructor(rootView: V, model: M? = null) : IPresenter, LifecycleObserver {
    protected val TAG = this.javaClass.simpleName
    protected var mCompositeDisposable: CompositeDisposable? = null

    protected var mModel: M? = model
    protected var mRootView: V = rootView

    init {
        onStart()
    }

    /**
     * 使用 2017 Google IO 发布的 Architecture Components 中的 Lifecycles 的新特性 (此特性已被加入 Support library)
     * 使 `Presenter` 可以与 [SupportActivity] 和 [Fragment] 的部分生命周期绑定
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {

    }

    final override fun onStart() {
        //将 LifecycleObserver 注册给 LifecycleOwner 后 @OnLifecycleEvent 才可以正常使用
        if (mRootView != null && mRootView is LifecycleOwner)
            (mRootView as LifecycleOwner).lifecycle.addObserver(this)
        if (useEventBus())
            EventBusManager.instance.register(this)
    }

    /**
     *  [Activity.onDestroy] 会默认调用 [IPresenter.onDestroy]
     */
    override fun onDestroy() {
        if (useEventBus())
            EventBusManager.instance.unregister(this)
        unDispose()//解除订阅
        mModel?.onDestroy()
        mCompositeDisposable = null
    }

    /**
     * 只有当 `mRootView` 不为 null, 并且 `mRootView` 实现了 [LifecycleOwner] 时, 此方法才会被调用
     * 所以当您想在 [Service] 以及一些自定义 [View] 或自定义类中使用 `Presenter` 时
     * 您也将不能继续使用 [OnLifecycleEvent] 绑定生命周期
     *
     * 注意, 如果在这里调用了 [.onDestroy] 方法, 会出现某些地方引用 `mModel` 或 `mRootView` 为 null 的情况
     * 比如在 [RxLifecycle] 终止 [Observable] 时, 在 [io.reactivex.Observable.doFinally] 中却引用了 `mRootView` 做一些释放资源的操作, 此时会空指针
     * 或者如果你声明了多个 @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) 时在其他 @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
     * 中引用了 `mModel` 或 `mRootView` 也可能会出现此情况
     */

    open fun addDispose(disposable: Disposable) {
        if (mCompositeDisposable == null)
            mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable!!.add(disposable)//将所有 Disposable 放入集中处理
    }

    open fun useEventBus(): Boolean = false

    open fun unDispose() = mCompositeDisposable?.clear()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
    }
}
