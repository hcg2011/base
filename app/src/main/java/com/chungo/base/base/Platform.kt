package com.chungo.base.base

object Platform {
    val DEPENDENCY_AUTO_LAYOUT: Boolean
    val DEPENDENCY_SUPPORT_DESIGN: Boolean
    val DEPENDENCY_GLIDE: Boolean
    val DEPENDENCY_EVENTBUS: Boolean

    init {
        DEPENDENCY_AUTO_LAYOUT = findClassByClassName("com.zhy.autolayout.AutoLayoutInfo")
        DEPENDENCY_SUPPORT_DESIGN = findClassByClassName("android.support.design.widget.Snackbar")
        DEPENDENCY_GLIDE = findClassByClassName("com.bumptech.glide.Glide")
        DEPENDENCY_EVENTBUS = findClassByClassName("org.greenrobot.eventbus.EventBus")
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
