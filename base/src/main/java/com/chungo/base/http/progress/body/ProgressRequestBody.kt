package com.chungo.base.http.progress.body

import android.os.Handler
import android.os.SystemClock
import com.chungo.base.http.progress.ProgressListener
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.IOException

/**
 * 继承于 [RequestBody], 通过此类获取 Okhttp 上传的二进制数据
 */
class ProgressRequestBody(protected var mHandler: Handler, protected val mDelegate: RequestBody, listeners: List<ProgressListener>, protected var mRefreshTime: Int) : RequestBody() {
    protected val mListeners: Array<ProgressListener>?
    protected val mProgressInfo: ProgressInfo
    private var mBufferedSink: BufferedSink? = null


    init {
        this.mListeners = listeners.toTypedArray()
        this.mProgressInfo = ProgressInfo(System.currentTimeMillis())
    }

    override fun contentType(): MediaType? {
        return mDelegate.contentType()
    }

    override fun contentLength(): Long {
        try {
            return mDelegate.contentLength()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return -1
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        if (mBufferedSink == null) {
            mBufferedSink = Okio.buffer(CountingSink(sink))
        }
        try {
            mDelegate.writeTo(mBufferedSink!!)
            mBufferedSink!!.flush()
        } catch (e: IOException) {
            e.printStackTrace()
            for (i in mListeners!!.indices) {
                mListeners[i].onError(mProgressInfo.id, e)
            }
            throw e
        }

    }

    protected inner class CountingSink(delegate: Sink) : ForwardingSink(delegate) {
        private var totalBytesRead = 0L
        private var lastRefreshTime = 0L  //最后一次刷新的时间
        private var tempSize = 0L

        @Throws(IOException::class)
        override fun write(source: Buffer, byteCount: Long) {
            try {
                super.write(source, byteCount)
            } catch (e: IOException) {
                e.printStackTrace()
                for (i in mListeners!!.indices) {
                    mListeners[i].onError(mProgressInfo.id, e)
                }
                throw e
            }

            if (mProgressInfo.contentLength == 0L) { //避免重复调用 contentLength()
                mProgressInfo.contentLength = contentLength()
            }
            totalBytesRead += byteCount
            tempSize += byteCount
            if (mListeners != null) {
                val curTime = SystemClock.elapsedRealtime()
                if (curTime - lastRefreshTime >= mRefreshTime || totalBytesRead == mProgressInfo.contentLength) {
                    val finalTempSize = tempSize
                    val finalTotalBytesRead = totalBytesRead
                    val finalIntervalTime = curTime - lastRefreshTime
                    for (i in mListeners.indices) {
                        val listener = mListeners[i]
                        mHandler.post {
                            // Runnable 里的代码是通过 Handler 执行在主线程的,外面代码可能执行在其他线程
                            // 所以我必须使用 final ,保证在 Runnable 执行前使用到的变量,在执行时不会被修改
                            mProgressInfo.eachBytes = finalTempSize
                            mProgressInfo.currentbytes = finalTotalBytesRead
                            mProgressInfo.intervalTime = finalIntervalTime
                            mProgressInfo.isFinish = finalTotalBytesRead == mProgressInfo.contentLength
                            listener.onProgress(mProgressInfo)
                        }
                    }
                    lastRefreshTime = curTime
                    tempSize = 0
                }
            }
        }
    }
}
