package com.mvvm.lux.framework.base;

import android.databinding.BaseObservable;

import java.io.Serializable;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @Description
 * @Author luxiao418
 * @Email luxiao418@pingan.com.cn
 * @Date 2016/11/16 17:09
 * @Version $VALUE
 */
public class BaseViewModel extends BaseObservable implements Serializable {

    protected CompositeSubscription mCompositeSubscription;

    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscribe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    public void detachView() {
        unSubscribe();
    }
}
