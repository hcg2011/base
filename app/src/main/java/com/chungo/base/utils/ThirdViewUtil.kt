package com.chungo.base.utils

import android.app.Activity
import android.app.Dialog
import android.view.View

import butterknife.ButterKnife
import butterknife.Unbinder


/**
 * ================================================
 * Created by JessYan on 17/03/2016 13:59
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class ThirdViewUtil private constructor() {

    init {
        throw IllegalStateException("you can't instantiate me!")
    }

    companion object {
        private val HAS_AUTO_LAYOUT_META = -1//0 说明 AndroidManifest 里面没有使用 AutoLauout 的 Meta, 即不使用 AutoLayout, 1 为有 Meta, 即需要使用

        fun bindTarget(target: Any, source: Any): Unbinder {
            return if (source is Activity) {
                ButterKnife.bind(target, source)
            } else if (source is View) {
                ButterKnife.bind(target, source)
            } else if (source is Dialog) {
                ButterKnife.bind(target, source)
            } else {
                Unbinder.EMPTY
            }
        }
    }
}
