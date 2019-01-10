package com.chungo.base.base

import android.app.Service
import android.content.Intent
import android.os.IBinder

import com.chungo.base.eventbus.EventBusManager

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseService : Service() {
    protected val TAG = this.javaClass.simpleName
    protected var mCompositeDisposable: CompositeDisposable? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (useEventBus())
            EventBusManager.instance.register(this)
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus())
            EventBusManager.instance.unregister(this)
        unDispose()//解除订阅
        this.mCompositeDisposable = null
    }

    /**
     * 是否使用 EventBus
     */
    open fun useEventBus(): Boolean {
        return true
    }

    protected fun addDispose(disposable: Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable!!.add(disposable)//将所有subscription放入,集中处理
    }

    protected fun unDispose() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable!!.clear()//保证activity结束时取消所有正在执行的订阅
        }
    }

    /**
     * 初始化
     */
    abstract fun init()
}
