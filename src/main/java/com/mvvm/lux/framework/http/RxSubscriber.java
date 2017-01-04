package com.mvvm.lux.framework.http;

import com.mvvm.lux.framework.utils.SnackbarUtil;

import rx.Subscriber;

/**
 * NovateSubscriber
 *
 * @param <T>
 */
public abstract class RxSubscriber<T> extends Subscriber<T> {

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        SnackbarUtil.showMessage(e.getMessage());
    }
}