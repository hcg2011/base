package com.chungo.base.delegate

import android.app.Activity
import android.os.Bundle
import com.chungo.base.eventbus.EventBusManager


/**
 * [ActivityDelegate] 默认实现类
 *
 */
open class ActivityDelegateImpl(val activity: Activity) : ActivityDelegate {

    override fun onCreate(savedInstanceState: Bundle?) {
        val iActivity = activity as? IActivity
        if (iActivity != null && iActivity.useEventBus()) {//注册到事件主线
            EventBusManager.instance.register(activity)
        }
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
        val iActivity = activity as? IActivity
        if (iActivity != null && iActivity.useEventBus())
            EventBusManager.instance.unregister(iActivity)
    }
}
