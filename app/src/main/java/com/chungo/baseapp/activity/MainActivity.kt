package com.chungo.baseapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import com.chungo.base.base.BaseActivity
import com.chungo.base.di.component.AppComponent
import com.chungo.base.utils.AppUtils
import com.chungo.baseapp.R
import com.chungo.baseapp.adapter.DefaultAdapter
import com.chungo.baseapp.adapter.DefaultAdapter.OnRecyclerViewItemClickListener
import com.chungo.baseapp.adapter.UserAdapter
import com.chungo.baseapp.di.component.DaggerUserComponent
import com.chungo.baseapp.mvp.contract.UserContract
import com.chungo.baseapp.mvp.model.entity.User
import com.chungo.baseapp.mvp.presenter.UserPresenter
import com.paginate.Paginate
import com.tbruyelle.rxpermissions2.RxPermissions
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.internal.CustomAdapt
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity<UserPresenter>(), UserContract.View, SwipeRefreshLayout.OnRefreshListener, CustomAdapt, OnRecyclerViewItemClickListener<User> {

    @BindView(R.id.recyclerView)
    lateinit var mRecyclerView: RecyclerView
    @BindView(R.id.swipeRefreshLayout)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    @Inject
    lateinit var rxPermissions: RxPermissions
    @Inject
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    @Inject
    internal lateinit var mAdapter: UserAdapter

    private var mPaginate: Paginate? = null
    private var isLoadingMore: Boolean = false

    override fun isBaseOnWidth(): Boolean = false
    override fun getSizeInDp(): Float = 0.0f

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerUserComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this)
        //由于某些原因, 屏幕旋转后 Fragment 的重建, 会导致框架对 Fragment 的自定义适配参数失去效果
        //所以如果您的 Fragment 允许屏幕旋转, 则请在 onCreateView 手动调用一次 AutoSize.autoConvertDensity()
        //如果您的 Fragment 不允许屏幕旋转, 则可以将下面调用 AutoSize.autoConvertDensity() 的代码删除掉
        AutoSize.autoConvertDensity(this, 360f, true)
    }

    override fun obtainActivity(): FragmentActivity {
        return this
    }

    override fun obtainRxPermissions(): RxPermissions {
        return rxPermissions
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_user
    }

    override fun initData(savedInstanceState: Bundle?) {
        initRecyclerView()
        mRecyclerView.adapter = mAdapter
        initPaginate()
    }


    override fun onRefresh() {
        mPresenter.requestUsers(true)
    }

    /**
     * 初始化RecyclerView
     */
    private fun initRecyclerView() {
        mSwipeRefreshLayout.setOnRefreshListener(this)
        AppUtils.configRecyclerView(mRecyclerView, mLayoutManager)
        mAdapter.setOnItemClickListener(this)
    }

    override fun onItemClick(view: View, viewType: Int, data: User, position: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(User.KEY_URL, data.avatarUrl)
        startActivity(intent)
    }

    override fun showLoading() {
        Timber.tag(TAG).w("showLoading")
        mSwipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        Timber.tag(TAG).w("hideLoading")
        mSwipeRefreshLayout.isRefreshing = false
    }

    override fun showMessage(message: String) {
        checkNotNull(message)
        AppUtils.snackbarText(message)
    }

    override fun launchActivity(intent: Intent) {
        checkNotNull(intent)
        AppUtils.startActivity(intent)
    }

    override fun killMyself() {
        finish()
    }

    /**
     * 开始加载更多
     */
    override fun startLoadMore() {
        isLoadingMore = true
    }

    /**
     * 结束加载更多
     */
    override fun endLoadMore() {
        isLoadingMore = false
    }

    /**
     * 初始化Paginate,用于加载更多
     */
    private fun initPaginate() {
        if (mPaginate == null) {
            val callbacks = object : Paginate.Callbacks {
                override fun onLoadMore() {
                    mPresenter.requestUsers(false)
                }

                override fun isLoading(): Boolean {
                    return isLoadingMore
                }

                override fun hasLoadedAllItems(): Boolean {
                    return false
                }
            }

            mPaginate = Paginate.with(mRecyclerView, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build()
            mPaginate!!.setHasMoreDataToLoad(false)
        }
    }

    override fun onDestroy() {
        DefaultAdapter.releaseAllHolder(mRecyclerView)//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy()
        this.mPaginate = null
        //this.mPresenter = null
    }
}
