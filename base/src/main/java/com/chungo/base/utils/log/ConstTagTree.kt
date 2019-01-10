package com.chungo.base.utils.log

import timber.log.Timber

/**
 * [ConstTagTree] will save the tag forever once you call Timber.tag()
 */
class ConstTagTree : Timber.DebugTree() {
    private var mConstTag: String? = null

    fun setTag(tag: String): ConstTagTree {
        mConstTag = tag
        return this
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        //tag was originally come from Timber.tag(),so we just ignore.
        val element = callingElement()
        var newTag: String? = if (mConstTag != null) mConstTag else getCallingClassName(element)
        newTag += ":" + element.lineNumber
        super.log(priority, newTag, message, t)
    }

    private fun callingElement(): StackTraceElement {
        val stackTrace = Throwable().stackTrace
        if (stackTrace.size <= CALL_STACK_INDEX) {
            throw IllegalStateException(
                    "Synthetic stacktrace didn't have enough elements: are you using proguard?")
        }
        return stackTrace[CALL_STACK_INDEX]
    }

    private fun getCallingClassName(element: StackTraceElement): String? {
        return createStackElementTag(element)
    }

    companion object {

        private val CALL_STACK_INDEX = 6
    }
}
