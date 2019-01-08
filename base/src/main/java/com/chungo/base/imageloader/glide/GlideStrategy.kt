package com.chungo.base.imageloader.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chungo.base.di.module.GlobalConfigModule
import com.chungo.base.imageloader.BaseStrategy
import com.chungo.base.imageloader.ImageConfig
import com.chungo.base.utils.Preconditions
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * 此类只是简单的实现了 Glide 加载的策略,方便快速使用,但大部分情况会需要应对复杂的场景
 * 这时可自行实现 [BaseStrategy] 和 [ImageConfig] 替换现有策略
 *
 * @see GlobalConfigModule.Builder.imageLoaderStrategy
 */
class GlideStrategy : BaseStrategy<ImageConfigImpl>, GlideAppliesOptions {

    override fun loadImage(ctx: Context, config: ImageConfigImpl) {
        Preconditions.checkNotNull(ctx, "Context is required")
        Preconditions.checkNotNull(config, "ImageConfigImpl is required")
        Preconditions.checkNotNull(config.imageView, "ImageView is required")

        val requests: GlideRequests

        requests = Glides.with(ctx)//如果context是activity则自动使用Activity的生命周期

        val glideRequest = requests.load(config.url)

        when (config.cacheStrategy) {
            //缓存策略
            0 -> glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL)
            1 -> glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE)
            2 -> glideRequest.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            3 -> glideRequest.diskCacheStrategy(DiskCacheStrategy.DATA)
            4 -> glideRequest.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            else -> glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL)
        }

        if (config.isCrossFade) {
            glideRequest.transition(DrawableTransitionOptions.withCrossFade())
        }

        if (config.isCenterCrop) {
            glideRequest.centerCrop()
        }

        if (config.isCircle) {
            glideRequest.circleCrop()
        }

        if (config.isImageRadius) {
            glideRequest.transform(RoundedCorners(config.imageRadius))
        }

        if (config.isBlurImage) {
            glideRequest.transform(BlurTransformation(config.blurValue))
        }

        if (config.transformation != null) {//glide用它来改变图形的形状
            glideRequest.transform(config.transformation)
        }

        if (config.placeholder != 0)
        //设置占位符
            glideRequest.placeholder(config.placeholder)

        if (config.errorPic != 0)
        //设置错误的图片
            glideRequest.error(config.errorPic)

        if (config.fallback != 0)
        //设置请求 url 为空图片
            glideRequest.fallback(config.fallback)

        glideRequest
                .into(config.imageView!!)
    }

    override fun clear(ctx: Context, config: ImageConfigImpl) {
        Preconditions.checkNotNull(ctx, "Context is required")
        Preconditions.checkNotNull(config, "ImageConfigImpl is required")

        if (config.imageView != null) {
            Glides.get(ctx).getRequestManagerRetriever().get(ctx).clear(config.imageView!!)
        }

        if (config.imageViews != null && config.imageViews!!.size > 0) {//取消在执行的任务并且释放资源
            for (imageView in config.imageViews!!) {
                Glides.get(ctx).getRequestManagerRetriever().get(ctx).clear(imageView)
            }
        }

        if (config.isClearDiskCache) {//清除本地缓存
            Completable.fromAction { Glide.get(ctx).clearDiskCache() }.subscribeOn(Schedulers.io()).subscribe()
        }

        if (config.isClearMemory) {//清除内存缓存
            Completable.fromAction { Glide.get(ctx).clearMemory() }.subscribeOn(AndroidSchedulers.mainThread()).subscribe()
        }
    }

    override fun applyGlideOptions(context: Context, builder: GlideBuilder) {
        Timber.w("applyGlideOptions")
    }
}
