package com.chungo.base.http.progress

import com.chungo.base.http.progress.body.ProgressInfo

interface ProgressListener {
    /**
     * 进度监听
     *
     * @param progressInfo 关于进度的所有信息
     */
    fun onProgress(progressInfo: ProgressInfo)

    /**
     * 错误监听
     *
     * @param id 进度信息的 id
     * @param e 错误
     */
    fun onError(id: Long, e: Exception)
}
