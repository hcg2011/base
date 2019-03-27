package com.chungo.baseapp.activity

import android.content.Context
import com.chungo.base.base.BaseApplication
import com.chungo.base.di.component.IComponent

/**
 * @Description
 *
 * @Author huangchangguo
 * @Created  2019/1/11 18:03
 *
 */
class App : BaseApplication() {
    override var mAppComponent: IComponent? = null
        get() = (mAppDelegate as AppDelegate).mAppComponent

    override fun attachAppDelegate(context: Context) {
        if (mAppDelegate == null)
            this.mAppDelegate = AppDelegate(context)
    }
}