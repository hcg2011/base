package com.chungo.base.lifecycle.rx

import android.app.Activity

import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * 让 [Activity] 实现此接口,即可正常使用 [RxLifecycle]
 *
 */
interface IActivityLifecycleable : ILifecycleable<ActivityEvent>
