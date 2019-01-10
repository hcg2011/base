package com.chungo.base.utils.log

import android.util.Log

import timber.log.Timber

class ReleaseTree : Timber.DebugTree() {
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return (priority == Log.WARN
                || priority == Log.ERROR
                || priority == Log.ASSERT)
    }

    override fun createStackElementTag(element: StackTraceElement): String? {
        return super.createStackElementTag(element) + ":" + element.lineNumber
    }
}
