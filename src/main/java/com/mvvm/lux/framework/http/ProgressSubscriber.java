package com.mvvm.lux.framework.http;

import android.support.v4.app.DialogFragment;

import com.mvvm.lux.framework.manager.dialogs.DialogManager;
import com.mvvm.lux.framework.manager.dialogs.config.BaseTask;
import com.mvvm.lux.framework.manager.dialogs.interfaces.ProgressCancelListener;

/**
 * @Description 带progress的subscriber
 * 1,dialog取消的时候需要取消网络请求
 * //TODO 2,请求和生命周期绑定,activity销毁的时候取消网络请求
 * @Author luxiao418
 * @Email luxiao418@pingan.com.cn
 * @Date 2016/12/16 20:38
 * @Version
 */
public abstract class ProgressSubscriber<T> extends RxSubscriber<T> implements ProgressCancelListener {

    private BaseTask mServiceTask;
    private DialogFragment mDialogFragment;
    private boolean isCache;

    public ProgressSubscriber(BaseTask serviceTask) {
        if (serviceTask != null)
            serviceTask.setOnCancelProgress(this);
        mServiceTask = serviceTask;
    }

    @Override
    public void onStart() {
        super.onStart();
        showProgressDialog();
         /*缓存并且有网*/
        /*if (isCache && NetworkUtil.isNetworkAvailable()) {
             *//*获取缓存数据*//*
            CookieResulte cookieResulte = CookieDbUtil.getInstance().queryCookieBy(api.getUrl());
            if (cookieResulte != null) {
                long time = (System.currentTimeMillis() - cookieResulte.getTime()) / 1000;
                if (time < api.getCookieNetWorkTime()) {
                    onCacheNext(cookieResulte.getResulte());
                    onCompleted();
                    unsubscribe();
                }
            }
        }*/
    }

    private void onCacheNext(String resulte) {

    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        dismissProgressDialog();
        /*需要緩存并且本地有缓存才返回*/
       /* if (isCache) {
            Observable.just(api.getUrl()).subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    errorDo(e);
                }

                @Override
                public void onNext(String s) {
                    *//*获取缓存数据*//*
                    CookieResulte cookieResulte = CookieDbUtil.getInstance().queryCookieBy(s);
                    if (cookieResulte == null) {
                        throw new HttpTimeException("网络错误");
                    }
                    long time = (System.currentTimeMillis() - cookieResulte.getTime()) / 1000;
                    if (time < api.getCookieNoNetWorkTime()) {
                        if (mSubscriberOnNextListener.get() != null) {
                            mSubscriberOnNextListener.get().onCacheNext(cookieResulte.getResulte());
                        }
                    } else {
                        CookieDbUtil.getInstance().deleteCookie(cookieResulte);
                        throw new HttpTimeException("网络错误");
                    }
                }
            });
        } else {
            errorDo(e);
        }*/
    }

    private void showProgressDialog() {
        //不知道为什么activity会destroy了, 所以这里一直报错
        if (mDialogFragment == null && mServiceTask != null && !mServiceTask.getContext().isDestroyed()) {
            mDialogFragment = DialogManager.showProgressDialog(mServiceTask);
        } else {
            dismissProgressDialog();
        }
    }

    private void dismissProgressDialog() {
        if (mDialogFragment != null) {
            mDialogFragment.dismissAllowingStateLoss();
        }
    }

    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}
