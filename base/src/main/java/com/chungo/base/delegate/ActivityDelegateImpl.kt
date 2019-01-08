package com.chungo.base.delegate

import android.app.Activity
import android.os.Bundle

import com.chungo.base.integration.EventBusManager
import com.chungo.base.utils.ArmsUtils


/**
 * [ActivityDelegate] 默认实现类
 *
 */
class ActivityDelegateImpl(private var mActivity: Activity?) : ActivityDelegate {
    private var iActivity: IActivity? = null

    init {
        this.iActivity = mActivity as IActivity
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        //如果要使用 EventBus 请将此方法返回 true
        if (iActivity!!.useEventBus()) {
            //注册到事件主线
            EventBusManager.instance.register(mActivity!!)
        }

        //这里提供 AppComponent 对象给 BaseActivity 的子类, 用于 Dagger2 的依赖注入
        iActivity!!.setupActivityComponent(ArmsUtils.obtainAppComponentFromContext(mActivity!!))
    }

    override fun onStart() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onStop() {

    }

    override fun onSaveInstanceState(outState: Bundle?) {

    }

    override fun onDestroy() {
        //如果要使用 EventBus 请将此方法返回 true
        if (iActivity != null && iActivity!!.useEventBus())
            EventBusManager.instance.unregister(mActivity!!)
        this.iActivity = null
        this.mActivity = null
    }
}
