package com.chungo.baseapp.mvp.model.entity

import com.squareup.moshi.Json

//@JsonClass(generateAdapter = true)
class User(val id: Int,
           val login: String,
           @Json(name = "avatar_url") val avatarUrls: String) {

    companion object {
        const val KEY_URL = "key_url"
    }

    val avatarUrl: String
        get() = if (avatarUrls.isEmpty()) avatarUrls else avatarUrls.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]

    override fun toString(): String {
        return "id -> $id login -> $login"
    }
}
