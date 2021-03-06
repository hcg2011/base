package com.chungo.base.imageloader

import android.content.Context
import com.chungo.base.imageloader.glide.GlideStrategy
import com.chungo.base.utils.Preconditions

/**
 * [ImageLoader] 使用策略模式和建造者模式,可以动态切换图片请求框架(比如说切换成 Picasso )
 * 当需要切换图片请求框架或图片请求框架升级后变更了 Api 时
 * 这里可以将影响范围降到最低,所以封装 [ImageLoader] 是为了屏蔽这个风险
 *
 */

class ImageLoader {
    var mStrategy: GlideStrategy

    init {
        mStrategy = GlideStrategy()
    }

    /**
     * 可在运行时随意切换 [BaseStrategy]
     *
     * @param strategy
     */
    var loadImgStrategy: BaseStrategy<ImageConfig>
        get() = mStrategy as BaseStrategy<ImageConfig>
        set(strategy) {
            Preconditions.checkNotNull<BaseStrategy<ImageConfig>>(strategy, "strategy == null")
            this.mStrategy = strategy as GlideStrategy
        }

    /**
     * 加载图片
     *
     * @param context
     * @param config
     * @param <T>
    </T> */
    fun <T : ImageConfig> loadImage(context: Context, config: T) {
        Preconditions.checkNotNull<BaseStrategy<*>>(mStrategy, "Please implement BaseImageLoaderStrategy and call GlobalConfigModule.Builder#imageLoaderStrategy(BaseImageLoaderStrategy) in the applyOptions method of ConfigModule")
        val strategy = mStrategy as BaseStrategy<ImageConfig>
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
        Preconditions.checkNotNull<BaseStrategy<T>>(mStrategy as BaseStrategy<T>?, "Please implement BaseImageLoaderStrategy and call GlobalConfigModule.Builder#imageLoaderStrategy(BaseImageLoaderStrategy) in the applyOptions method of ConfigModule")
        val strategy = mStrategy as BaseStrategy<ImageConfig>
        strategy.clear(context, config)
    }

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ImageLoader()
        }
    }
}
