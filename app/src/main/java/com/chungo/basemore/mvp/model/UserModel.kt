package com.chungo.basemore.mvp.model

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import com.chungo.base.api.cache.CommonCache
import com.chungo.base.api.service.UserService
import com.chungo.base.integration.IRepositoryManager
import com.chungo.basemore.mvp.contract.UserContract
import com.chungo.basemore.mvp.model.entity.User
import io.reactivex.Observable
import io.rx_cache2.DynamicKey
import io.rx_cache2.EvictDynamicKey
import timber.log.Timber
import javax.inject.Inject

class UserModel : BaseModel, UserContract.Model {
    companion object {
        val USERS_PER_PAGE = 10
    }

    @Inject
    constructor(repositoryManager: IRepositoryManager) : super(repositoryManager)

    override fun getUsers(lastIdQueried: Int, update: Boolean): Observable<List<User>> {
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return Observable.just(mRepositoryManager!!
                .obtainRetrofitService(UserService::class.java)
                .getUsers(lastIdQueried, USERS_PER_PAGE))
                .flatMap { listObservable ->
                    mRepositoryManager!!.obtainCacheService(CommonCache::class.java)
                            .getUsers(listObservable, DynamicKey(lastIdQueried), EvictDynamicKey(update))
                            .map { listReply -> listReply.data as List<User> }
                }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    internal fun onPause() {
        Timber.d("Release Resource")
    }
}
