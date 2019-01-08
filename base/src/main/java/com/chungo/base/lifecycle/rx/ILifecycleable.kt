package com.chungo.base.lifecycle.rx

import android.app.Activity
import android.support.v4.app.Fragment

import com.chungo.base.utils.RxLifecycleUtils
import com.trello.rxlifecycle2.RxLifecycle

import io.reactivex.subjects.Subject

/**
 * 让 [Activity]/[Fragment] 实现此接口,即可正常使用 [RxLifecycle]
 * 无需再继承 [RxLifecycle] 提供的 Activity/Fragment ,扩展性极强
 *
 * @see RxLifecycleUtils 详细用法请查看此类
 */
interface ILifecycleable<E> {
    fun provideLifecycleSubject(): Subject<E>
}
