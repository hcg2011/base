package com.chungo.basemore.di.component

import com.chungo.base.di.component.AppComponent
import com.chungo.base.di.scope.Scopes
import com.chungo.baseapp.activity.DetailActivity
import com.chungo.baseapp.activity.MainActivity
import com.chungo.basemore.di.module.UserModule
import com.chungo.basemore.di.module.UserModuleBinds
import com.chungo.basemore.mvp.contract.UserContract

import dagger.BindsInstance
import dagger.Component

@Scopes.Activity
@Component(
        modules = arrayOf(
                UserModule::class,
                UserModuleBinds::class),
        dependencies = arrayOf(AppComponent::class))
interface UserComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: DetailActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun view(view: UserContract.View): UserComponent.Builder

        fun appComponent(appComponent: AppComponent): UserComponent.Builder

        fun build(): UserComponent
    }
}
