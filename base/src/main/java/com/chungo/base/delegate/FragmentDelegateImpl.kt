package com.chungo.base.delegate

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import com.chungo.base.eventbus.EventBusManager

/**
 * [FragmentDelegate] 默认实现类
 *
 *
 */
open class FragmentDelegateImpl(private var mFragmentManager: FragmentManager?, private var mFragment: Fragment?) : FragmentDelegate {
    protected var iFragment: IFragment? = null

    /**
     * Return true if the fragment is currently added to its activity.
     */
    override val isAdded: Boolean
        get() = mFragment != null && mFragment!!.isAdded


    init {
        iFragment = mFragment as IFragment
    }

    override fun onAttach(context: Context) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (iFragment!!.useEventBus())
        //如果要使用eventbus请将此方法返回true
            EventBusManager.instance.register(mFragment!!)//注册到事件主线
    }

    override fun onCreateView(view: View?, savedInstanceState: Bundle?) {

    }

    override fun onActivityCreate(savedInstanceState: Bundle?) {
        iFragment!!.initData(savedInstanceState)
    }

    override fun onStart() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onStop() {

    }

    override fun onSaveInstanceState(outState: Bundle) {

    }

    override fun onDestroyView() {

    }

    override fun onDestroy() {
        if (iFragment != null && iFragment!!.useEventBus())
        //如果要使用eventbus请将此方法返回true
            EventBusManager.instance.unregister(mFragment!!)//注册到事件主线
        this.mFragmentManager = null
        this.mFragment = null
        this.iFragment = null
    }

    override fun onDetach() {

    }
}
