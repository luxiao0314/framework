package com.mvvm.lux.framework.http;


import com.mvvm.lux.framework.http.base.BaseResponse;
import com.mvvm.lux.framework.http.exception.RetrofitException;
import com.mvvm.lux.framework.http.exception.RetryWhenNetworkException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Jam on 16-6-12
 * Description: Rx 一些巧妙的处理
 */
public class RxHelper {

    /**
     * 异常处理变压器
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<BaseResponse<T>, T> handleErrTransformer() {
        return new Observable.Transformer<BaseResponse<T>, T>() {
            @Override
            public Observable<T> call(Observable<BaseResponse<T>> observable) {
                return observable.flatMap(new <T>HandleResultFuc())    //将RxSubscriber中服务器异常处理换到这里,在RxSubscriber中处理onstart(),onCompleted().onError,onNext()
                        .compose(schedulersTransformer()) //处理线程切换,注销Observable
                        .onErrorResumeNext(new HttpResponseFunc<T>())//判断异常
                        .retryWhen(new RetryWhenNetworkException());
//                        .retryWhen(new TimeOutRetry())  //token过期的重试,有问题

            }
        };
    }

    /**
     * 根据responseCode,处理异常,类似于捕获了异常
     *
     * @param <T>
     */
    private static class HttpResponseFunc<T> implements Func1<Throwable, Observable<T>> {
        @Override
        public Observable<T> call(Throwable t) {
            return Observable.error(RetrofitException.handleException(t));
        }
    }

//    /**
//     * token过期,登录超时的重新连接
//     */
//    public static class TimeOutRetry implements Func1<Observable<? extends Throwable>, Observable> {
//
//        @Override
//        public Observable call(Observable<? extends Throwable> observable) {
//            return observable.flatMap(new Func1<Throwable, Observable<?>>() {
//                @Override
//                public Observable<?> call(Throwable throwable) {
//                    if (throwable instanceof TimeoutException) {
//                        return RetrofitExcuter.init("")
//                                .getLiveIndex()
//                                .doOnSubscribe(new Action0() {
//                                    @Override
//                                    public void call() {
//
//                                    }
//                                });
//                    }
//                    return Observable.error(throwable);
//                }
//            });
//        }
//    }

    /**
     * 调度器,切换线程和注销Observable
     */
    public static <T> Observable.Transformer<T, T> schedulersTransformer() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> io_main() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 生成Observable
     *
     * @param <T>
     * @return
     */
    public static <T> Observable<T> createData(final T t) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}