package com.chungo.base.lifecycle.rx

import android.support.v4.app.Fragment

import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.FragmentEvent

/**
 * 让 [Fragment] 实现此接口,即可正常使用 [RxLifecycle]
 */
interface IFragmentLifecycleable : ILifecycleable<FragmentEvent>
