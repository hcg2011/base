package com.chungo.base.utils.log

import timber.log.Timber
import java.util.concurrent.CopyOnWriteArrayList

class TagTree : Timber.DebugTree() {
    private val mTags = CopyOnWriteArrayList<String>()

    override fun createStackElementTag(element: StackTraceElement): String? {
        return super.createStackElementTag(element) + ":" + element.lineNumber
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val newTag = produceTag(tag)
        super.log(priority, newTag, message, t)
    }

    //override this method to produce your own multi tag
    protected fun produceTag(originTag: String?): String {
        val builder = StringBuilder()
        var i = 0
        val lenth = mTags.size
        while (i < lenth) {
            builder.append(mTags[i])
                    .append(if (i != lenth - 1)
                        SEPARATOR
                    else
                        if (originTag == null) "" else SEPARATOR + originTag)
            ++i
        }
        return builder.toString()
    }

    fun removeTag(tag: String): Boolean {
        return mTags.remove(tag)
    }

    fun addTag(vararg tags: String): TagTree {
        for (tag in tags) {
            if (!mTags.contains(tag)) {
                mTags.add(tag)
            }
        }
        return this
    }

    companion object {
        private val SEPARATOR = "-"
    }
}
