package com.chungo.base.di.scope

import javax.inject.Qualifier

/**
 * @Description
 * @Author huangchangguo
 * @Created 2018/11/22 15:41
 */
annotation class Qualifiers {

    @Qualifier
    @Retention
    annotation class Lifecycle

    @Qualifier
    @Retention
    annotation class RxLifecycle
}
