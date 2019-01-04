package com.chungo.basemore.db.helper

interface DBHelper<T> {

    val likeList: List<Any?>?

    /**
     * 获取 掘金首页管理列表
     *
     * @return
     */
    val goldManagerList: T

    fun insertNewsId(id: Int)

    /**
     * 查询 阅读记录
     *
     * @param id
     * @return
     */
    fun queryNewsId(id: Int): Boolean

    /**
     * 增加 收藏记录
     *
     * @param bean
     */
    fun insertLikeBean(bean: T)

    /**
     * 删除 收藏记录
     *
     * @param id
     */
    fun deleteLikeBean(id: String)

    /**
     * 查询 收藏记录
     *
     * @param id
     * @return
     */
    fun queryLikeId(id: String): Boolean

    /**
     * 修改 收藏记录 时间戳以重新排序
     *
     * @param id
     * @param time
     * @param isPlus
     */
    fun changeLikeTime(id: String, time: Long, isPlus: Boolean)

    /**
     * 更新 掘金首页管理列表
     *
     * @param bean
     */
    fun updateGoldManagerList(bean: T)
}
