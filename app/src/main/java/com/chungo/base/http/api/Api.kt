package com.chungo.base.http.api

interface Api {
    companion object {
        val APP_DOMAIN = "https://api.github.com"
        val RequestSuccess = "0"
        //切换url
        val HEADER_DOMAIN_KEY_BASE = "header_key"
        val HEADER_DOMAIN_KEY = "$HEADER_DOMAIN_KEY_BASE:"
    }

}
