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

import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Function
import java.util.concurrent.TimeUnit

/**
 * ================================================
 * Created by JessYan on 9/2/16 14:32
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class RetryWithDelay(private val maxRetries: Int, private val retryDelaySecond: Int) : Function<Observable<Throwable>, ObservableSource<*>> {

    val TAG = this.javaClass.simpleName
    private var retryCount: Int = 0

    @Throws(Exception::class)
    override fun apply(@NonNull throwableObservable: Observable<Throwable>): ObservableSource<*> {
        return throwableObservable
                .flatMap(Function<Throwable, ObservableSource<*>> { throwable ->
                    if (++retryCount <= maxRetries) {
                        // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                        Log.d(TAG, "Observable get error, it will try after " + retryDelaySecond
                                + " second, retry count " + retryCount)
                        return@Function Observable.timer(retryDelaySecond.toLong(),
                                TimeUnit.SECONDS)
                    }
                    // Max retries hit. Just pass the error along.
                    Observable.error<Any>(throwable)
                })
    }
}