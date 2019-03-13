package com.chungo.baseapp.http

import android.content.Context
import android.text.TextUtils
import com.chungo.base.http.interceptor.GlobalHttpHandler
import com.chungo.base.http.log.RequestInterceptor
import com.chungo.baseapp.api.Api
import com.chungo.baseapp.mvp.model.entity.User
import com.chungo.baseapp.utils.AppUtils
import com.google.gson.reflect.TypeToken
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

class GlobalHttpHandlerImpl(private val context: Context) : GlobalHttpHandler {

    /**
     * 这里可以先客户端一步拿到每一次 Http 请求的结果, 可以先解析成 Json, 再做一些操作, 如检测到 token 过期后
     * 重新请求 token, 并重新执行请求
     *
     * @param httpResult 服务器返回的结果 (已被框架自动转换为字符串)
     * @param chain [Interceptor.Chain]
     * @param response [Response]
     * @return
     */
    override fun onHttpResultResponse(httpResult: String, chain: Interceptor.Chain, response: Response): Response {
        if (!TextUtils.isEmpty(httpResult) && RequestInterceptor.isJson(response.body()!!.contentType())) {
            try {
                val list = AppUtils.obtainAppComponentFromContext(context).gson()!!.fromJson<List<User>>(httpResult, object : TypeToken<List<User>>() {

                }.type)
                val user = list[0]
                Timber.w("Result ------> " + user.login + "    ||   Avatar_url------> " + user.avatarUrl)
            } catch (e: Exception) {
                e.printStackTrace()
                return response
            }

        }

        /* 这里如果发现 token 过期, 可以先请求最新的 token, 然后在拿新的 token 放入 Request 里去重新请求
        注意在这个回调之前已经调用过 proceed(), 所以这里必须自己去建立网络请求, 如使用 Okhttp 使用新的 Request 去请求
        create a new request and modify it accordingly using the new token
        Request newRequest = chain.request().newBuilder().header("token", newToken)
                             .build();

        retry the request

        response.body().close();
        如果使用 Okhttp 将新的请求, 请求成功后, 再将 Okhttp 返回的 Response return 出去即可
        如果不需要返回新的结果, 则直接把参数 response 返回出去即可*/
        return response
    }

    /**
     * 这里可以在请求服务器之前拿到 [Request], 做一些操作比如给 [Request] 统一添加 token 或者 header 以及参数加密等操作
     *
     * @param chain [Interceptor.Chain]
     * @param request [Request]
     * @return
     */
    override fun onHttpRequestBefore(chain: Interceptor.Chain, request: Request): Request {
        /* 如果需要再请求服务器之前做一些操作, 则重新返回一个做过操作的的 Request 如增加 Header, 不做操作则直接返回参数 request
        return chain.request().newBuilder().header("token", tokenId)
                              .build(); */
        return setHeader(request)
    }

    private fun setHeader(request: Request): Request {
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
