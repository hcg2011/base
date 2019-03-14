//package com.chungo.baseapp.di.component
//
//import com.chungo.base.di.scope.Scopes
//import com.chungo.base.mvp.IPresenter
//import com.chungo.baseapp.activity.BaseActivity
//import com.chungo.baseapp.di.module.UserModule
//import com.chungo.baseapp.di.module.UserModuleBinds
//import com.chungo.baseapp.mvp.contract.UserContract
//import dagger.BindsInstance
//import dagger.Subcomponent
//import dagger.android.AndroidInjectionModule
//import dagger.android.AndroidInjector
//
///**
// * @Description
// *
// * @Author huangchangguo
// * @Created  2019/3/12 15:53
// *
// */
//@Scopes.Activity
//@Subcomponent(modules = [
//    UserModule::class,
//    UserModuleBinds::class,
//    AndroidInjectionModule::class])
//interface UserSubConponent : AndroidInjector<BaseActivity<IPresenter>> {
//    @Subcomponent.Builder
//    abstract class Builder : AndroidInjector.Builder<BaseActivity<IPresenter>>() {
//        @BindsInstance
//        abstract fun view(view: UserContract.View): Builder
//    }
//
//}