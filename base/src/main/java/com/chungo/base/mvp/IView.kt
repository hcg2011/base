package com.chungo.base.mvp

import android.app.Activity
import android.content.Intent
import com.chungo.base.utils.AppUtils


interface IView {

    /**
     * 显示加载
     */
    fun showLoading() {

    }

    /**
     * 隐藏加载
     */
    fun hideLoading() {

    }

    /**
     * 显示信息
     *
     * @param message 消息内容, 不能为 `null`
     */
    fun showMessage(message: String)

    /**
     * 跳转 [Activity]
     *
     * @param intent `intent` 不能为 `null`
     */
    fun launchActivity(intent: Intent) {
        checkNotNull(intent)
        AppUtils.startActivity(intent)
    }

    /**
     * 杀死自己
     */
    fun killMyself() {

    }
}
