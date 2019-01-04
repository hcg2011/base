package com.chungo.baseapp.activity

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.chungo.base.base.BaseActivity
import com.chungo.base.di.component.AppComponent
import com.chungo.baseapp.R
import com.chungo.basemore.di.component.DaggerUserComponent
import com.chungo.basemore.mvp.contract.UserContract
import com.chungo.basemore.mvp.model.entity.User
import com.chungo.basemore.mvp.presenter.UserPresenter
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_detail.*
import javax.inject.Inject

class DetailActivity : BaseActivity<UserPresenter>(), UserContract.View {
    override fun obtainActivity(): Activity = this
    @Inject
    lateinit var rxPermissions: RxPermissions

    override fun obtainRxPermissions(): RxPermissions {
        return rxPermissions
    }

    override fun startLoadMore() {

    }

    override fun endLoadMore() {

    }

    override fun showMessage(message: String) {

    }

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerUserComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this)
    }

    override fun initView(savedInstanceState: Bundle?): Int = R.layout.activity_detail

    override fun initData(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        val data = intent.extras
        val url = data.get(User.KEY_URL) as String
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
