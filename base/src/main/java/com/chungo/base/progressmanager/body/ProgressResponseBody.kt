package com.chungo.base.progressmanager.body

import android.os.Handler
import android.os.SystemClock
import com.chungo.base.progressmanager.ProgressListener
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * ================================================
 * 继承于 [ResponseBody], 通过此类获取 Okhttp 下载的二进制数据
 *
 */
class ProgressResponseBody(protected var mHandler: Handler, protected val mDelegate: ResponseBody?, listeners: MutableList<ProgressListener>, protected var mRefreshTime: Int) : ResponseBody() {
    protected val mListeners: Array<ProgressListener>?
    protected val mProgressInfo: ProgressInfo
    private var mBufferedSource: BufferedSource? = null

    init {
        this.mListeners = listeners.toTypedArray()
        this.mProgressInfo = ProgressInfo(System.currentTimeMillis())
    }

    override fun contentType(): MediaType? {
        return mDelegate!!.contentType()
    }

    override fun contentLength(): Long {
        return mDelegate!!.contentLength()
    }

    override fun source(): BufferedSource? {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mDelegate!!.source()))
        }
        return mBufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            private var totalBytesRead = 0L
            private var lastRefreshTime = 0L  //最后一次刷新的时间
            private var tempSize = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                var bytesRead = 0L
                try {
                    bytesRead = super.read(sink, byteCount)
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
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += if (bytesRead.toInt() != -1) bytesRead else 0
                tempSize += if (bytesRead.toInt() != -1) bytesRead else 0
                if (mListeners != null) {
                    val curTime = SystemClock.elapsedRealtime()
                    if (curTime - lastRefreshTime >= mRefreshTime || bytesRead.toInt() == -1 || totalBytesRead == mProgressInfo.contentLength) {
                        val finalBytesRead = bytesRead
                        val finalTempSize = tempSize
                        val finalTotalBytesRead = totalBytesRead
                        val finalIntervalTime = curTime - lastRefreshTime
                        for (i in mListeners.indices) {
                            val listener = mListeners[i]
                            mHandler.post {
                                // Runnable 里的代码是通过 Handler 执行在主线程的,外面代码可能执行在其他线程
                                // 所以我必须使用 final ,保证在 Runnable 执行前使用到的变量,在执行时不会被修改
                                mProgressInfo.eachBytes = if (finalBytesRead.toInt() != -1) finalTempSize else -1
                                mProgressInfo.currentbytes = finalTotalBytesRead
                                mProgressInfo.intervalTime = finalIntervalTime
                                mProgressInfo.isFinish = finalBytesRead.toInt() == -1 && finalTotalBytesRead == mProgressInfo.contentLength
                                listener.onProgress(mProgressInfo)
                            }
                        }
                        lastRefreshTime = curTime
                        tempSize = 0
                    }
                }
                return bytesRead
            }
        }
    }
}
