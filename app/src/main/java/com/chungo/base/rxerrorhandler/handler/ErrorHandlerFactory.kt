package com.chungo.base.rxerrorhandler.handler

import android.content.Context

import com.chungo.base.rxerrorhandler.handler.listener.ResponseErrorListener

class ErrorHandlerFactory(val mContext: Context,val mResponseErrorListener: ResponseErrorListener) {
    val TAG = this.javaClass.simpleName

    /**
     * 处理错误
     * @param throwable
     */
    fun handleError(throwable: Throwable) {
        mResponseErrorListener.handleResponseError(mContext, throwable)
    }
}
