package com.chungo.base.http.ssl

import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate

import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

/**
 * @Description
 * @Author huangchangguo
 * @Created 2018/8/7 14:57
 */

class SSLTrustManager : X509TrustManager {

    private var trustManagers: Array<javax.net.ssl.TrustManager>? = null
    private val _AcceptedIssuers = arrayOf<X509Certificate>()

    @Throws(java.security.cert.CertificateException::class)
    override fun checkClientTrusted(x509Certificates: Array<X509Certificate>, s: String) {
    }

    @Throws(java.security.cert.CertificateException::class)
    override fun checkServerTrusted(x509Certificates: Array<X509Certificate>, s: String) {
    }

    fun isClientTrusted(chain: Array<X509Certificate>): Boolean {
        return true
    }

    fun isServerTrusted(chain: Array<X509Certificate>): Boolean {
        return true
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return _AcceptedIssuers
    }

    fun allowAllSSL(): SSLContext? {
        var context: SSLContext? = null
        if (trustManagers == null)
            trustManagers = arrayOf(this)

        try {
            context = SSLContext.getInstance("TLS")
            context!!.init(null, trustManagers, SecureRandom())
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return context
    }
}

