package com.chungo.base.mvp

import com.chungo.base.http.IRepositoryManager

/**
 * 基类 Model
 */
open class BaseModel constructor(protected var mRepositoryManager: IRepositoryManager?//用于管理网络请求层, 以及数据缓存层
) : IModel {

    override fun onDestroy() {
        mRepositoryManager = null
    }
}
