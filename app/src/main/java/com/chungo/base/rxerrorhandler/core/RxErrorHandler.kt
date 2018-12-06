/**
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chungo.base.rxerrorhandler.core

import android.content.Context
import com.chungo.base.rxerrorhandler.handler.ErrorHandlerFactory
import com.chungo.base.rxerrorhandler.handler.listener.ResponseErrorListener

/**
 * ================================================
 * Created by JessYan on 9/2/2016 13:27
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
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
