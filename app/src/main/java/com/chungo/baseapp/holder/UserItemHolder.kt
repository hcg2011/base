package com.chungo.baseapp.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chungo.base.imageloader.ImageLoader
import com.chungo.base.imageloader.glide.ImageConfigImpl
import com.chungo.baseapp.R
import com.chungo.baseapp.adapter.BaseHolder
import com.chungo.baseapp.adapter.DefaultAdapter
import com.chungo.baseapp.mvp.model.entity.User
import io.reactivex.Observable


/**
 * 展示 [BaseHolder] 的用法
 */
class UserItemHolder(itemView: View) : BaseHolder<User>(itemView) {

    lateinit var mAvatar: ImageView
    lateinit var mName: TextView

    init {
        mAvatar = itemView.findViewById(R.id.iv_avatar)
        mName = itemView.findViewById(R.id.tv_name)
    }

    override fun setData(data: User, position: Int) {

        Observable.just(data.login)
                .subscribe { s -> mName.text = s }

        //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
        ImageLoader.instance.loadImage(itemView.context,
                ImageConfigImpl
                        .builder()
                        .url(data.avatarUrl)
                        .imageView(mAvatar)
                        .build())
    }


    /**
     * 在 Activity 的 onDestroy 中使用 [DefaultAdapter.releaseAllHolder] 方法 (super.onDestroy() 之前)
     * [BaseHolder.onRelease] 才会被调用, 可以在此方法中释放一些资源
     */
    override fun onRelease() {
        ImageLoader.instance.clear(itemView.context, ImageConfigImpl.builder()
                .imageViews(mutableListOf<ImageView>(mAvatar))
                .build())
    }
}
