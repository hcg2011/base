package com.chungo.base.di.component

import android.app.Application
import com.chungo.base.config.ConfigModule
import com.chungo.base.http.IRepositoryManager
import com.chungo.base.integration.cache.Cache
import com.chungo.base.rxerror.RxErrorHandler
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.ExecutorService

/**
 * @Description
 *
 * @Author huangchangguo
 * @Created  2019/3/26 19:51
 *
 */
interface IComponent {
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
     * Json 高级序列化库
     *
     * @return [Moshi]
     */
    fun moshi(): Moshi?

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
}