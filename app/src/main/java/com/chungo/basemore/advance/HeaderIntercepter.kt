package com.chungo.basemore.advance

import com.chungo.basemore.mvp.model.api.Api
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * @Description
 * @Author huangchangguo
 * @Created 2018/11/26 17:01
 */
class HeaderIntercepter : Interceptor {
    var isRun = true

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!isRun) chain.proceed(chain.request()) else chain.proceed(setGlobalHeader(chain.request()))
    }

    private fun setGlobalHeader(request: Request): Request {
        val builder = request.newBuilder()
        val headerValues = request.headers(Api.HEADER_DOMAIN_KEY_BASE)
        if (headerValues != null && headerValues.size > 0) {
            builder.removeHeader(Api.HEADER_DOMAIN_KEY_BASE)
            val baseUrl = request.url()
            val headerValue = headerValues[0]
            val newBaseUrl = HttpUrl.parse(headerValue)
            val tid = ""
            val ua = ""
            val params = " "
            builder.addHeader("tid", tid)
                    .addHeader("User-Agent", ua)
                    .addHeader("params", params)
            //重建新的HttpUrl
            val newUrl = baseUrl
                    .newBuilder()
                    .scheme(newBaseUrl!!.scheme())//http
                    .host(newBaseUrl.host())//ad.szprize.cn
                    .port(newBaseUrl.port())//端口
                    .build()
            return builder.url(newUrl).build()
        } else {
            val host = request.url().host()
            return if (host == Api.APP_DOMAIN) {
                builder.build()
            } else
                request
        }
    }
}
