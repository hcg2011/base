package com.chungo.base.base

import com.chungo.base.di.component.IComponent

interface IApp {
    /**
     * 提供app的component
     */
    var mAppComponent: IComponent?
}
