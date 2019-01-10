package com.chungo.base.utils.log

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.Log
import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FileTree : Timber.Tree() {

    private val mHandler: Handler

    private val sDateFormat = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            return SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.UK)
        }
    }

    init {
        ++sNum
        mHandler = FileLogHandler(sLogThread!!.looper)
    }

    fun storeAt(path: String): FileTree {
        mHandler.obtainMessage(MSG_RECORD_FILE_PATH, path).sendToTarget()
        return this
    }

    fun name(name: String): FileTree {
        mHandler.obtainMessage(MSG_RECORD_FILE_NAME, name).sendToTarget()
        return this
    }

    fun release() {
        mHandler.removeCallbacksAndMessages(null)
        if (--sNum == 0)
            doRelease()
    }

    private fun doRelease() {
        sLogThread!!.quit()
    }

    private fun <T> checkNotNull(target: T?, desc: String) {
        if (target == null)
            throw IllegalArgumentException(desc)
    }

    override fun log(priority: Int, tag: String?,
                     message: String, t: Throwable?) {
        mHandler.obtainMessage(MSG_RECORD_FILE_CONTENT,
                LogMsgBean(priority, tag, message, t)).sendToTarget()
    }

    private inner class FileLogHandler(looper: Looper) : Handler(looper) {

        private var mWriter: BufferedWriter? = null
        private var mLogFilePath: String? = null
        private var mLogFileName: String? = null

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_RECORD_FILE_NAME -> mLogFileName = msg.obj as String
                MSG_RECORD_FILE_PATH -> mLogFilePath = msg.obj as String
                MSG_RECORD_FILE_CONTENT -> {
                    preparedWriter()
                    doWrite(msg.obj as LogMsgBean)
                }
            }
        }

        private fun doWrite(msgBean: LogMsgBean) {
            try {
                mWriter!!.write(produceLogInfo(msgBean))
                mWriter!!.newLine()
                mWriter!!.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    mWriter!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    mWriter = null
                }
            }
        }

        private fun preparedWriter() {
            if (mWriter == null) {
                checkNotNull(mLogFilePath, "please set log file path first")
                checkNotNull(mLogFileName, "please set log file name first")
                try {
                    mWriter = BufferedWriter(
                            FileWriter(File(mLogFilePath, mLogFileName!!), true))
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    //2017.08.28 04:41:09.022,VERBOSE/MainActivity,watch the tag hehe
    private fun produceLogInfo(msgBean: LogMsgBean): String {
        return produceLogInfo(msgBean.priority, msgBean.tag, msgBean.message, msgBean.throwable)
    }

    //you can override this method to create your own log content
    protected fun produceLogInfo(priority: Int, tag: String?, message: String, t: Throwable?): String {
        val builder = StringBuilder()
                .append(formatDate(Date(System.currentTimeMillis())))
                .append(SEPARATOR)
                .append(getLogLevel(priority))
                .append("/")
                .append(tag ?: "")
                .append(SEPARATOR)
                .append(message)
        return builder.toString()
    }

    private fun getLogLevel(priority: Int): String {
        var level = "unknown"
        when (priority) {
            Log.VERBOSE -> level = "VERBOSE"
            Log.INFO -> level = "INFO"
            Log.DEBUG -> level = "DEBUG"
            Log.WARN -> level = "WARN"
            Log.ERROR -> level = "ERROR"
            Log.ASSERT -> level = "ASSERT"
            else -> level = "unknown"
        }
        return level
    }

    private fun formatDate(date: Date): String {
        return sDateFormat.get()!!.format(date)
    }

    private class LogMsgBean(internal val priority: Int, internal val tag: String?,
                             internal val message: String, internal val throwable: Throwable?)

    companion object {
        private val MSG_RECORD_FILE_NAME = 0x00
        private val MSG_RECORD_FILE_PATH = 0x01
        private val MSG_RECORD_FILE_CONTENT = 0x03

        private val SEPARATOR = ","
        //all objects share one thread
        private var sLogThread: HandlerThread? = null
        private var sNum = 0

        init {
            sLogThread = HandlerThread("File_Log_Thread")
            sLogThread!!.start()
        }
    }
}
