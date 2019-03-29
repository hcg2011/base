package com.shuyu.github.kotlin.common.db

import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Realm数据库工厂类
 */

class RealmFactory private constructor() {
    init {
        val config = RealmConfiguration.Builder()
                .name(DB_NAME)
                .schemaVersion(DB_VERSION)
                .build()
        Realm.setDefaultConfiguration(config)
    }

    companion object {
        const val DB_VERSION = 1L
        const val DB_NAME = "myRealm.realm"

        val instance: RealmFactory by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RealmFactory()
        }

        fun getRealmObservable(): Observable<Realm> {
            return Observable.create { emitter ->
                val observableRealm = Realm.getDefaultInstance()
                emitter.onNext(observableRealm)
                emitter.onComplete()
            }
        }
    }
}