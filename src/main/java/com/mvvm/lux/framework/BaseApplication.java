package com.mvvm.lux.framework;

import android.app.Application;
import android.os.Process;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mvvm.lux.framework.http.RetrofitExcuter;
import com.mvvm.lux.framework.http.fresco.ImageLoaderConfig;
import com.mvvm.lux.framework.manager.IActivityLifecycleCallbacks;
import com.orhanobut.hawk.Hawk;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.rx.RealmObservableFactory;

/**
 * Created by WangChao on 2016/10/12.
 */

public class BaseApplication extends Application {

    public static BaseApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        init();
        registerActivityLifecycleCallbacks(new IActivityLifecycleCallbacks());
    }

    private void init() {
       new Thread(new Runnable() {
           @Override
           public void run() {
               //设置线程优先级,不与主线程抢资源
               Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
               //初始化retrofit
               RetrofitExcuter.init();
               //初始化fresco
               Fresco.initialize(getAppContext(), ImageLoaderConfig.getImagePipelineConfig(getAppContext()));
               //初始化realm
               initRealm();
               //初始化缓存
               Hawk.init(sInstance).build();

               //蒲公英crash上报
               //PgyCrashManager.register(this);
               //初始化内存泄漏检测
               //LeakCanary.install(this);
               //初始化过度绘制检测
               //BlockCanary.install(this, new AppBlockCanaryContext()).start();
           }
       }).start();
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("lux.realm")
                .schemaVersion(1)
                .rxFactory(new RealmObservableFactory())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static synchronized BaseApplication getAppContext() {
        return sInstance;
    }
}
