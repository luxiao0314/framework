package com.mvvm.lux.framework.manager;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.mvvm.lux.framework.http.RetrofitExcuter;
import com.mvvm.lux.framework.utils.Logger;

import io.realm.Realm;

/**
 * @Description
 * @Author luxiao418
 * @Email luxiao418@pingan.com.cn
 * @Date 28/06/2017 18:53
 * @Version
 */
public class IActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        IActivityManager.instance.registerActivity(activity);
        setTranslucentStatus(activity,false);
        Logger.d(activity.getClass().getName(), activity.getClass().getName() + "------>onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Logger.d(activity.getClass().getName(), activity.getClass().getName() + "------>onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        IActivityManager.instance.setCurrentActivityWeakRef(activity);
        Logger.d(activity.getClass().getName(), activity.getClass().getName() + "------>onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Logger.d(activity.getClass().getName(), activity.getClass().getName() + "------>onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Logger.d(activity.getClass().getName(), activity.getClass().getName() + "------>onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Logger.d(activity.getClass().getName(), activity.getClass().getName() + "------>onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        IActivityManager.instance.unregisterActivity(activity);
        if (!Realm.getDefaultInstance().isClosed()) {
            Realm.getDefaultInstance().close();
        }
        RetrofitExcuter.getOkHttpClient().dispatcher().cancelAll();
        Logger.d(activity.getClass().getName(), activity.getClass().getName() + "------>onActivityDestroyed");
    }

    /**
     * 设置沉浸式
     *
     * @param activity
     * @param on
     */
    protected void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
