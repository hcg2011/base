package com.chungo.basemore.mvp.contract

import android.app.Activity
import android.content.Intent
import com.chungo.base.mvp.IModel
import com.chungo.base.mvp.IView
import com.chungo.base.utils.AppUtils
import com.chungo.basemore.mvp.model.entity.User
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable


/**
 * 展示 Contract 的用法
 *
 */
interface UserContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun obtainActivity(): Activity
        //申请权限
        fun obtainRxPermissions(): RxPermissions

        fun startLoadMore()
        fun endLoadMore()
        fun showLoading() {

        }

        fun hideLoading() {

        }

        fun showMessage(message: String)

        fun launchActivity(intent: Intent) {
            checkNotNull(intent)
            AppUtils.startActivity(intent)
        }
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,如是否使用缓存
    interface Model : IModel {
        fun getUsers(lastIdQueried: Int, update: Boolean): Observable<List<User>>
    }
}
