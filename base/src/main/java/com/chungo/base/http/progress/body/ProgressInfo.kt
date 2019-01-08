package com.chungo.base.http.progress.body

import android.os.Parcel
import android.os.Parcelable

/**
 * [ProgressInfo] 用于存储与进度有关的变量,已实现 [Parcelable]
 */
class ProgressInfo : Parcelable {
    var currentbytes: Long = 0
        internal set //当前已上传或下载的总长度
    var contentLength: Long = 0
        internal set //数据总长度
    var intervalTime: Long = 0
        internal set //本次调用距离上一次被调用所间隔的时间(毫秒)
    var eachBytes: Long = 0
        internal set //本次调用距离上一次被调用的间隔时间内上传或下载的byte长度
    var id: Long = 0
        private set //如果同一个 Url 地址,上一次的上传或下载操作都还没结束,
    //又开始了新的上传或下载操作(比如用户点击多次点击上传或下载同一个 Url 地址,当然你也可以在上层屏蔽掉用户的重复点击),
    //此 id (请求开始时的时间)就变得尤为重要,用来区分正在执行的进度信息,因为是以请求开始时的时间作为 id ,所以值越大,说明该请求越新
    var isFinish: Boolean = false
        internal set //进度是否完成

    /**
     * 获取百分比,该计算舍去了小数点,如果你想得到更精确的值,请自行计算
     *
     * @return
     */
    val percent: Int
        get() = if (currentbytes <= 0 || contentLength <= 0) 0 else (100 * currentbytes / contentLength).toInt()

    /**
     * 获取上传或下载网络速度,单位为byte/s,如果你想得到更精确的值,请自行计算
     *
     * @return
     */
    val speed: Long
        get() = if (eachBytes <= 0 || intervalTime <= 0) 0 else eachBytes * 1000 / intervalTime


    constructor(id: Long) {
        this.id = id
    }

    override fun toString(): String {
        return "ProgressInfo{" +
                "id=" + id +
                ", currentBytes=" + currentbytes +
                ", contentLength=" + contentLength +
                ", eachBytes=" + eachBytes +
                ", intervalTime=" + intervalTime +
                ", finish=" + isFinish +
                '}'.toString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(this.currentbytes)
        dest.writeLong(this.contentLength)
        dest.writeLong(this.intervalTime)
        dest.writeLong(this.eachBytes)
        dest.writeLong(this.id)
        dest.writeByte(if (this.isFinish) 1.toByte() else 0.toByte())
    }

    protected constructor(`in`: Parcel) {
        this.currentbytes = `in`.readLong()
        this.contentLength = `in`.readLong()
        this.intervalTime = `in`.readLong()
        this.eachBytes = `in`.readLong()
        this.id = `in`.readLong()
        this.isFinish = `in`.readByte().toInt() != 0
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<ProgressInfo> = object : Parcelable.Creator<ProgressInfo> {
            override fun createFromParcel(source: Parcel): ProgressInfo {
                return ProgressInfo(source)
            }

            override fun newArray(size: Int): Array<ProgressInfo?> {
                return arrayOfNulls(size)
            }
        }
    }
}
