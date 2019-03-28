package com.chungo.baseapp.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.chungo.base.base.BaseActivity
import com.chungo.baseapp.R
import com.chungo.baseapp.mvp.contract.UserContract
import com.chungo.baseapp.mvp.model.entity.User
import com.chungo.baseapp.mvp.presenter.UserPresenter
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_detail.*
import me.jessyan.autosize.AutoSize
import javax.inject.Inject

@Route(path = "DetailActivity", name = "DetailActivity")
class DetailActivity : BaseActivity<UserPresenter>(), UserContract.View {
    override fun obtainActivity(): Activity = this
    @Inject
    lateinit var rxPermissions: RxPermissions

    override fun obtainRxPermissions(): RxPermissions = rxPermissions

    override fun startLoadMore() {

    }

    override fun endLoadMore() {

    }

    override fun showMessage(message: String) {

    }

    override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View? {
        return super.onCreateView(name, context, attrs)
        //由于某些原因, 屏幕旋转后 Fragment 的重建, 会导致框架对 Fragment 的自定义适配参数失去效果
        //所以如果您的 Fragment 允许屏幕旋转, 则请在 onCreateView 手动调用一次 AutoSize.autoConvertDensity()
        //如果您的 Fragment 不允许屏幕旋转, 则可以将下面调用 AutoSize.autoConvertDensity() 的代码删除掉
        AutoSize.autoConvertDensity(this, 360f, true)
    }

    override fun initView(savedInstanceState: Bundle?): Int = R.layout.activity_detail

    override fun initData(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        val data = intent.extras
        val url = data?.get(User.KEY_URL) as String
        fab.setOnClickListener { view ->
            Snackbar.make(view, url, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            val intent = Intent(this, DemoService::class.java)
            startService(intent)
        }
    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail)
//
//    }

}
