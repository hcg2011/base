package com.chungo.baseapp.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.chungo.base.di.component.AppComponent
import com.chungo.base.imageloader.ImageLoader
import com.chungo.base.imageloader.glide.ImageConfigImpl
import com.chungo.base.utils.AppUtils
import com.chungo.baseapp.R
import com.chungo.baseapp.adapter.BaseHolder
import com.chungo.baseapp.adapter.DefaultAdapter
import com.chungo.baseapp.mvp.model.entity.User
import io.reactivex.Observable


/**
 * 展示 [BaseHolder] 的用法
 */
class UserItemHolder(itemView: View) : BaseHolder<User>(itemView) {

    @BindView(R.id.iv_avatar)
    lateinit var mAvatar: ImageView
    @BindView(R.id.tv_name)
    lateinit var mName: TextView
    private var mAppComponent: AppComponent? = null

    init {
        //可以在任何可以拿到 Context 的地方,拿到 AppComponent,从而得到用 Dagger 管理的单例对象
        mAppComponent = AppUtils.obtainAppComponentFromContext(itemView.context)
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
        //只要传入的 Context 为 Activity, Glide 就会自己做好生命周期的管理, 其实在上面的代码中传入的 Context 就是 Activity
        //所以在 onRelease 方法中不做 clear 也是可以的, 但是在这里想展示一下 clear 的用法
        ImageLoader.instance.clear(mAppComponent!!.application(), ImageConfigImpl.builder()
                .imageViews(mutableListOf<ImageView>(mAvatar))
                .build())
        this.mAppComponent = null
    }
}
