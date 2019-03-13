package com.chungo.baseapp.api

import com.chungo.baseapp.mvp.model.entity.HomeGameBean
import com.chungo.baseapp.mvp.model.entity.User
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface Api {
    companion object {
        const val HEADER_API_VERSION = "Accept: application/vnd.github.v3+json"
        val APP_DOMAIN = "https://api.github.com"
        val RequestSuccess = "0"
        //切换url
        val HEADER_DOMAIN_KEY_BASE = "header_key"
        val HEADER_DOMAIN_KEY = "$HEADER_DOMAIN_KEY_BASE:"
    }

    @Headers(HEADER_API_VERSION)
    @GET("/users")
    fun getUsers(@Query("since") lastIdQueried: Int, @Query("per_page") perPage: Int): Observable<List<User>>

    @Headers(HEADER_API_VERSION)
    @GET("/mgame/h5/home")
    fun obtainHome(@Query("pageIndex") index: Int, @Query("pageSize") pageSize: Int, @Query("sign") sign: String): Observable<HomeGameBean>
}
