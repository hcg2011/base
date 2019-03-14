//package com.chungo.baseapp.di.module
//
//import com.chungo.base.di.scope.Scopes
//import com.chungo.baseapp.activity.MainActivity
//import com.chungo.baseapp.di.component.MainActivitySubConponent
//import dagger.Module
//import dagger.android.ContributesAndroidInjector
//
///**
// * @Description
// *
// * @Author huangchangguo
// * @Created  2019/3/12 15:57
// *
// */
//@Module(subcomponents = [
//    MainActivitySubConponent::class])
//abstract class MainActivityModule {
//
////    @Binds
////    @IntoMap
////    @ClassKey(MainActivity::class)
////    abstract fun bindMainActivityInjectorFactory(builder: MainActivitySubConponent.Builder): AndroidInjector.Factory<*>
//
//    @Scopes.Activity
//    @ContributesAndroidInjector(modules = [
//        UserModule::class,
//        UserModuleBinds::class])
//    abstract fun UserActivityInjector(): MainActivity
//}
//
