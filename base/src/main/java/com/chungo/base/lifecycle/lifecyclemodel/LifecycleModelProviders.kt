package com.chungo.base.lifecycle.lifecyclemodel

import android.app.Activity
import android.app.Application
import android.support.annotation.MainThread
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity


/**
 * [LifecycleModelProviders] 配合 [LifecycleModel] 的实现类可以帮助 [Activity] 和 [Fragment]
 * 储存和管理一些与 UI 相关以及他们必需的数据, 并且这些数据在屏幕旋转或配置更改引起的 [Activity] 重建的情况下也会被保留, 直到最后被 finish
 * 但是 [LifecycleModel] 的实现类切勿直接引用 [Activity] 和 [Fragment] 以及他们里面 UI 元素
 *
 *
 * 他还可以让开发者能够在绑定在同一个 [Activity] 之下的多个不同的 [Fragment] 之间通讯以及共享数据 (Activity 与 Fragmnet 之间的通讯以及共享数据也可以)
 * 使用这种方式进行通讯, [Fragment] 之间不存在任何耦合关系
 *
 *
 * [LifecycleModel] 可以用来存储以及管理数据, 也可以作为 MVP 模式中的 Presenter (只要实现 [LifecycleModel] 即可)
 * 或者 MVVM 模式中的 ViewModel (只要实现 [LifecycleModel] 即可) 用来管理业务逻辑, 更多使用方式等着你去发现
 * <pre>
 * public class UserLifecycleModel implements LifecycleModel {
 * private int id;
 *
 * public UserLifecycleModel(int id) {
 * this.id = id;
 * }
 *
 * void doAction() {
 *
 * }
 * }
 *
 *
 * public class MyActivity extends AppCompatActivity {
 *
 * protected void onCreate(@Nullable Bundle savedInstanceState) {
 * LifecycleModelProviders.of(this).put(UserLifecycleModel.class.getName(), new UserLifecycleModel(123));
 * fragmentManager.beginTransaction().add(R.layout.afragment_container_Id, new AFragment).commit();
 * fragmentManager.beginTransaction().add(R.layout.bfragment_container_Id, new BFragment).commit();
 * }
 * }
</pre> *
 *
 *
 * 只要 AFragment 和 BFragment 绑定在同一个 Activity 下, 并使用同一个 key, 那获取到的 UserLifecycleModel 就是同一个对象
 * 这时就可以使用这个 UserLifecycleModel 进行通讯 (Fragment 之间如何通讯? 比如说接口回调? 观察者模式?) 和共享数据
 * 这时 Fragment 之间并不知道彼此, 也不互相持有, 所以也不存在耦合关系
 *
 *
 * <pre>
 * public class AFragment extends Fragment {
 * public void onStart() {
 * UserLifecycleModel userLifecycleModel = LifecycleModelProviders.of(getActivity()).get(UserLifecycleModel.class.getName());
 * }
 * }
 *
 * public class BFragment extends Fragment {
 * public void onStart() {
 * UserLifecycleModel userLifecycleModel = LifecycleModelProviders.of(getActivity()).get(UserLifecycleModel.class.getName());
 * }
 * }
 *
</pre> *
 *
 * @see [
 * 功能和 Android Architecture 中的 ViewModel 一致, 但放开了权限不仅可以存储 ViewModel, 还可以存储任意自定义对象
](https://developer.android.google.cn/topic/libraries/architecture/viewmodel.html) */
object LifecycleModelProviders {


    private fun checkApplication(activity: Activity): Application {
        return activity.application
                ?: throw IllegalStateException("Your activity/fragment is not yet attached to " + "Application. You can't request LifecycleModel before onCreate call.")
    }

    private fun checkActivity(fragment: Fragment): Activity {
        return fragment.activity
                ?: throw IllegalStateException("Can't create LifecycleModelStore for detached fragment")
    }

    /**
     * 返回的 [LifecycleModelStore] 可以在传入的这个 {code fragment} 的生命范围内一直存活者
     * [LifecycleModelStore] 可以存储不同的 [LifecycleModel] 实现类
     * 更多信息请查看 [LifecycleModelProviders] 顶部的注释
     *
     * @param fragment [LifecycleModel] 在这个 {code fragment} 的生命范围内被保留
     * @return a LifecycleModelStore instance
     */
    @MainThread
    fun of(fragment: Fragment): LifecycleModelStore {
        checkApplication(checkActivity(fragment))
        return LifecycleModelStores.of(fragment)
    }

    /**
     * 返回的 [LifecycleModelStore] 可以在传入的这个 {code activity} 的生命范围内一直存活着
     * [LifecycleModelStore] 可以存储不同的 [LifecycleModel] 实现类
     * 更多信息请查看 [LifecycleModelProviders] 顶部的注释
     *
     * @param activity [LifecycleModel] 在这个 {code activity} 的生命范围内被保留
     * @return a LifecycleModelStore instance
     */
    @MainThread
    fun of(activity: FragmentActivity): LifecycleModelStore {
        checkApplication(activity)
        return LifecycleModelStores.of(activity)
    }

}
