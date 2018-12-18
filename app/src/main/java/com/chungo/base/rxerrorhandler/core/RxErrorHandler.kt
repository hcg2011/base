package com.chungo.base.rxerrorhandler.core

import android.content.Context
import com.chungo.base.rxerrorhandler.handler.ErrorHandlerFactory
import com.chungo.base.rxerrorhandler.handler.listener.ResponseErrorListener

class RxErrorHandler private constructor(builder: Builder) {
    val TAG = this.javaClass.simpleName
    val handlerFactory: ErrorHandlerFactory?

    init {
        this.handlerFactory = builder.errorHandlerFactory
    }

    class Builder{
        private lateinit var context: Context
        private lateinit var mResponseErrorListener: ResponseErrorListener
        internal var errorHandlerFactory: ErrorHandlerFactory? = null

        fun with(context: Context): Builder {
            this.context = context
            return this
        }

        fun responseErrorListener(responseErrorListener: ResponseErrorListener): Builder {
            this.mResponseErrorListener = responseErrorListener
            return this
        }

        fun build(): RxErrorHandler {
            if (context == null)
                throw IllegalStateException("Context is required")

            if (mResponseErrorListener == null)
                throw IllegalStateException("ResponseErrorListener is required")

            this.errorHandlerFactory = ErrorHandlerFactory(context, mResponseErrorListener)

            return RxErrorHandler(this)
        }
    }

    companion object {

        fun builder(): Builder {
            return Builder()
        }
    }


}
