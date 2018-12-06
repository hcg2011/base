/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chungo.base.di.module

import android.app.Application
import android.support.v4.app.FragmentManager
import com.chungo.base.di.scope.Qualifiers
import com.chungo.base.integration.ActivityLifecycle
import com.chungo.base.integration.FragmentLifecycle
import com.chungo.base.integration.IRepositoryManager
import com.chungo.base.integration.RepositoryManager
import com.chungo.base.integration.lifecycle.ActivityLifecycleForRxLifecycle
import dagger.Binds
import dagger.Module

/**
 * ================================================
 * 提供一些框架必须的实例的 [Module]
 *
 *
 * Created by JessYan on 8/4/2016.
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
@Module
abstract class AppModuleBinds {

    @Binds
    abstract fun bindRepositoryManager(repositoryManager: RepositoryManager): IRepositoryManager


    //@Qualifiers.Lifecycle
    @Binds
    abstract fun bindActivityLifecycle(activityLifecycle: ActivityLifecycle): Application.ActivityLifecycleCallbacks

    @Qualifiers.RxLifecycle
    @Binds
    abstract fun bindActivityForRxLifecycle(activityRxLifecycle: ActivityLifecycleForRxLifecycle): Application.ActivityLifecycleCallbacks

    @Binds
    abstract fun bindFragmentLifecycle(fragmentLifecycle: FragmentLifecycle): FragmentManager.FragmentLifecycleCallbacks
}
