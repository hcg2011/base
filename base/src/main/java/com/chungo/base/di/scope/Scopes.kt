package com.chungo.base.di.scope

import javax.inject.Scope

/**
 * @Description 作用域，用于限定生命周期
 * @Author huangchangguo
 * @Created 2018/11/22 15:16
 */
annotation class Scopes {
    @Scope
    @MustBeDocumented
    @Retention
    annotation class App

    //Activity 的生命周期，对于activity单例
    @Scope
    @MustBeDocumented
    @Retention
    annotation class Activity

    //Fragment 的生命周期，对于fragment单例
    @Scope
    @MustBeDocumented
    @Retention
    annotation class Fragment
}
