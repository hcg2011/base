package com.chungo.baseapp.activity

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.chungo.base.base.BaseActivity
import com.chungo.baseapp.R
import com.chungo.baseapp.mvp.contract.UserContract
import com.chungo.baseapp.mvp.model.entity.User
import com.chungo.baseapp.mvp.presenter.UserPresenter
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_detail.*
import javax.inject.Inject

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

    override fun initView(savedInstanceState: Bundle?): Int = R.layout.activity_detail

    override fun initData(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        val data = intent.extras
        val url = data?.get(User.KEY_URL) as String
        fab.setOnClickListener { view ->
            Snackbar.make(view, url, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail)
//
//    }

}
