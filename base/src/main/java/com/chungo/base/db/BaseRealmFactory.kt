package com.chungo.base.db

import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Realm数据库工厂类
 */

open class BaseRealmFactory {
    protected open val dbVersion = 1L
    protected open val dbName = "myRealm.realm"

    init {
        val config = RealmConfiguration.Builder()
                .name(dbName)
                .schemaVersion(dbVersion)
                .build()
        Realm.setDefaultConfiguration(config)
    }

    open fun getRealmObservable(): Observable<Realm> {
        return Observable.create {
            val observableRealm = Realm.getDefaultInstance()
            it.onNext(observableRealm)
            it.onComplete()
        }
    }

    companion object {
        val instance: BaseRealmFactory by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BaseRealmFactory()
        }
    }
}