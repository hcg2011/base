package com.chungo.base.http.imageloader

import android.content.Context
import com.chungo.base.http.imageloader.glide.GlideImageLoaderStrategy
import com.chungo.base.utils.Preconditions
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ================================================
 * [ImageLoader] 使用策略模式和建造者模式,可以动态切换图片请求框架(比如说切换成 Picasso )
 * 当需要切换图片请求框架或图片请求框架升级后变更了 Api 时
 * 这里可以将影响范围降到最低,所以封装 [ImageLoader] 是为了屏蔽这个风险
 *
 */
@Singleton
class ImageLoader @Inject
constructor() {
    @Inject
    lateinit var mStrategy: GlideImageLoaderStrategy

    /**
     * 可在运行时随意切换 [BaseImageLoaderStrategy]
     *
     * @param strategy
     */
    var loadImgStrategy: BaseImageLoaderStrategy<ImageConfig>
        get() = mStrategy as BaseImageLoaderStrategy<ImageConfig>
        set(strategy) {
            Preconditions.checkNotNull<BaseImageLoaderStrategy<ImageConfig>>(strategy, "strategy == null")
            this.mStrategy = strategy as GlideImageLoaderStrategy
        }

    /**
     * 加载图片
     *
     * @param context
     * @param config
     * @param <T>
    </T> */
    fun <T : ImageConfig> loadImage(context: Context, config: T) {
        Preconditions.checkNotNull<BaseImageLoaderStrategy<*>>(mStrategy, "Please implement BaseImageLoaderStrategy and call GlobalConfigModule.Builder#imageLoaderStrategy(BaseImageLoaderStrategy) in the applyOptions method of ConfigModule")
        val strategy = mStrategy as BaseImageLoaderStrategy<ImageConfig>
        strategy.loadImage(context, config)
    }

    /**
     * 停止加载或清理缓存
     *
     * @param context
     * @param config
     * @param <T>
    </T> */
    fun <T : ImageConfig> clear(context: Context, config: T) {
        Preconditions.checkNotNull<BaseImageLoaderStrategy<T>>(mStrategy as BaseImageLoaderStrategy<T>?, "Please implement BaseImageLoaderStrategy and call GlobalConfigModule.Builder#imageLoaderStrategy(BaseImageLoaderStrategy) in the applyOptions method of ConfigModule")
        val strategy = mStrategy as BaseImageLoaderStrategy<ImageConfig>
        strategy.clear(context, config)
    }
}
