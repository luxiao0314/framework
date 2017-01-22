package com.mvvm.lux.framework.base;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import com.mvvm.lux.framework.config.FinishEvent;
import com.mvvm.lux.framework.manager.router.Router;
import com.mvvm.lux.framework.rx.RxBus;

import java.io.Serializable;

import rx.Subscription;
import rx.functions.Action1;
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

    public ObservableField<String> title = new ObservableField<>();

    public ObservableBoolean downloadImg = new ObservableBoolean(true);

    public Activity mActivity;

    public BaseViewModel(Activity activity) {
        mActivity = activity;
        initEvent();
    }

    private void initEvent() {
        RxBus.init()
                .toObservableSticky(FinishEvent.class)
                .subscribe(new Action1<FinishEvent>() {
                    @Override
                    public void call(FinishEvent finishEvent) {
                        detachView();
                    }
                });
    }

    public View.OnClickListener back = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Router.pop(mActivity);
        }
    };

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
