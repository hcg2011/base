package com.chungo.base.base

import android.app.IntentService
import android.content.Intent

abstract class BaseInitService(name: String) : IntentService(name) {
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            if (ACTION_INIT == it.action)
                initApp()
        }
    }

    abstract fun initApp()

    companion object {
        const val ACTION_INIT = "initApplication"
    }
}
