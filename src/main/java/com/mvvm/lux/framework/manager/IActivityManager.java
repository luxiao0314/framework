package com.mvvm.lux.framework.manager;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * @Description activity管理类，主要用来获取当前的运行实例,对于跳转实用
 * @Author louyaming802
 * @Date 17/4/27
 * @Version 2.5.X
 */

public class IActivityManager {
    /**
     * 应用创建activity集合 在创建activity时调用addActivity方法将新创建的活动添加到集合中
     */
    private static LinkedList<Activity> activitys = new LinkedList<>();

    public static IActivityManager instance = new IActivityManager();

    private WeakReference<Activity> mCurrentActivityWeakRef;

    private IActivityManager(){

    }

    public Activity currentActivity() {
        Activity currentActivity = null;
        if (null != mCurrentActivityWeakRef){
            currentActivity = mCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    void  setCurrentActivityWeakRef(Activity activity){
        mCurrentActivityWeakRef = new WeakReference<>(activity);
    }

    /**
     * 添加活动到应用活动集合中
     *
     * @param activity 活动对象
     */
    public void registerActivity(Activity activity) {
        if (null != activitys) {
            activitys.add(activity);
        }
    }

    /**
     * 结束活动集合中的一个对象
     *
     * @param activity
     */
    public void unregisterActivity(Activity activity) {
        if (null != activitys && null != activity) {
            int position = activitys.indexOf(activity);
            if (position >= 0) {
                activitys.remove(position);
            }
            activity.finish();
        }
    }

    /**
     * 结束活动的Activity
     */
    public void exitApp() {
        if (null != activitys) {
            synchronized (activitys) {
                for (Activity activity : activitys) {
                    activity.finish();
                }
            }
            activitys.clear();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 结束最后一个活动之前的Activity
     */
    public static void killBeforeActivitys() {
        if (null != activitys) {
            int size = activitys.size() - 1;
            for (int i = 0; i < size; i++) {
                Activity activity = activitys.get(i);
                activity.finish();
            }
            System.out.println(activitys.size());
        }
    }

    /**
     * 获取队列中最后一个activity
     *
     * @return ACTIVITY
     */
    public static Activity lastActivity() {
        Activity activity = null;
        if (null != activitys) {
            activity = activitys.getLast();
        }
        return activity;
    }
}
