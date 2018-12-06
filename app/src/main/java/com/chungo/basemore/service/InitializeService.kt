package com.chungo.basemore.service

import android.app.IntentService
import android.content.Context
import android.content.Intent

/**
 * 在子线程中完成其他初始化
 */
class InitializeService : IntentService("InitializeService") {

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            if (ACTION_INIT == action) {
                initApplication()
            }
        }
    }

    private fun initApplication() {
        //初始化日志
        //Logger.init(getPackageName()).hideThreadInfo();

        //初始化错误收集
        //        CrashHandler.init(new CrashHandler(getApplicationContext()));
        initBugly()

        //初始化内存泄漏检测
        //LeakCanary.install(App.getInstance());

        //初始化过度绘制检测
        //BlockCanary.install(getApplicationContext(), new AppBlockCanaryContext()).start();

    }

    private fun initBugly() {
        //        Context context = getApplicationContext();
        //        String packageName = context.getPackageName();
        //        String processName = SystemUtil.getProcessName(android.os.Process.myPid());
        //        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        //        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        //        CrashReport.initCrashReport(context, Constants.BUGLY_ID, isDebug, strategy);
    }

    companion object {

        private val ACTION_INIT = "initApplication"

        fun start(context: Context) {
            val intent = Intent(context, InitializeService::class.java)
            intent.action = ACTION_INIT
            context.startService(intent)
        }
    }
}
