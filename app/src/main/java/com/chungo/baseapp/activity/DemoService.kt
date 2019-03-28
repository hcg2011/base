package com.chungo.baseapp.activity

import android.content.Intent
import android.util.Log
import com.chungo.base.base.BaseService
import com.chungo.baseapp.adapter.UserAdapter
import javax.inject.Inject

/**
 * @Description
 *
 * @Author huangchangguo
 * @Created  2019/3/28 11:37
 *
 */
class DemoService : BaseService() {
    @Inject
    internal lateinit var mAdapter: UserAdapter

    override fun init() {
        Log.d("hcg_log", "rxPermissions=$mAdapter")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
}