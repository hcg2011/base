package com.chungo.base.http.api.service


import com.chungo.basemore.mvp.model.entity.User
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UserService {

    companion object {
        const val HEADER_API_VERSION = "Accept: application/vnd.github.v3+json"
    }

    @Headers(HEADER_API_VERSION)
    @GET("/users")
    fun getUsers(@Query("since") lastIdQueried: Int, @Query("per_page") perPage: Int): Observable<List<User>>


}
