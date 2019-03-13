package com.chungo.baseapp.mvp.model.entity

import com.chungo.baseapp.api.Api

import java.io.Serializable


/**
 * 如果你服务器返回的数据格式固定为这种方式(这里只提供思想,服务器返回的数据格式可能不一致,可根据自家服务器返回的格式作更改)
 * 指定范型即可改变 `data` 字段的类型, 达到重用 [BaseResponse], 如果你实在看不懂, 请忽略
 *
 *
 */
open class BaseResponse<T> : Serializable {
    val data: T? = null
    val code: String? = null
    val msg: String? = null

    /**
     * 请求是否成功
     *
     * @return
     */
    val isSuccess: Boolean
        get() = code == Api.RequestSuccess
}
