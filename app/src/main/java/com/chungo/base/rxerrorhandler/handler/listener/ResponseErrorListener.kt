/**
 * Copyright 2017 JessYan
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chungo.base.rxerrorhandler.handler.listener

import android.content.Context

/**
 * ================================================
 * Created by JessYan on 9/2/2016 13:58
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
interface ResponseErrorListener {
    fun handleResponseError(context: Context, t: Throwable)

    companion object {

        val EMPTY: ResponseErrorListener = object : ResponseErrorListener {
            override fun handleResponseError(context: Context, t: Throwable) {


            }
        }
    }
}
