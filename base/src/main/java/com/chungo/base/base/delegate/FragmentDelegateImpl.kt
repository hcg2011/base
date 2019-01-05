package com.chungo.base.base.delegate

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import butterknife.ButterKnife
import butterknife.Unbinder
import com.chungo.base.integration.EventBusManager
import com.chungo.base.utils.ArmsUtils
import timber.log.Timber


/**
 * [FragmentDelegate] 默认实现类
 *
 *
 */
class FragmentDelegateImpl(private var mFragmentManager: FragmentManager?, private var mFragment: Fragment?) : FragmentDelegate {
    private var iFragment: IFragment? = null
    private var mUnbinder: Unbinder? = null

    /**
     * Return true if the fragment is currently added to its activity.
     */
    override val isAdded: Boolean
        get() = mFragment != null && mFragment!!.isAdded


    init {
        this.iFragment = mFragment as IFragment
    }

    override fun onAttach(context: Context) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (iFragment!!.useEventBus())
        //如果要使用eventbus请将此方法返回true
            EventBusManager.instance.register(mFragment!!)//注册到事件主线
        iFragment!!.setupFragmentComponent(ArmsUtils.obtainAppComponentFromContext(mFragment!!.activity!!))
    }

    override fun onCreateView(view: View?, savedInstanceState: Bundle?) {
        //绑定到butterknife
        if (view != null)
            mUnbinder = ButterKnife.bind(mFragment!!, view)
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
        if (mUnbinder != null && mUnbinder !== Unbinder.EMPTY) {
            try {
                mUnbinder!!.unbind()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                //fix Bindings already cleared
                Timber.w("onDestroyView: " + e.message)
            }

        }
    }

    override fun onDestroy() {
        if (iFragment != null && iFragment!!.useEventBus())
        //如果要使用eventbus请将此方法返回true
            EventBusManager.instance.unregister(mFragment!!)//注册到事件主线
        this.mUnbinder = null
        this.mFragmentManager = null
        this.mFragment = null
        this.iFragment = null
    }

    override fun onDetach() {

    }
}
