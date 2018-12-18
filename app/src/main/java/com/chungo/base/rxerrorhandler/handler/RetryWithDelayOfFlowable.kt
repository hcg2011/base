package com.chungo.base.rxerrorhandler.handler

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Function
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit

class RetryWithDelayOfFlowable(private val maxRetries: Int, private val retryDelaySecond: Int) : Function<Flowable<Throwable>, Publisher<*>> {

    val TAG = this.javaClass.simpleName
    private var retryCount: Int = 0

    @Throws(Exception::class)
    override fun apply(@NonNull throwableFlowable: Flowable<Throwable>): Publisher<*> {
        return throwableFlowable
                .flatMap(Function<Throwable, Publisher<*>> { throwable ->
                    if (++retryCount <= maxRetries) {
                        // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                        Log.d(TAG, "Flowable get error, it will try after " + retryDelaySecond
                                + " second, retry count " + retryCount)
                        return@Function Flowable.timer(retryDelaySecond.toLong(),
                                TimeUnit.SECONDS)
                    }
                    // Max retries hit. Just pass the error along.
                    Flowable.error<Any>(throwable)
                })
    }
}