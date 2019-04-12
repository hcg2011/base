package com.chungo.baseapp.db.helper

import android.content.Context
import io.realm.*
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.security.SecureRandom

class RealmHelper private constructor() {
    private val mRealm = Realm.getDefaultInstance()

    private object SingletonHolder {
        private val INSTANCE = RealmHelper(
        )
    }

    /**
     * 增加单条数据到数据库中
     *
     * @param bean 数据对象，必须继承了RealmObject
     */
    fun add(bean: RealmObject) {
        mRealm.executeTransaction { realm -> realm.copyToRealmOrUpdate(bean) }

    }

    /**
     * 增加多条数据到数据库中
     *
     * @param beans 数据集合，其中元素必须继承了RealmObject
     */
    fun add(beans: List<RealmObject>) {
        mRealm.executeTransaction { realm -> realm.copyToRealmOrUpdate(beans) }

    }

    /**
     * 增加多条数据到数据库中
     *
     * @param beans 数据集合，其中元素必须继承了RealmObject
     */
    fun addAsync(beans: List<RealmObject>) {
        mRealm.executeTransactionAsync { realm -> realm.copyToRealmOrUpdate(beans) }

    }

    /**
     * 删除数据库中clazz类所属所有元素
     *
     * @param clazz
     */
    fun deleteAll(clazz: Class<RealmObject>) {
        val beans = mRealm.where<RealmObject>(clazz).findAll()
        mRealm.executeTransaction { beans.deleteAllFromRealm() }
    }

    /**
     * 删除数据库中clazz类所属所有元素
     *
     * @param clazz
     */
    fun deleteAllAsync(clazz: Class<RealmObject>) {
        val beans = mRealm.where<RealmObject>(clazz).findAll()
        mRealm.executeTransactionAsync { beans.deleteAllFromRealm() }


    }

    /**
     * 删除数据库中clazz类所属第一个元素
     *
     * @param clazz
     */
    fun deleteFirst(clazz: Class<RealmObject>) {
        val beans = mRealm.where<RealmObject>(clazz).findAll()
        mRealm.executeTransaction { beans.deleteFirstFromRealm() }
    }

    /**
     * 删除数据库中clazz类所属最后一个元素
     *
     * @param clazz
     */
    fun deleteLast(clazz: Class<RealmObject>) {
        val beans = mRealm.where<RealmObject>(clazz).findAll()
        mRealm.executeTransaction { beans.deleteLastFromRealm() }

    }

    /**
     * 删除数据库中clazz类所属数据中某一位置的元素
     *
     * @param clazz
     * @param position
     */
    fun deleteElement(clazz: Class<RealmObject>, position: Int) {
        val beans = mRealm.where<RealmObject>(clazz).findAll()
        mRealm.executeTransaction { beans.deleteFromRealm(position) }
    }


    /**
     * 查询数据库中clazz类所属所有数据
     *
     * @param clazz
     * @return
     */
    fun queryAll(clazz: Class<RealmObject>): List<RealmObject> {
        val beans = mRealm.where<RealmObject>(clazz).findAll()
        return mRealm.copyFromRealm(beans)
    }

    /**
     * 查询数据库中clazz类所属所有数据
     *
     * @param clazz
     * @return
     */
    fun queryAllAsync(clazz: Class<RealmObject>): List<RealmObject> {
        val beans = mRealm.where<RealmObject>(clazz).findAllAsync()
        return mRealm.copyFromRealm(beans)
    }

    /**
     * 查询满足条件的第一个数据
     *
     * @param clazz
     * @param fieldName
     * @param value
     * @return
     * @throws NoSuchFieldException
     */
    @Throws(NoSuchFieldException::class)
    fun queryFirst(clazz: Class<RealmObject>, fieldName: String, value: String): RealmObject? {
        return mRealm.where<RealmObject>(clazz).equalTo(fieldName, value).findFirst()
    }

    /**
     * 查询满足条件的第一个数据
     *
     * @param clazz
     * @param fieldName
     * @param value
     * @return
     * @throws NoSuchFieldException
     */
    @Throws(NoSuchFieldException::class)
    fun queryFirst(clazz: Class<RealmObject>, fieldName: String, value: Int): RealmObject? {
        return mRealm.where<RealmObject>(clazz).equalTo(fieldName, value).findFirst()
    }

    /**
     * 查询满足条件的所有数据
     *
     * @param clazz
     * @param fieldName
     * @param value
     * @return
     * @throws NoSuchFieldException
     */
    @Throws(NoSuchFieldException::class)
    fun queryAll(clazz: Class<RealmObject>, fieldName: String, value: String): List<RealmObject> {
        val beans = mRealm.where<RealmObject>(clazz).equalTo(fieldName, value).findAll()
        return mRealm.copyFromRealm(beans)
    }

    /**
     * 查询满足条件的所有数据
     *
     * @param clazz
     * @param fieldName
     * @param value
     * @return
     * @throws NoSuchFieldException
     */
    @Throws(NoSuchFieldException::class)
    fun queryAllAsync(clazz: Class<RealmObject>, fieldName: String, value: String): List<RealmObject> {
        val beans = mRealm.where<RealmObject>(clazz).equalTo(fieldName, value).findAllAsync()
        return mRealm.copyFromRealm(beans)
    }


    /**
     * 查询满足条件的所有数据
     *
     * @param clazz
     * @param fieldName
     * @param value
     * @return
     * @throws NoSuchFieldException
     */
    @Throws(NoSuchFieldException::class)
    fun queryAll(clazz: Class<RealmObject>, fieldName: String, value: Int): List<RealmObject> {
        val beans = mRealm.where<RealmObject>(clazz).equalTo(fieldName, value).findAll()
        return mRealm.copyFromRealm(beans)
    }

    /**
     * 查询满足条件的所有数据
     *
     * @param clazz
     * @param fieldName
     * @param value
     * @return
     * @throws NoSuchFieldException
     */
    @Throws(NoSuchFieldException::class)
    fun queryAllAsync(clazz: Class<RealmObject>, fieldName: String, value: Int): List<RealmObject> {
        val beans = mRealm.where<RealmObject>(clazz).equalTo(fieldName, value).findAllAsync()
        return mRealm.copyFromRealm(beans)
    }

    /**
     * 查询数据，排序
     *
     * @param clazz
     * @param fieldName
     * @param isAscendOrDescend true 增序， false 降序
     * @return
     */
    fun queryAllBySort(clazz: Class<RealmObject>, fieldName: String, isAscendOrDescend: Boolean): List<RealmObject> {
        val sort = if (isAscendOrDescend) Sort.ASCENDING else Sort.DESCENDING
        val beans = mRealm.where<RealmObject>(clazz).findAll()
        val results = beans.sort(fieldName, sort)
        return mRealm.copyFromRealm(results)
    }


    /**
     * 根据 主键 获取 对象，修改对象中的某个属性
     *
     * @param clazz
     * @param primaryKeyName  主键名称
     * @param primaryKeyValue 主键类型，一般为 Integer 和 String
     * @param fieldName       修改方法名称，如setXXX
     * @param newValue        修改对象的值，类型可能为
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Throws(NoSuchMethodException::class, InvocationTargetException::class, IllegalAccessException::class)
    fun updateParamByPrimaryKey(clazz: Class<RealmObject>, primaryKeyName: String, primaryKeyValue: Any, fieldName: String, newValue: Any) {
        var realmObject: RealmObject? = null
        if (primaryKeyValue is Int) {
            realmObject = mRealm.where<RealmObject>(clazz).equalTo(primaryKeyName, primaryKeyValue).findFirst()
        } else if (primaryKeyValue is String) {
            realmObject = mRealm.where<RealmObject>(clazz).equalTo(primaryKeyName, primaryKeyValue).findFirst()
        }
        mRealm.beginTransaction()
        if (newValue is Int) {
            val method = clazz.getMethod(fieldName, Int::class.javaPrimitiveType!!)
            method.invoke(realmObject, newValue)
        } else {
            val method = clazz.getMethod(fieldName, newValue.javaClass)
            method.invoke(realmObject, newValue)
        }
        mRealm.commitTransaction()
    }


    /**
     * 根据 主键 获取 对象，修改对象中的  “某些”  属性
     *
     * @param clazz
     * @param primaryKeyName
     * @param primaryKeyValue
     * @param params          属性map，key 保存fieldName,value 为 属性值
     * @throws NullPointerException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Throws(NullPointerException::class, NoSuchMethodException::class, InvocationTargetException::class, IllegalAccessException::class)
    fun updateParamsByPrimaryKey(clazz: Class<RealmObject>, primaryKeyName: String, primaryKeyValue: Any, params: Map<String, Any>?) {
        var realmObject: RealmObject? = null
        if (primaryKeyValue is Int) {
            realmObject = mRealm.where<RealmObject>(clazz).equalTo(primaryKeyName, primaryKeyValue).findFirst()
        } else if (primaryKeyValue is String) {
            realmObject = mRealm.where<RealmObject>(clazz).equalTo(primaryKeyName, primaryKeyValue).findFirst()
        }
        mRealm.beginTransaction()
        if (params == null || params.isEmpty()) {
            throw NullPointerException("It can't be empty by Map")
        } else {
            for ((key, value) in params) {
                val method: Method
                if (value is Int) {
                    method = clazz.getMethod(key, Int::class.javaPrimitiveType!!)
                } else {
                    method = clazz.getMethod(key, value.javaClass)
                }
                method.invoke(realmObject, value)
            }
        }
        mRealm.commitTransaction()
    }

    /**
     * 更新某个对象的所有数据
     *
     * @param clazz
     * @param lists
     */
    fun updateAll(clazz: Class<RealmObject>, lists: List<RealmObject>) {
        deleteAll(clazz)
        add(lists)
    }


    /**
     * 更新某个对象的所有数据
     *
     * @param clazz
     * @param realmObject
     */
    fun updateAll(clazz: Class<RealmObject>, realmObject: RealmObject) {
        deleteAll(clazz)
        add(realmObject)
    }

    companion object {
        const val DB_NAME = "myRealm.realm"
        const val DB_VERSION = 1L
        fun getRealm(context: Context): Realm {
            val key = ByteArray(64)
            SecureRandom().nextBytes(key)
            Realm.init(context)
            val migration = RealmMigration { realm, oldVersion, newVersion -> }
            val config = RealmConfiguration.Builder()
                    .name(DB_NAME) //文件名
                    .schemaVersion(DB_VERSION) //版本号
                    .migration(migration)//数据库版本迁移（数据库升级，当数据库中某个表添加字段或者删除字段）
                    .deleteRealmIfMigrationNeeded()//声明版本冲突时自动删除原数据库(当调用了该方法时，上面的方法将失效)。
                    .build()//创建
            return Realm.getInstance(config)
        }
    }

}
