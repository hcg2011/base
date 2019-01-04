package com.chungo.base.utils

import com.chungo.base.integration.lifecycle.ActivityLifecycleable
import com.chungo.base.integration.lifecycle.FragmentLifecycleable
import com.chungo.base.integration.lifecycle.Lifecycleable
import com.chungo.base.mvp.IView
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid

import io.reactivex.annotations.NonNull

/**
 * 使用此类操作 RxLifecycle 的特性
 *
 */

class RxLifecycleUtils private constructor() {

    init {
        throw IllegalStateException("you can't instantiate me!")
    }

    companion object {

        /**
         * 绑定 Activity 的指定生命周期
         *
         * @param view
         * @param event
         * @param <T>
         * @return
        </T> */
        fun <T> bindUntilEvent(@NonNull view: IView,
                               event: ActivityEvent): LifecycleTransformer<T> {
            Preconditions.checkNotNull(view, "view == null")
            return if (view is ActivityLifecycleable) {
                bindUntilEvent(view as ActivityLifecycleable, event)
            } else {
                throw IllegalArgumentException("view isn't ActivityLifecycleable")
            }
        }

        /**
         * 绑定 Fragment 的指定生命周期
         *
         * @param view
         * @param event
         * @param <T>
         * @return
        </T> */
        fun <T> bindUntilEvent(@NonNull view: IView,
                               event: FragmentEvent): LifecycleTransformer<T> {
            Preconditions.checkNotNull(view, "view == null")
            return if (view is FragmentLifecycleable) {
                bindUntilEvent(view as FragmentLifecycleable, event)
            } else {
                throw IllegalArgumentException("view isn't FragmentLifecycleable")
            }
        }

        fun <T, R> bindUntilEvent(@NonNull lifecycleable: Lifecycleable<R>,
                                  event: R): LifecycleTransformer<T> {
            Preconditions.checkNotNull(lifecycleable, "lifecycleable == null")
            return RxLifecycle.bindUntilEvent(lifecycleable.provideLifecycleSubject(), event)
        }


        /**
         * 绑定 Activity/Fragment 的生命周期
         *
         * @param view
         * @param <T>
         * @return
        </T> */
        fun <T> bindToLifecycle(@NonNull view: IView): LifecycleTransformer<T> {
            Preconditions.checkNotNull(view, "view == null")
            return if (view is Lifecycleable<*>) {
                bindToLifecycle(view as Lifecycleable<*>)
            } else {
                throw IllegalArgumentException("view isn't Lifecycleable")
            }
        }

        fun <T> bindToLifecycle(@NonNull lifecycleable: Lifecycleable<*>): LifecycleTransformer<T> {
            Preconditions.checkNotNull<Lifecycleable<*>>(lifecycleable, "lifecycleable == null")
            return if (lifecycleable is ActivityLifecycleable) {
                RxLifecycleAndroid.bindActivity(lifecycleable.provideLifecycleSubject())
            } else if (lifecycleable is FragmentLifecycleable) {
                RxLifecycleAndroid.bindFragment(lifecycleable.provideLifecycleSubject())
            } else {
                throw IllegalArgumentException("Lifecycleable not match")
            }
        }
    }

}
