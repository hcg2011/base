package com.chungo.baseapp.di.module

import com.chungo.baseapp.activity.DetailActivity
import com.chungo.baseapp.di.component.UserSubConponent
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

/**
 * @Description
 *
 * @Author huangchangguo
 * @Created  2019/3/12 15:57
 *
 */
@Module(subcomponents = [
    UserSubConponent::class])
abstract class UserActivityModule {

    @Binds
    @IntoMap
    @ClassKey(DetailActivity::class)
    abstract fun bindActivityInjectorFactory(builder: UserSubConponent.Builder): AndroidInjector.Factory<*>

//
//    @Binds
//    @IntoMap
//    @ClassKey(MainActivity::class)
//    abstract fun bindMainActivityInjectorFactory(builder: UserSubConponent.Builder): AndroidInjector.Factory<*>
}

