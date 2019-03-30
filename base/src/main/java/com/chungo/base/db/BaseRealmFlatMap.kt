package com.chungo.base.db

import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmQuery
import io.realm.RealmResults
import retrofit2.Response

/**
 * 保存response中的实体信息
 */
open class FlatMapRealmSaveResult<T, E : RealmModel>
constructor(response: Response<T>,
            private val clazz: Class<E>,
            private val listener: FlatTransactionInterface<E>,
            needSave: Boolean) {
    init {
        if (response.isSuccessful && needSave) {
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction { bgRealm ->
                val results = listener.query(bgRealm.where(clazz))
                val commitTarget = if (results.isNotEmpty()) {
                    results[0]
                } else {
                    bgRealm.createObject(clazz)
                }
                listener.onTransaction(commitTarget)
            }
        }
    }
}

/**
 * 获取数据库保存的列表信息
 */
fun <T, E : RealmModel> FlatMapRealmReadList(realm: Realm, listener: FlatRealmReadConversionInterface<T, E>): ArrayList<Any> {
    val realmResults = listener.query(realm)
    val list = if (realmResults.isEmpty()) {
        ArrayList()
    } else {
        listener.onJSON(realmResults[0]!!)
    }
    val dataList = ArrayList<Any>()
    for (item in list) {
        dataList.add(listener.onConversion(item))
    }
    return dataList
}

/**
 * 获取数据库保存的实体信息
 */
fun <T, E : RealmModel, R> FlatMapRealmReadObject(realm: Realm, listener: FlatRealmReadConversionObjectInterface<T, E, R>): R? {
    val realmResults = listener.query(realm)
    val data = if (realmResults.isEmpty()) {
        null
    } else {
        listener.onJSON(realmResults[0]!!)
    }
    return listener.onConversion(data)
}

interface FlatTransactionInterface<E : RealmModel> {
    fun query(q: RealmQuery<E>): RealmResults<E>
    fun onTransaction(targetObject: E?)
}

interface FlatRealmReadConversionInterface<T, E> {
    fun query(realm: Realm): RealmResults<E>
    fun onJSON(t: E): List<T>
    fun onConversion(t: T): Any
}

interface FlatRealmReadConversionObjectInterface<T, E, R> {
    fun query(realm: Realm): RealmResults<E>
    fun onJSON(t: E): T
    fun onConversion(t: T?): R?
}