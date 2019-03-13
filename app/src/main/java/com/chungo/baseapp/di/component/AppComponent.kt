package com.chungo.baseapp.di.component

import android.app.Application
import com.chungo.base.config.ConfigModule
import com.chungo.base.di.module.*
import com.chungo.base.http.IRepositoryManager
import com.chungo.base.integration.cache.Cache
import com.chungo.base.rxerror.RxErrorHandler
import com.chungo.base.utils.AppUtils
import com.chungo.baseapp.di.module.AppModuleBinds
import com.chungo.baseapp.di.module.MainActivityModule
import com.chungo.baseapp.di.module.UserActivityModule
import com.chungo.baseapp.lifecycle.AppDelegate
import com.google.gson.Gson
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.ExecutorService
import javax.inject.Singleton

/**
 * 可通过 [AppUtils.obtainAppComponentFromContext] 拿到此接口的实现类
 * 拥有此接口的实现类即可调用对应的方法拿到 Dagger 提供的对应实例
 *
 */
@Singleton
@Component(modules = [
    AppModule::class,
    AppModuleBinds::class,
    NetModule::class,
    RxCacheModule::class,
    InterceptorModuleBinds::class,
    GlobalConfigModule::class,
    AndroidInjectionModule::class,
    //ActivityBindModule::class
    UserActivityModule::class,
    MainActivityModule::class
])
interface AppComponent {

    fun inject(delegate: AppDelegate)

    fun application(): Application

    /**
     * 用于管理网络请求层, 以及数据缓存层
     *
     * @return [IRepositoryManager]
     */
    fun repositoryManager(): IRepositoryManager

    /**
     * RxJava 错误处理管理类
     *
     * @return [RxErrorHandler]
     */
    fun rxErrorHandler(): RxErrorHandler

    /**
     * 网络请求框架
     *
     * @return [OkHttpClient]
     */
    fun okHttpClient(): OkHttpClient

    /**
     * Json 序列化库
     *
     * @return [Gson]
     */
    fun gson(): Gson?

    /**
     * 缓存文件根目录 (RxCache 和 Glide 的缓存都已经作为子文件夹放在这个根目录下), 应该将所有缓存都统一放到这个根目录下
     * 便于管理和清理, 可在 [ConfigModule.applyOptions] 种配置
     *
     * @return [File]
     */
    fun cacheFile(): File

    /**
     * 用来存取一些整个 App 公用的数据, 切勿大量存放大容量数据, 这里的存放的数据和 [Application] 的生命周期一致
     *
     * @return [Cache]
     */
    fun extras(): Cache<*, *>

    /**
     * 用于创建框架所需缓存对象的工厂
     *
     * @return [Cache.Factory]
     */
    fun cacheFactory(): Cache.Factory

    /**
     * 返回一个全局公用的线程池,适用于大多数异步需求。
     * 避免多个线程池创建带来的资源消耗。
     *
     * @return [ExecutorService]
     */
    fun executorService(): ExecutorService

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun globalConfigModule(globalConfigModule: GlobalConfigModule): Builder

        fun build(): AppComponent
    }
}
