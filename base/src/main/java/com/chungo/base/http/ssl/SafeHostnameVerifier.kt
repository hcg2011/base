package com.chungo.base.http.ssl

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

/**
 * @Description
 * @Author huangchangguo
 * @Created 2018/12/10 19:10
 */
class SafeHostnameVerifier : HostnameVerifier {
    override fun verify(hostname: String, session: SSLSession): Boolean {
        return true
    }
}
