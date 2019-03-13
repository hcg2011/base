package com.chungo.baseapp.di.component

import com.chungo.base.di.scope.Scopes
import com.chungo.baseapp.activity.DetailActivity
import com.chungo.baseapp.activity.MainActivity
import com.chungo.baseapp.di.module.UserModule
import com.chungo.baseapp.di.module.UserModuleBinds
import com.chungo.baseapp.mvp.contract.UserContract
import dagger.BindsInstance
import dagger.Component

@Scopes.Activity
@Component(
        modules = [UserModule::class, UserModuleBinds::class],
        dependencies = [AppComponent::class])
interface UserComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: DetailActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun view(view: UserContract.View): Builder

        fun appComponent(appComponent: AppComponent): Builder

        fun build(): UserComponent
    }
}
