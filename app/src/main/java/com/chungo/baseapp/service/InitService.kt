package com.chungo.baseapp.service

import android.content.Context
import android.content.Intent
import com.chungo.base.base.BaseInitService
import timber.log.Timber

/**
 * 在子线程中完成其他初始化
 */
class InitService : BaseInitService("InitService") {
    override fun initApp() {
        Timber.d("InitService!!")
    }

    companion object {
        @JvmStatic
        open fun start(context: Context) {
            val intent = Intent(context, InitService::class.java)
            intent.action = ACTION_INIT
            context.startService(intent)
        }
    }

}
