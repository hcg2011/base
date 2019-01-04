/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chungo.baseapp.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.chungo.base.di.component.AppComponent
import com.chungo.base.http.imageloader.ImageLoader
import com.chungo.base.http.imageloader.glide.ImageConfigImpl
import com.chungo.base.utils.ArmsUtils
import com.chungo.baseapp.R
import com.chungo.baseapp.adapter.BaseHolder
import com.chungo.baseapp.adapter.DefaultAdapter
import com.chungo.basemore.mvp.model.entity.User
import io.reactivex.Observable


/**
 * ================================================
 * 展示 [BaseHolder] 的用法
 *
 *
 * Created by JessYan on 9/4/16 12:56
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class UserItemHolder(itemView: View) : BaseHolder<User>(itemView) {

    @BindView(R.id.iv_avatar)
    lateinit var mAvatar: ImageView
    @BindView(R.id.tv_name)
    lateinit var mName: TextView
    private var mAppComponent: AppComponent? = null
    private var mImageLoader: ImageLoader? = null//用于加载图片的管理类,默认使用 Glide,使用策略模式,可替换框架

    init {
        //可以在任何可以拿到 Context 的地方,拿到 AppComponent,从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.context)
        mImageLoader = mAppComponent!!.imageLoader()
    }

    override fun setData(data: User, position: Int) {
        Observable.just(data.login)
                .subscribe { s -> mName.text = s }

        //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
        mImageLoader!!.loadImage(itemView.context,
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
        mImageLoader!!.clear(mAppComponent!!.application(), ImageConfigImpl.builder()
                .imageViews(mutableListOf<ImageView>(mAvatar))
                .build())
        this.mAppComponent = null
        this.mImageLoader = null
    }
}
