package com.chungo.baseapp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import com.chungo.base.base.BaseActivity
import com.chungo.base.base.DefaultAdapter
import com.chungo.base.di.component.AppComponent
import com.chungo.base.utils.ArmsUtils
import com.chungo.basemore.di.component.DaggerUserComponent
import com.chungo.basemore.mvp.contract.UserContract
import com.chungo.basemore.mvp.presenter.UserPresenter
import com.paginate.Paginate
import com.tbruyelle.rxpermissions2.RxPermissions
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.internal.CustomAdapt
import timber.log.Timber
import javax.inject.Inject


/**
 * ================================================
 * 展示 View 的用法
 *
 * @see [View wiki 官方文档](https://github.com/JessYanCoding/MVPArms/wiki.2.4.2)
 * Created by JessYan on 09/04/2016 10:59
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class MainActivity : BaseActivity<UserPresenter>(), UserContract.View, SwipeRefreshLayout.OnRefreshListener, CustomAdapt {

    @BindView(R.id.recyclerView)
    lateinit var mRecyclerView: RecyclerView
    @BindView(R.id.swipeRefreshLayout)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    @Inject
    lateinit var rxPermissions: RxPermissions
    @Inject
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    @Inject
    internal lateinit var mAdapter: RecyclerView.Adapter<*>

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
        AutoSize.autoConvertDensity(this, 0.0f, false);
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
        mRecyclerView!!.adapter = mAdapter
        initPaginate()
    }


    override fun onRefresh() {
        mPresenter!!.requestUsers(true)
    }

    /**
     * 初始化RecyclerView
     */
    private fun initRecyclerView() {
        mSwipeRefreshLayout!!.setOnRefreshListener(this)
        ArmsUtils.configRecyclerView(mRecyclerView!!, mLayoutManager)
    }


    override fun showLoading() {
        Timber.tag(TAG).w("showLoading")
        mSwipeRefreshLayout!!.isRefreshing = true
    }

    override fun hideLoading() {
        Timber.tag(TAG).w("hideLoading")
        mSwipeRefreshLayout!!.isRefreshing = false
    }

    override fun showMessage(message: String) {
        checkNotNull(message)
        ArmsUtils.snackbarText(message)
    }

    override fun launchActivity(intent: Intent) {
        checkNotNull(intent)
        ArmsUtils.startActivity(intent)
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
                    mPresenter!!.requestUsers(false)
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
    }
}
