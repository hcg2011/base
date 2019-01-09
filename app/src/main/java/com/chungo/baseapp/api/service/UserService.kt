package com.chungo.baseapp.api.service


import com.chungo.baseapp.api.Api.Companion.HEADER_API_VERSION
import com.chungo.baseapp.mvp.model.entity.User
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UserService {

    @Headers(HEADER_API_VERSION)
    @GET("/users")
    fun getUsers(@Query("since") lastIdQueried: Int, @Query("per_page") perPage: Int): Observable<List<User>>
}
