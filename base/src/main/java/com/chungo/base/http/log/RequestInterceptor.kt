package com.chungo.base.http.log

import com.chungo.base.http.interceptor.GlobalHttpHandler
import com.chungo.base.utils.StringUtils
import com.chungo.base.utils.UrlEncoderUtils
import com.chungo.base.utils.ZipHelper
import okhttp3.*
import okio.Buffer
import timber.log.Timber
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestInterceptor @Inject constructor() : Interceptor {
    @Inject
    lateinit var mHandler: GlobalHttpHandler
    @Inject
    lateinit var mPrinter: FormatPrinter
    @Inject
    lateinit var printLevel: Level

    enum class Level {
        NONE,//不打印log
        REQUEST,//只打印请求信息
        RESPONSE,//只打印响应信息
        ALL//所有数据全部打印
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val logRequest = printLevel == Level.ALL || printLevel != Level.NONE && printLevel == Level.REQUEST

        if (logRequest) { //打印请求信息
            if (request.body() != null && isParseable(request.body()!!.contentType())) {
                mPrinter.printJsonRequest(request, parseParams(request))
            } else
                mPrinter.printFileRequest(request)
        }

        val logResponse = printLevel == Level.ALL || printLevel != Level.NONE && printLevel == Level.RESPONSE
        val t1 = if (logResponse) System.nanoTime() else 0
        val originalResponse: Response
        try {
            originalResponse = chain.proceed(request)
        } catch (e: Exception) {
            Timber.w("Http Error: $e")
            throw e
        }

        val t2 = if (logResponse) System.nanoTime() else 0

        val responseBody = originalResponse.body()

        //打印响应结果
        var bodyString: String? = null
        if (responseBody != null && isParseable(responseBody.contentType())) {
            bodyString = printResult(request, originalResponse, logResponse)
        }

        if (logResponse) {
            val segmentList = request.url().encodedPathSegments()
            val header = originalResponse.headers().toString()
            val code = originalResponse.code()
            val isSuccessful = originalResponse.isSuccessful
            val message = originalResponse.message()
            val url = originalResponse.request().url().toString()

            if (responseBody != null && isParseable(responseBody.contentType())) {
                mPrinter.printJsonResponse(TimeUnit.NANOSECONDS.toMillis(t2 - t1), isSuccessful,
                        code, header, responseBody.contentType()!!, bodyString!!, segmentList, message, url)
            } else {
                mPrinter.printFileResponse(TimeUnit.NANOSECONDS.toMillis(t2 - t1),
                        isSuccessful, code, header, segmentList, message, url)
            }

        }

        return originalResponse
        return if (mHandler != null) mHandler.onHttpResultResponse(bodyString!!, chain, originalResponse) else originalResponse

    }

    /**
     * 打印响应结果
     *
     * @param request     [Request]
     * @param response    [Response]
     * @param logResponse 是否打印响应结果
     * @return 解析后的响应结果
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun printResult(request: Request, response: Response, logResponse: Boolean): String? {
        try {
            //读取服务器返回的结果
            val responseBody = response.newBuilder().build().body()
            val source = responseBody!!.source()
            source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer()

            //获取content的压缩类型
            val encoding = response
                    .headers()
                    .get("Content-Encoding")

            val clone = buffer.clone()

            //解析response content
            return parseContent(responseBody, encoding, clone)
        } catch (e: IOException) {
            e.printStackTrace()
            return "{\"error\": \"" + e.message + "\"}"
        }

    }


    /**
     * 解析服务器响应的内容
     *
     * @param responseBody [ResponseBody]
     * @param encoding     编码类型
     * @param clone        克隆后的服务器响应内容
     * @return 解析后的响应结果
     */
    private fun parseContent(responseBody: ResponseBody, encoding: String?, clone: Buffer): String? {
        var charset: Charset? = Charset.forName("UTF-8")
        val contentType = responseBody.contentType()
        if (contentType != null)
            charset = contentType.charset(charset)
        return if (encoding != null && encoding.equals("gzip", ignoreCase = true)) {//content 使用 gzip 压缩
            ZipHelper.decompressForGzip(clone.readByteArray(), convertCharset(charset!!))//解压
        } else if (encoding != null && encoding.equals("zlib", ignoreCase = true)) {//content 使用 zlib 压缩
            ZipHelper.decompressToStringForZlib(clone.readByteArray(), convertCharset(charset!!))//解压
        } else {//content 没有被压缩, 或者使用其他未知压缩方式
            clone.readString(charset!!)
        }
    }

    companion object {
        /**
         * 解析请求服务器的请求参数
         *
         * @param request [Request]
         * @return 解析后的请求信息
         * @throws UnsupportedEncodingException
         */
        @Throws(UnsupportedEncodingException::class)
        fun parseParams(request: Request): String {
            try {
                val body = request.newBuilder().build().body() ?: return ""
                val requestbuffer = Buffer()
                body.writeTo(requestbuffer)
                var charset: Charset? = Charset.forName("UTF-8")
                val contentType = body.contentType()
                if (contentType != null) {
                    charset = contentType.charset(charset)
                }
                var json = requestbuffer.readString(charset!!)
                if (UrlEncoderUtils.hasUrlEncoded(json)) {
                    json = URLDecoder.decode(json, convertCharset(charset))
                }
                return StringUtils.jsonFormat(json)
            } catch (e: IOException) {
                e.printStackTrace()
                return "{\"error\": \"" + e.message + "\"}"
            }

        }

        /**
         * 是否可以解析
         *
         * @param mediaType [MediaType]
         * @return `true` 为可以解析
         */
        fun isParseable(mediaType: MediaType?): Boolean {
            return if (mediaType == null || mediaType.type() == null) false else isText(mediaType) || isPlain(mediaType)
                    || isJson(mediaType) || isForm(mediaType)
                    || isHtml(mediaType) || isXml(mediaType)
        }

        fun isText(mediaType: MediaType?): Boolean {
            return if (mediaType == null || mediaType.type() == null) false else mediaType.type() == "text"
        }

        fun isPlain(mediaType: MediaType?): Boolean {
            return if (mediaType == null || mediaType.subtype() == null) false else mediaType.subtype().toLowerCase().contains("plain")
        }

        fun isJson(mediaType: MediaType?): Boolean {
            return if (mediaType == null || mediaType.subtype() == null) false else mediaType.subtype().toLowerCase().contains("json")
        }

        fun isXml(mediaType: MediaType?): Boolean {
            return if (mediaType == null || mediaType.subtype() == null) false else mediaType.subtype().toLowerCase().contains("xml")
        }

        fun isHtml(mediaType: MediaType?): Boolean {
            return if (mediaType == null || mediaType.subtype() == null) false else mediaType.subtype().toLowerCase().contains("html")
        }

        fun isForm(mediaType: MediaType?): Boolean {
            return if (mediaType == null || mediaType.subtype() == null) false else mediaType.subtype().toLowerCase().contains("x-www-form-urlencoded")
        }

        fun convertCharset(charset: Charset): String {
            val s = charset.toString()
            val i = s.indexOf("[")
            return if (i == -1) s else s.substring(i + 1, s.length - 1)
        }
    }

}
