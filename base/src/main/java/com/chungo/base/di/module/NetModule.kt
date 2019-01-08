package com.chungo.base.di.module

import android.app.Application
import android.content.Context
import com.chungo.base.http.interceptor.GlobalHttpHandler
import com.chungo.base.rxerror.RxErrorHandler
import com.chungo.base.rxerror.listener.ResponseErrorListener
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Dispatcher
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetModule {
    companion object {
        private const val TIME_OUT = 10
    }

    interface RetrofitConfig {
        fun configRetrofit(context: Context, builder: Retrofit.Builder)
    }

    interface OkhttpConfig {
        fun configOkhttp(context: Context, builder: OkHttpClient.Builder)
    }

    interface GsonConfig {
        fun configGson(context: Context, builder: GsonBuilder)
    }

    interface MoshiConfig {
        fun configMoshi(context: Context, builder: Moshi.Builder)
    }


    @Singleton
    @Provides
    fun provideGson(application: Application, config: GsonConfig?): Gson {
        val builder = GsonBuilder()
        config?.configGson(application, builder)
        return builder.create()
    }

    @Singleton
    @Provides
    fun provideMoshi(application: Application, config: MoshiConfig?): Moshi {
        val builder = Moshi.Builder()
        config?.configMoshi(application, builder)
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(application: Application, config: RetrofitConfig?, builder: Retrofit.Builder, client: OkHttpClient, httpUrl: HttpUrl, gson: Gson, moshi: Moshi): Retrofit {
        builder.baseUrl(httpUrl)//域名
                .client(client)//设置okhttp

        config?.configRetrofit(application, builder)
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用 Rxjava
                //.addConverterFactory(GsonConverterFactory.create(gson))//使用 Gson
                .addConverterFactory(MoshiConverterFactory.create(moshi))//使用 moshi
        return builder.build()
    }

    /**
     * 提供 [OkHttpClient]
     *
     * @param application
     * @param configuration
     * @param builder
     * @param intercept
     * @param interceptors
     * @param handler
     * @return [OkHttpClient]
     */
    @Singleton
    @Provides
    fun provideClient(application: Application, config: OkhttpConfig?, intercept: Interceptor?, interceptors: MutableList<Interceptor>?, handler: GlobalHttpHandler?, executorService: ExecutorService): OkHttpClient {
        var builder = OkHttpClient.Builder()
        builder.connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .addNetworkInterceptor(intercept)
                .addInterceptor { chain -> chain.proceed(handler?.onHttpRequestBefore(chain, chain.request())) }
                .dispatcher(Dispatcher(executorService)) // 为 OkHttp 设置默认的线程池
        if (interceptors != null) //如果外部提供了interceptor的集合则遍历添加
            for (interceptor in interceptors)
                builder.addInterceptor(interceptor)

        config?.configOkhttp(application, builder)

        return builder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
    }

    @Singleton
    @Provides
    fun provideClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }

    /**
     * 提供处理 RxJava 错误的管理器
     *
     * @param application
     * @param listener
     * @return [RxErrorHandler]
     */
    @Singleton
    @Provides
    fun proRxErrorHandler(application: Application, listener: ResponseErrorListener): RxErrorHandler {
        return RxErrorHandler
                .builder()
                .with(application)
                .responseErrorListener(listener)
                .build()
    }
}
