package com.chungo.basemore.mvp.presenter

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v4.app.Fragment
import android.support.v4.app.SupportActivity
import com.chungo.base.di.scope.Scopes
import com.chungo.base.mvp.BasePresenter
import com.chungo.base.rxerror.ErrorHandleSubscriber
import com.chungo.base.rxerror.RetryWithDelay
import com.chungo.base.rxerror.RxErrorHandler
import com.chungo.base.utils.PermissionUtil
import com.chungo.base.utils.RxLifecycleUtils
import com.chungo.baseapp.adapter.UserAdapter
import com.chungo.basemore.mvp.contract.UserContract
import com.chungo.basemore.mvp.model.entity.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@Scopes.Activity
class UserPresenter
@Inject
constructor(model: UserContract.Model, rootView: UserContract.View) : BasePresenter<UserContract.Model, UserContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mUsers: MutableList<User>
    @Inject
    lateinit var mAdapter: UserAdapter
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
                mRootView.showMessage("Request permissions failure")
                mRootView.hideLoading()//隐藏下拉刷新的进度条
            }

            override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {
                mRootView.showMessage("Need to go to the settings")
                mRootView.hideLoading()//隐藏下拉刷新的进度条
            }
        }, mRootView.obtainRxPermissions(), mErrorHandler)
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
                .doOnSubscribe {
                    if (pullToRefresh)
                        mRootView.showLoading()//显示下拉刷新的进度条
                    else
                        mRootView.startLoadMore()//显示上拉加载更多的进度条
                }.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    if (pullToRefresh)
                        mRootView.hideLoading()//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadMore()//隐藏上拉加载更多的进度条
                }
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<List<User>>(mErrorHandler) {
                    override fun onNext(users: List<User>) {
                        lastUserId = users[users.size - 1].id//记录最后一个id,用于下一次请求
                        if (pullToRefresh) mUsers.clear()//如果是下拉刷新则清空列表
                        preEndIndex = mUsers.size//更新之前列表总长度,用于确定加载更多的起始位置
                        mUsers.addAll(users)
                        if (pullToRefresh)
                            mAdapter.notifyDataSetChanged()
                        else
                            mAdapter.notifyItemRangeInserted(preEndIndex, users.size)
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
