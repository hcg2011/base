package com.chungo.baseapp.lifecycle

import android.app.Application
import android.content.Context
import butterknife.ButterKnife
import com.chungo.base.integration.cache.Cache
import com.chungo.base.integration.cache.IntelligentCache
import com.chungo.base.lifecycle.IAppLifecycles
import com.chungo.base.utils.AppUtils
import com.chungo.base.utils.log.TagTree
import com.chungo.baseapp.BuildConfig
import com.chungo.baseapp.config.Config
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import timber.log.Timber

class AppLifecyclesImpl : IAppLifecycles {

    override fun attachBaseContext(base: Context) {
        //          MultiDex.install(base);  //这里比 onCreate 先执行,常用于 MultiDex 初始化,插件化框架的初始化
    }

    override fun onCreate(application: Application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            return
        }
        Timber.plant(TagTree().addTag(Config.DEVELOPER))
        if (BuildConfig.DEBUG) {//Timber初始化
            //Timber 是一个日志框架容器,外部使用统一的Api,内部可以动态的切换成任何日志框架(打印策略)进行日志打印
            //并且支持添加多个日志框架(打印策略),做到外部调用一次 Api,内部却可以做到同时使用多个策略
            //比如添加三个策略,一个打印日志,一个将日志保存本地,一个将日志上传服务器
//            Timber.plant(Timber.DebugTree())
//            Timber.plant(object : Timber.DebugTree() {
//                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
//                }
//            })
            ButterKnife.setDebug(true)
        }
        //LeakCanary 内存泄露检查
        //使用 IntelligentCache.KEY_KEEP 作为 key 的前缀, 可以使储存的数据永久存储在内存中
        //否则存储在 LRU 算法的存储空间中, 前提是 extras 使用的是 IntelligentCache (框架默认使用)
        val refWatcher = if (BuildConfig.DEBUG)
            LeakCanary.install(application)
        else
            RefWatcher.DISABLED

        var cache = AppUtils.obtainAppComponentFromContext(application).extras() as? Cache<String, Any>

        cache?.put(IntelligentCache.getKeyOfKeep(RefWatcher::class.java.name), refWatcher)
    }

    override fun onTerminate(application: Application) {

    }
}
