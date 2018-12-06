package com.chungo.basemore.db.helper

import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject


/**
 * Created by codeest on 16/8/16.
 */

class RealmHelper : DBHelper<Any?> {


    companion object {

        private val DB_NAME = "myRealm.realm"
    }

    @Inject
    constructor()
    private val mRealm: Realm

    override//使用findAllSort ,先findAll再result.sort无效
    //RealmResults<RealmLikeBean> results = mRealm.where(RealmLikeBean.class).findAllSorted("time");
    //return mRealm.copyFromRealm(results);
    val likeList: List<Any?>?
        get() = null

    /**
     * 获取 掘金首页管理列表
     *
     * @return
     */
    override//        GoldManagerBean bean = mRealm.where(GoldManagerBean.class).findFirst();
    //        if (bean == null)
    //            return null;
    //        return mRealm.copyFromRealm(bean);
    val goldManagerList: Any?
        get() = null

    init {
        mRealm = Realm.getInstance(RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DB_NAME)
                .build())
    }

    /**
     * 增加 阅读记录
     *
     * @param id 使用@PrimaryKey注解后copyToRealm需要替换为copyToRealmOrUpdate
     */
    override fun insertNewsId(id: Int) {
        //        ReadStateBean bean = new ReadStateBean();
        //        bean.setId(id);
        //        mRealm.beginTransaction();
        //        mRealm.copyToRealmOrUpdate(bean);
        //        mRealm.commitTransaction();
    }

    /**
     * 查询 阅读记录
     *
     * @param id
     * @return
     */
    override fun queryNewsId(id: Int): Boolean {
        //        RealmResults<ReadStateBean> results = mRealm.where(ReadStateBean.class).findAll();
        //        for (ReadStateBean item : results) {
        //            if (item.getId() == id) {
        //                return true;
        //            }
        //        }
        return false
    }

    /**
     * 增加 收藏记录
     *
     * @param bean
     */
    override fun insertLikeBean(bean: Any?) {
        //        mRealm.beginTransaction();
        //        mRealm.copyToRealmOrUpdate(bean);
        //        mRealm.commitTransaction();
    }

    /**
     * 删除 收藏记录
     *
     * @param id
     */
    override fun deleteLikeBean(id: String) {
        //        RealmLikeBean data = mRealm.where(RealmLikeBean.class).equalTo("id", id).findFirst();
        //        mRealm.beginTransaction();
        //        if (data != null) {
        //            data.deleteFromRealm();
        //        }
        //        mRealm.commitTransaction();
    }

    /**
     * 查询 收藏记录
     *
     * @param id
     * @return
     */
    override fun queryLikeId(id: String): Boolean {
        //        RealmResults<RealmLikeBean> results = mRealm.where(RealmLikeBean.class).findAll();
        //        for (RealmLikeBean item : results) {
        //            if (item.getId().equals(id)) {
        //                return true;
        //            }
        //        }
        return false
    }

    /**
     * 修改 收藏记录 时间戳以重新排序
     *
     * @param id
     * @param time
     * @param isPlus
     */
    override fun changeLikeTime(id: String, time: Long, isPlus: Boolean) {
        //        RealmLikeBean bean = mRealm.where(RealmLikeBean.class).equalTo("id", id).findFirst();
        //        mRealm.beginTransaction();
        //        if (isPlus) {
        //            bean.setTime(++time);
        //        } else {
        //            bean.setTime(--time);
        //        }
        mRealm.commitTransaction()
    }

    /**
     * 更新 掘金首页管理列表
     *
     * @param bean
     */
    override fun updateGoldManagerList(bean: Any?) {
        //        GoldManagerBean data = mRealm.where(GoldManagerBean.class).findFirst();
        //        mRealm.beginTransaction();
        //        if (data != null) {
        //            data.deleteFromRealm();
        //        }
        //        mRealm.copyToRealm(bean);
        mRealm.commitTransaction()
    }

}
