package com.chungo.base.mvp

import com.chungo.base.http.IRepositoryManager


open class BaseModel constructor(protected var mRepositoryManager: IRepositoryManager?) : IModel {
    override fun onDestroy() {
        mRepositoryManager = null
    }
}
