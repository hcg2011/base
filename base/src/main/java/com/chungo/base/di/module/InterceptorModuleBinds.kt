package com.chungo.base.di.module

import com.chungo.base.http.log.RequestInterceptor
import dagger.Binds
import dagger.Module
import okhttp3.Interceptor

/**
 * @Description 网络劫持module
 *
 * @Author huangchangguo
 * @Created  2018/12/6 17:07
 *
 */
@Module
abstract class InterceptorModuleBinds {

    @Binds
    abstract fun bindInterceptor(interceptor: RequestInterceptor): Interceptor
}