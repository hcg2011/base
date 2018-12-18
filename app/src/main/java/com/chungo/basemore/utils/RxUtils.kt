package com.chungo.basemore.utils

import com.chungo.base.mvp.IView
import com.chungo.base.utils.RxLifecycleUtils
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 放置便于使用 RxJava 的一些工具方法
 */
object RxUtils {

    fun <T> applySchedulers(view: IView): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                    .doOnSubscribe {
                        view.showLoading()//显示进度条
                    }
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally {
                        view.hideLoading()//隐藏进度条
                    }.compose(RxLifecycleUtils.bindToLifecycle(view))
        }
    }

    /**
     * 此方法已废弃
     *
     * @param view
     * @param <T>
     * @return
     * @see RxLifecycleUtils 此类可以实现 {@link RxLifecycle} 的所有功能, 使用方法和之前一致
     *
    </T> */
    @Deprecated("Use {@link RxLifecycleUtils#bindToLifecycle(IView)} instead")
    fun <T> bindToLifecycle(view: IView): LifecycleTransformer<T> {
        return RxLifecycleUtils.bindToLifecycle(view)
    }

}
