package com.mvvm.lux.framework.http;


import com.google.gson.Gson;
import com.mvvm.lux.framework.BaseApplication;
import com.mvvm.lux.framework.http.base.BaseResponse;
import com.mvvm.lux.framework.http.exception.RetrofitException;
import com.mvvm.lux.framework.http.exception.RetryWhenNetworkException;
import com.mvvm.lux.framework.utils.FileUtil;

import rx.Observable;
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
                        .compose(io_main()) //处理线程切换,注销Observable
                        .onErrorResumeNext(new HttpResponseFunc<T>())//判断异常
                        .retryWhen(new RetryWhenNetworkException());
//                        .retryWhen(new TimeOutRetry())  //token过期的重试,有问题
            }
        };
    }

    /**
     * 使用假数据
     *
     * @param virtualData
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<BaseResponse<T>, T> handleVirtualData(final String virtualData) {
        return new Observable.Transformer<BaseResponse<T>, T>() {
            @Override
            public Observable<T> call(Observable<BaseResponse<T>> observable) {
                return (Observable<T>) observable.flatMap(new Func1<BaseResponse<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseResponse<T> tBaseResponse) {
                        String filepath = "virtualdata" + "/" + virtualData;
                        String response = FileUtil.getJson(BaseApplication.getAppContext(), filepath);
                        Gson gson = new Gson();
                        T data = (T) gson.fromJson(response, tBaseResponse.getData().getClass());
                        return HandleResultFuc.createData(data);
                    }
                }).compose(io_main());
            }
        };
    }

    public static <T> Observable.Transformer<T, T> handleVirtualDatas() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return (Observable<T>) observable.flatMap(new Func1<T, Observable<T>>() {
                    @Override
                    public Observable<T> call(T tBaseResponse) {
                        String filepath = "virtualdata" + "/" + tBaseResponse.getClass().getSimpleName();
                        String response = FileUtil.getJson(BaseApplication.getAppContext(), filepath);
                        Gson gson = new Gson();
                        T data = (T) gson.fromJson(response, tBaseResponse.getClass());
                        return HandleResultFuc.createData(data);
                    }
                }).compose(io_main());
            }
        };
    }

    /**
     * 异常处理变压器
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> handleErr() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return (Observable<T>) observable
                        .compose(io_main()) //处理线程切换,注销Observable
                        .onErrorResumeNext(new HttpResponseFunc<T>());//判断异常
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

    /**
     * 调度器,切换线程和注销Observable
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> io_main() {
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

}