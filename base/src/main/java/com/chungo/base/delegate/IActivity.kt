package com.chungo.base.delegate


import android.app.Activity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.chungo.base.base.BaseActivity
import com.chungo.base.integration.cache.Cache
import com.chungo.base.lifecycle.ActivityLifecycle

/**
 * 每个 [Activity] 都需要实现此类,以满足规范
 *
 * @see BaseActivity
 */
interface IActivity {

    /**
     * 提供在 [Activity] 生命周期内的缓存容器, 可向此 [Activity] 存取一些必要的数据
     * 此缓存容器和 [Activity] 的生命周期绑定, 如果 [Activity] 在屏幕旋转或者配置更改的情况下
     * 重新创建, 那此缓存容器中的数据也会被清空, 如果你想避免此种情况请使用 [LifecycleModel](https://github.com/JessYanCoding/LifecycleModel)
     *
     * @return like [LruCache]
     */
    fun provideCache(): Cache<*, *>

    /**
     * 是否使用 EventBus
     * Arms 核心库现在并不会依赖某个 EventBus, 要想使用 EventBus, 还请在项目中自行依赖对应的 EventBus
     * 现在支持两种 EventBus, greenrobot 的 EventBus 和畅销书 《Android源码设计模式解析与实战》的作者 何红辉 所作的 AndroidEventBus
     * 确保依赖后, 将此方法返回 true, Arms 会自动检测您依赖的 EventBus, 并自动注册
     * 这种做法可以让使用者有自行选择三方库的权利, 并且还可以减轻 Arms 的体积
     *
     * @return 返回 `true`, Arms 会自动注册 EventBus
     */
    fun useEventBus(): Boolean

    /**
     * 初始化 View, 如果 [.initView] 返回 0, 框架则不会调用 [Activity.setContentView]
     *
     * @param savedInstanceState
     * @return
     */
    fun initView(savedInstanceState: Bundle?): Int

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    fun initData(savedInstanceState: Bundle?)

    /**
     * 这个 Activity 是否会使用 Fragment,会根据这个属性判断是否注册 [FragmentManager.FragmentLifecycleCallbacks]
     * 如果返回`false`,那意味着这个 Activity 不需要绑定 Fragment,那你再在这个 Activity 中绑定继承于 [BaseFragment] 的 Fragment 将不起任何作用
     * @see ActivityLifecycle.registerFragmentCallbacks
     * @return
     */
    fun useFragment(): Boolean
}