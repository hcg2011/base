package com.chungo.base.rxerror

import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Function
import java.util.concurrent.TimeUnit

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