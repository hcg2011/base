package com.chungo.baseapp.mvp.model.entity

import com.chad.library.adapter.base.entity.MultiItemEntity
import io.realm.annotations.Ignore
import java.io.Serializable

open class GameBean : Serializable, MultiItemEntity {
    var h5_url: String? = null
    var id: String? = null
    var icon_url: String? = null
    var name: String? = null
    var brief: String? = null
    var catid: String? = null
    var description: String? = null
    var source: String? = null
    var use_count: String? = null
    var developer: String? = null
    var tag: String? = null
    var lang: String? = null
    var remark: String? = null
    var catname: String? = null
    var ctime: String? = null
    var utime: String? = null
    var screen: String? = null
    var state: String? = null
    var e_name: String? = null
    @Ignore
    var typeCategory = XINPIN
    @Ignore
    var type = 0
    @Ignore
    var isPlayed = false
    var isHot: Boolean = false
    @Ignore
    var isAntiColor = false
    var last_modify_time: Long = 0
    var main_color = "#f6f6f6"//默认灰色
    var source_code_url: String? = null
    var local_url: String? = null
    var total_size: String? = null
    var local: Boolean? = null
    var position: Int = 0
    @Ignore
    private var isCheck = 0X00

    val last_modify_time_by_format: String
        get() {
            if (last_modify_time <= 0)
                last_modify_time = System.currentTimeMillis()

            return last_modify_time.toString()
        }

    val isCheckAvailable: Boolean?
        get() = isCheck != 0

    /**
     * @return true is checked
     */
    var checkState: Boolean?
        get() {
            if (isCheck and CHECK_STATE_OPEN != 0)
                return true
            return if (isCheck and CHECK_STATE_CLOSE != 0) false else false
        }
        set(open) = if (open!!) {
            isCheck = isCheck and CHECK_STATE_CLOSE.inv()
            isCheck = isCheck or CHECK_STATE_OPEN
        } else {
            isCheck = isCheck and CHECK_STATE_OPEN.inv()
            isCheck = isCheck or CHECK_STATE_CLOSE
        }

    fun resetCheckState() {
        this.isCheck = 0
    }

    override fun getItemType(): Int {
        return 0
    }

    companion object {
        val XINPIN = "TYPE_XINPIN"
        val JINGDIAN = "TYPE_JINGDIAN"
        val RENQI = "TYPE_RENQI"

        val CHECK_STATE_OPEN = 0X01
        val CHECK_STATE_CLOSE = 0X10

        val ITEM_HEADER = 0X01
        val ITEM_GAME = 0X03
    }
}
