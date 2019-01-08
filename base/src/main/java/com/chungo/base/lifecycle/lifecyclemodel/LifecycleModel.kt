package com.chungo.base.lifecycle.lifecyclemodel

import android.app.Activity
import android.support.v4.app.Fragment


/**
 * 参考google 推荐的mvvm架构中的关键v部分
 * [LifecycleModelProviders] 配合 [LifecycleModel] 的实现类可以帮助 [Activity] 和 [Fragment]
 * 储存存和管理一些与 UI 相关以及他们必需的数据, 并且这些数据在屏幕旋转或配置更改引起的 [Activity] 重建的情况下也会被保留, 直到最后被 finish
 * 但是 [LifecycleModel] 的实现类切勿直接引用 [Activity] 和 [Fragment] 以及他们里面 UI 元素
 *
 *
 * 他还可以让开发者能够在绑定在同一个 [Activity] 之下的多个不同的 [Fragment] 之间通讯以及共享数据 (Activity 与 Fragmnet 之间的通讯以及共享数据也可以)
 * 使用这种方式进行通讯, [Fragment] 之间不存在任何耦合关系
 *
 *
 * [LifecycleModel] 可以用来存储以及管理数据, 也可以作为 MVP 模式中的 Presenter (只要实现 [me.jessyan.lifecyclemodel.LifecycleModel] 即可)
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
interface LifecycleModel {
    /**
     * 这个方法会在宿主 [Activity] 或 [Fragment] 被彻底销毁时被调用, 在这个方法中释放一些资源可以避免内存泄漏
     */
    fun onCleared()
}
