package com.chungo.base.utils

object DependencyUtils {
    val AUTO_LAYOUT: Boolean
    val SUPPORT_DESIGN: Boolean
    val GLIDE: Boolean
    val EVENTBUS: Boolean

    init {
        AUTO_LAYOUT = findClassByClassName("com.zhy.autolayout.AutoLayoutInfo")
        SUPPORT_DESIGN = findClassByClassName("android.support.design.widget.Snackbar")
        GLIDE = findClassByClassName("com.bumptech.glide.Glide")
        EVENTBUS = findClassByClassName("org.greenrobot.eventbus.EventBus")
    }

    private fun findClassByClassName(className: String): Boolean {
        var hasDependency: Boolean
        try {
            Class.forName(className)
            hasDependency = true
        } catch (e: ClassNotFoundException) {
            hasDependency = false
        }
        return hasDependency
    }
}
