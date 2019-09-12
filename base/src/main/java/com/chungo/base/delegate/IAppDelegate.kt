package com.chungo.base.delegate

import com.chungo.base.lifecycle.IAppLifecycles
import dagger.android.AndroidInjector

/**
 * @Description dagger2-安卓管理的注入接口
 *
 * @Author huangchangguo
 * @Created  2019/3/27 15:23
 *
 */
interface IAppDelegate : IAppLifecycles {
    fun androidInjector(): AndroidInjector<Any>?
}