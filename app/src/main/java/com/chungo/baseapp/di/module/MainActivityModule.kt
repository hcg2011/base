package com.chungo.baseapp.di.module

import com.chungo.baseapp.activity.MainActivity
import com.chungo.baseapp.di.component.MainActivitySubConponent
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
    MainActivitySubConponent::class])
abstract class MainActivityModule {

    @Binds
    @IntoMap
    @ClassKey(MainActivity::class)
    abstract fun bindMainActivityInjectorFactory(builder: MainActivitySubConponent.Builder): AndroidInjector.Factory<*>
}

