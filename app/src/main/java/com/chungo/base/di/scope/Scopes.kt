package com.chungo.base.di.scope

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.RUNTIME
import javax.inject.Scope

/**
 * @Description 作用域，用于限定生命周期
 * @Author huangchangguo
 * @Created 2018/11/22 15:16
 */
annotation class Scopes {

    //Activity 的生命周期，对于activity单例
    @Scope
    @Documented
    @Retention(RUNTIME)
    annotation class Activity

    //Fragment 的生命周期，对于fragment单例
    @Scope
    @Documented
    @Retention(RUNTIME)
    annotation class Fragment
}
