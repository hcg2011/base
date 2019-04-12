package com.chungo.baseapp.mvp.model.entity

import com.squareup.moshi.Json
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmField

//@JsonClass(generateAdapter = true)

open class User(
        @PrimaryKey
        var id: Int = 0,
        var login: String = "",
        @Json(name = "avatar_url")
        @RealmField(name = "avatar_url")
        var avatarUrls: String = "",
        var refreshTimes: Int = 0) : RealmObject() {

    companion object {
        const val KEY_URL = "key_url"
    }


    val avatarUrl: String
        get() = if (avatarUrls.isEmpty()) avatarUrls else avatarUrls.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]

    override fun toString(): String {
        return "id -> $id login -> $login"
    }
}
