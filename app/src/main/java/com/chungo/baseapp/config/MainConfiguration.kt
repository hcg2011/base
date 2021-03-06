package com.chungo.baseapp.config

import android.app.Application
import android.content.Context
import android.support.v4.app.FragmentManager
import com.chungo.base.BuildConfig
import com.chungo.base.config.ConfigModule
import com.chungo.base.di.module.GlobalConfigModule
import com.chungo.base.di.module.NetModule
import com.chungo.base.di.module.RxCacheModule
import com.chungo.base.http.log.RequestInterceptor
import com.chungo.base.http.progress.ProgressManager
import com.chungo.base.http.ssl.SSLTrustManager
import com.chungo.base.lifecycle.IAppLifecycles
import com.chungo.base.rxerror.listener.ResponseErrorListenerImpl
import com.chungo.baseapp.api.Api
import com.chungo.baseapp.http.GlobalHttpHandlerImpl
import com.chungo.baseapp.lifecycle.ActivityLifecycleCallbacksImpl
import com.chungo.baseapp.lifecycle.AppLifecyclesImpl
import com.chungo.baseapp.lifecycle.FragmentLifecycleCallbacksImpl
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.rx_cache2.internal.RxCache
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * App 的全局配置信息在此配置, 需要将此实现类声明到 AndroidManifest 中
 * ConfigModule 的实现类可以有无数多个, 在 Application 中只是注册回调, 并不会影响性能 (多个 ConfigModule 在多 Module 环境下尤为受用)
 * 不过要注意 ConfigModule 接口的实现类对象是通过反射生成的, 这里会有些性能损耗
 *
 */
class MainConfiguration : ConfigModule {

    override fun applyOptions(context: Context, builder: GlobalConfigModule) {
        if (!BuildConfig.DEBUG) { //Release 时,让框架不再打印 Http 请求和响应的信息
            builder.apply { mPrintHttpLogLevel = RequestInterceptor.Level.NONE }
        }

        builder.apply { mApiUrl = HttpUrl.parse(Api.APP_DOMAIN) }
                .apply {
                    //自定义缓存策略
//                    mCacheFactory = object : Cache.Factory {
//                        override fun build(type: CacheType): Cache<*, *> {
//                            return when (type.cacheTypeId) {
//                                CacheType.EXTRAS_TYPE_ID ->
//                                    IntelligentCache<Any>(500)
//                                else ->
//                                    LruCache<Any, Any>(200)
//                            }
//                        }
//                    }
//                    //自定义打印格式
//                    mFormatPrinter = object : FormatPrinter {
//                        override fun printJsonRequest(request: Request, bodyString: String) {
//                            Timber.i("printJsonRequest:" + bodyString);
//                        }
//
//                        override fun printFileRequest(request: Request) {
//                            Timber.i("printFileRequest:" + request.url().toString());
//                        }
//
//                        override fun printJsonResponse(chainMs: Long, isSuccessful: Boolean, code: Int, headers: String, contentType: MediaType, bodyString: String, segments: List<String>, message: String, responseUrl: String) {
//                            Timber.i("printJsonResponse:" + bodyString);
//                        }
//
//                        override fun printFileResponse(chainMs: Long, isSuccessful: Boolean, code: Int, headers: String, segments: List<String>, message: String, responseUrl: String) {
//                            Timber.i("printFileResponse:" + responseUrl);
//                        }
//                    }
                    // 这里提供一个全局处理 Http 请求和响应结果的处理类,可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
                    mHandler = GlobalHttpHandlerImpl(context)
                    // 用来处理 rxjava 中发生的所有错误,rxjava 中发生的每个错误都会回调此接口
                    // rxjava必要要使用ErrorHandleSubscriber(默认实现Subscriber的onError方法),此监听才生效
                    mErrorListener = ResponseErrorListenerImpl()

                    mGsonConfiguration = object : NetModule.GsonConfig {  //自定义配置Gson的参数
                        override fun configGson(context: Context, builder: GsonBuilder) {
                            builder.serializeNulls()//支持序列化null的参数
                                    .enableComplexMapKeySerialization()//支持将序列化key为object的map,默认只能序列化key为string的map
                        }
                    }
                    /**
                     * https://github.com/square/moshi
                     * moshi可以通过两种方式解析json，
                     * 1是添加KotlinJsonAdapterFactory()，通过kotlin反射解析，它本身就是一个通用的反射解析类。
                     * 2是通过在bean上增加注解 @JsonClass(generateAdapter = true) ，通过自动生成adapter来解析
                     */
                    mMoshiConfiguration = object : NetModule.MoshiConfig {
                        override fun configMoshi(context: Context, builder: Moshi.Builder) {
                            builder.add(KotlinJsonAdapterFactory())
                        }
                    }

                    mRetrofitConfiguration = object : NetModule.RetrofitConfig {
                        override fun configRetrofit(context: Context, builder: Retrofit.Builder) {
                            //这里可以自己自定义配置Retrofit的参数, 甚至您可以替换框架配置好的 OkHttpClient 对象 (但是不建议这样做, 这样做您将损失框架提供的很多功能)
                            //builder.addConverterFactory(FastJsonConverterFactory.create());//比如使用fastjson替代gson
                        }
                    }

                    mOkhttpConfiguration = object : NetModule.OkhttpConfig {
                        override fun configOkhttp(context: Context, builder: OkHttpClient.Builder) {
                            //这里可以自己自定义配置Okhttp的参数
                            builder.writeTimeout(10, TimeUnit.SECONDS)
                            try {
                                val manager = SSLTrustManager()
                                val factory = manager.allowAllSSL()!!.socketFactory
                                builder.sslSocketFactory(factory, manager) //支持 Https,详情请百度
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            //使用一行代码监听 Retrofit／Okhttp 上传下载进度监听,以及 Glide 加载进度监听 详细使用方法查看 https://github.com/JessYanCoding/ProgressManager
                            ProgressManager.instance.with(builder)
                        }
                    }

                    mRxCacheConfiguration = object : RxCacheModule.RxCacheConfig {
                        override fun configRxCache(context: Context, builder: RxCache.Builder): RxCache? {
                            //这里可以自己自定义配置 RxCache 的参数
                            builder.useExpiredDataIfLoaderNotAvailable(true)
                            // 想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 fastjson, 请 return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());
                            // 否则请 return null;
                            return null
                        }
                    }
                }
    }

    //application 生命周期扩展
    override fun injectAppLifecycle(context: Context, lifecycles: MutableList<IAppLifecycles>) {
        lifecycles.add(AppLifecyclesImpl())
    }

    //activity 生命周期扩展
    override fun injectActivityLifecycle(context: Context, lifecycles: MutableList<Application.ActivityLifecycleCallbacks>) {
        lifecycles.add(ActivityLifecycleCallbacksImpl())
    }

    override fun injectFragmentLifecycle(context: Context, lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>) {
        lifecycles.add(FragmentLifecycleCallbacksImpl())
    }
}
