package com.chungo.basemore.api

interface Api {
    companion object {
        const val HEADER_API_VERSION = "Accept: application/vnd.github.v3+json"
        val APP_DOMAIN = "https://api.github.com"
        val RequestSuccess = "0"
        //切换url
        val HEADER_DOMAIN_KEY_BASE = "header_key"
        val HEADER_DOMAIN_KEY = "$HEADER_DOMAIN_KEY_BASE:"
    }
}
