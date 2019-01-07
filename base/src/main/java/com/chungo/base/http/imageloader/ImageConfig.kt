package com.chungo.base.http.imageloader

import android.widget.ImageView

/**
 * 这里是图片加载配置信息的基类,定义一些所有图片加载框架都可以用的通用参数
 * 每个 [BaseImageLoaderStrategy] 应该对应一个 [ImageConfig] 实现类
 *
 */
open class ImageConfig {
    var url: String? = null
        protected set
    var imageView: ImageView? = null
        protected set
    var placeholder: Int = 0
        protected set//占位符
    var errorPic: Int = 0
        protected set//错误占位符
}
