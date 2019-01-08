package com.chungo.base.utils

import android.text.TextUtils
import android.util.Log
import com.chungo.base.BuildConfig

/**
 * 日志工具类
 */
class LogUtils private constructor() {

    init {
        throw IllegalStateException("you can't instantiate me!")
    }

    companion object {
        private val DEFAULT_TAG = BuildConfig.APPLICATION_ID
        var isLog = true

        fun debugInfo(tag: String, msg: String) {
            if (!isLog || TextUtils.isEmpty(msg)) return
            Log.d(tag, msg)

        }

        fun debugInfo(msg: String) {
            debugInfo(DEFAULT_TAG, msg)
        }

        fun warnInfo(tag: String, msg: String) {
            if (!isLog || TextUtils.isEmpty(msg)) return
            Log.w(tag, msg)

        }

        fun warnInfo(msg: String) {
            warnInfo(DEFAULT_TAG, msg)
        }

        /**
         * 这里使用自己分节的方式来输出足够长度的 message
         *
         * @param tag 标签
         * @param msg 日志内容
         */
        fun debugLongInfo(tag: String, msg: String) {
            var msg = msg
            if (!isLog || TextUtils.isEmpty(msg)) return
            msg = msg.trim { it <= ' ' }
            var index = 0
            val maxLength = 3500
            var sub: String
            while (index < msg.length) {
                if (msg.length <= index + maxLength) {
                    sub = msg.substring(index)
                } else {
                    sub = msg.substring(index, index + maxLength)
                }

                index += maxLength
                Log.d(tag, sub.trim { it <= ' ' })
            }
        }

        fun debugLongInfo(msg: String) {
            debugLongInfo(DEFAULT_TAG, msg)
        }
    }
}
