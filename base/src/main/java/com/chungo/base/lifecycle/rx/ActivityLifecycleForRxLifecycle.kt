package com.chungo.base.lifecycle.rx

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.ActivityEvent
import dagger.Lazy
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 配合 [IActivityLifecycleable] 使用,使 [Activity] 具有 [RxLifecycle] 的特性
 *
 */
@Singleton
class ActivityLifecycleForRxLifecycle : Application.ActivityLifecycleCallbacks {

    @Inject
    constructor()

    @Inject
    lateinit var mFragmentLifecycle: Lazy<FragmentLifecycleForRxLifecycle>

    /**
     * 通过桥梁对象 `BehaviorSubject<ActivityEvent> mLifecycleSubject`
     * 在每个 Activity 的生命周期中发出对应的生命周期事件
     */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is IActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.CREATE)
            if (activity is FragmentActivity) {
                (activity as FragmentActivity).supportFragmentManager.registerFragmentLifecycleCallbacks(mFragmentLifecycle!!.get(), true)
            }
        }
    }

    override fun onActivityStarted(activity: Activity) {
        if (activity is IActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.START)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        if (activity is IActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.RESUME)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (activity is IActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.PAUSE)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        if (activity is IActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.STOP)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        if (activity is IActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.DESTROY)
        }
    }

    /**
     * 从 [BaseActivity] 中获得桥梁对象 `BehaviorSubject<ActivityEvent> mLifecycleSubject`
     *
     * @see [BehaviorSubject 官方中文文档](https://mcxiaoke.gitbooks.io/rxdocs/content/Subject.html)
     */
    private fun obtainSubject(activity: Activity): Subject<ActivityEvent> {
        return (activity as IActivityLifecycleable).provideLifecycleSubject()
    }
}
