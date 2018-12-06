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
package com.chungo.base.rxerrorhandler.handler

import com.chungo.base.rxerrorhandler.core.RxErrorHandler

import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable

/**
 * ================================================
 * Created by JessYan on 9/2/2016 14:41
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
abstract class ErrorHandleSubscriber<T>(rxErrorHandler: RxErrorHandler) : Observer<T> {
    private val mHandlerFactory: ErrorHandlerFactory?

    init {
        this.mHandlerFactory = rxErrorHandler.handlerFactory
    }


    override fun onSubscribe(@NonNull d: Disposable) {

    }


    override fun onComplete() {

    }


    override fun onError(@NonNull t: Throwable) {
        t.printStackTrace()
        //如果你某个地方不想使用全局错误处理,则重写 onError(Throwable) 并将 super.onError(e); 删掉
        //如果你不仅想使用全局错误处理,还想加入自己的逻辑,则重写 onError(Throwable) 并在 super.onError(e); 后面加入自己的逻辑
        mHandlerFactory!!.handleError(t)
    }
}

