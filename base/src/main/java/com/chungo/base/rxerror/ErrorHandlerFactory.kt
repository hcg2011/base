package com.chungo.base.rxerror

import android.content.Context

import com.chungo.base.rxerror.listener.ResponseErrorListener

class ErrorHandlerFactory(val context: Context,val mResponseErrorListener: ResponseErrorListener) {
    val TAG = this.javaClass.simpleName

    /**
     * 处理错误
     * @param throwable
     */
    fun handleError(throwable: Throwable) {
        mResponseErrorListener.handleResponseError(context, throwable)
    }
}
