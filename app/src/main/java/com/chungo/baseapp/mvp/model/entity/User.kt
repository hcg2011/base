package com.chungo.baseapp.mvp.model.entity

class User(val id: Int, val login: String, private val avatar_url: String) {

    companion object {
        const val KEY_URL = "key_url"
    }

    val avatarUrl: String
        get() = if (avatar_url.isEmpty()) avatar_url else avatar_url.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]

    override fun toString(): String {
        return "id -> $id login -> $login"
    }
}
