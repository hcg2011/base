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
package com.chungo.base.rxerrorhandler.handler

import android.content.Context

import com.chungo.base.rxerrorhandler.handler.listener.ResponseErrorListener

/**
 * ================================================
 * Created by JessYan on 9/2/2016 13:47
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
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
