package com.chungo.baseapp.mvp.model.entity

import com.squareup.moshi.Json
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

//@JsonClass(generateAdapter = true)

open class User : RealmObject() {

    companion object {
        const val KEY_URL = "key_url"
    }

    @PrimaryKey
    open var id: Int = 0
    open var login: String = ""
    @Json(name = "avatar_url")
    open var avatarUrls: String = ""
    open var refreshTimes: Int = 0

    val avatarUrl: String
        get() = if (avatarUrls.isEmpty()) avatarUrls else avatarUrls.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]

    override fun toString(): String {
        return "id -> $id login -> $login"
    }
}
