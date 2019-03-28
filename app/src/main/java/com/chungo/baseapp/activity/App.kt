package com.chungo.baseapp.activity

import android.content.Context
import com.chungo.base.base.BaseApplication

/**
 * @Description
 *
 * @Author huangchangguo
 * @Created  2019/1/11 18:03
 *
 */
class App : BaseApplication() {

    override fun attachAppDelegate(context: Context) {
        if (mAppDelegate == null)
            this.mAppDelegate = AppDelegate(context)
    }
}