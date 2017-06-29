package com.mvvm.lux.framework.manager;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * @Description activity管理类，主要用来获取当前的运行实例,对于跳转实用
 * @Author louyaming802
 * @Date 17/4/27
 * @Version 2.5.X
 */

public class CusActivityManager {
    private static CusActivityManager instance = new CusActivityManager();

    private WeakReference<Activity> mCurrentActivityWeakRef;

    private CusActivityManager(){

    }

    public static CusActivityManager getInstance(){
        return instance;
    }

    public Activity getCurrentActivity() {
        Activity currentActivity = null;

        if (null != mCurrentActivityWeakRef){
            currentActivity = mCurrentActivityWeakRef.get();
        }

        return currentActivity;
    }

    public void setCurrentActivityWeakRef(Activity activity){
        mCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }



}
