package com.chungo.base.rxerrorhandler.handler.listener

import android.content.Context

interface ResponseErrorListener {
    fun handleResponseError(context: Context, t: Throwable)

    companion object {

        val EMPTY: ResponseErrorListener = object : ResponseErrorListener {
            override fun handleResponseError(context: Context, t: Throwable) {


            }
        }
    }
}
