package com.mvvm.lux.framework.http;

import com.mvvm.lux.framework.utils.Logger;
import com.mvvm.lux.framework.utils.ToastUtil;

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
//        SnackbarUtil.showMessage(e.getMessage());
        ToastUtil.showToast(e.getMessage());
        Logger.e(e.getMessage());
    }
}