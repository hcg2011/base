/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chungo.basemore.mvp.presenter

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v4.app.Fragment
import android.support.v4.app.SupportActivity
import android.support.v7.widget.RecyclerView
import com.chungo.base.di.scope.Scopes
import com.chungo.base.mvp.BasePresenter
import com.chungo.base.rxerrorhandler.core.RxErrorHandler
import com.chungo.base.rxerrorhandler.handler.ErrorHandleSubscriber
import com.chungo.base.rxerrorhandler.handler.RetryWithDelay
import com.chungo.base.utils.PermissionUtil
import com.chungo.base.utils.RxLifecycleUtils
import com.chungo.basemore.mvp.contract.UserContract
import com.chungo.basemore.mvp.model.entity.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


/**
 * ================================================
 * 展示 Presenter 的用法
 *
 * @see [Presenter wiki 官方文档](https://github.com/JessYanCoding/MVPArms/wiki.2.4.4)
 * Created by JessYan on 09/04/2016 10:59
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
@Scopes.Activity
class UserPresenter @Inject
constructor(model: UserContract.Model, rootView: UserContract.View) : BasePresenter<UserContract.Model, UserContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mUsers: MutableList<User>
    @Inject
    lateinit var mAdapter: RecyclerView.Adapter<*>
    private var lastUserId = 1
    private var isFirst = true
    private var preEndIndex: Int = 0

    /**
     * 使用 2017 Google IO 发布的 Architecture Components 中的 Lifecycles 的新特性 (此特性已被加入 Support library)
     * 使 `Presenter` 可以与 [SupportActivity] 和 [Fragment] 的部分生命周期绑定
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    internal fun onCreate() {
        requestUsers(true)//打开 App 时自动加载列表
    }

    fun requestUsers(pullToRefresh: Boolean) {
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(object : PermissionUtil.RequestPermission {
            override fun onRequestPermissionSuccess() {
                //request permission success, do something.
                requestFromModel(pullToRefresh)
            }

            override fun onRequestPermissionFailure(permissions: List<String>) {
                mRootView!!.showMessage("Request permissions failure")
                mRootView!!.hideLoading()//隐藏下拉刷新的进度条
            }

            override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {
                mRootView!!.showMessage("Need to go to the settings")
                mRootView!!.hideLoading()//隐藏下拉刷新的进度条
            }
        }, mRootView.obtainRxPermissions(), this!!.mErrorHandler!!)
    }

    private fun requestFromModel(pullToRefresh: Boolean) {
        if (pullToRefresh)
            lastUserId = 1//下拉刷新默认只请求第一页

        //关于RxCache缓存库的使用请参考 http://www.jianshu.com/p/b58ef6b0624b

        var isEvictCache = pullToRefresh//是否驱逐缓存,为ture即不使用缓存,每次下拉刷新即需要最新数据,则不使用缓存

        if (pullToRefresh && isFirst) {//默认在第一次下拉刷新时使用缓存
            isFirst = false
            isEvictCache = false
        }

        mModel.getUsers(lastUserId, isEvictCache)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe { disposable ->
                    if (pullToRefresh)
                        mRootView!!.showLoading()//显示下拉刷新的进度条
                    else
                        mRootView!!.startLoadMore()//显示上拉加载更多的进度条
                }.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    if (pullToRefresh)
                        mRootView!!.hideLoading()//隐藏下拉刷新的进度条
                    else
                        mRootView!!.endLoadMore()//隐藏上拉加载更多的进度条
                }
                .compose(RxLifecycleUtils.bindToLifecycle(this!!.mRootView!!))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<List<User>>(mErrorHandler!!) {
                    override fun onNext(users: List<User>) {
                        lastUserId = users[users.size - 1].id//记录最后一个id,用于下一次请求
                        if (pullToRefresh) mUsers!!.clear()//如果是下拉刷新则清空列表
                        preEndIndex = mUsers!!.size//更新之前列表总长度,用于确定加载更多的起始位置
                        mUsers!!.addAll(users)
                        if (pullToRefresh)
                            mAdapter!!.notifyDataSetChanged()
                        else
                            mAdapter!!.notifyItemRangeInserted(preEndIndex, users.size)
                    }
                })

        // addDispose(disposables);
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
