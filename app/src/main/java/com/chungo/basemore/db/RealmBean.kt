package com.chungo.basemore.db

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

/**
 * 使用realm数据库,所有的bean类需要继承该类
 *
 * @Description
 * @Author huangchangguo
 * @Created 2018/11/22 14:06
 */
open class RealmBean : RealmObject(), Serializable {

    @PrimaryKey
    var id: String? = null
}
