package com.mvvm.lux.framework.config;

import com.mvvm.lux.framework.http.RxSubscriber;
import com.mvvm.lux.framework.rx.RxBus;
import com.orhanobut.hawk.Hawk;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Jam on 16-7-6
 * Description:
 * RxJava + Retrofit 的缓存机制
 */
public class RxCache {
    private static String cacheKey;//缓存key

    /**
     * @param fromNetwork  从网络获取的Observable
     * @param forceRefresh 是否强制刷新
     * @param <T>
     * @return
     */
    public static <T> Observable<T> load(Observable<T> fromNetwork, final boolean forceRefresh) {
        //从拦截器获取的url
        RxBus.init().toObservable(PassUrlEvent.class)
                .subscribe(new RxSubscriber<PassUrlEvent>() {
                    @Override
                    public void onNext(PassUrlEvent passUrlEvent) {
                        cacheKey = passUrlEvent.url;
                    }
                });

        Observable<T> fromCache = Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                T cache = Hawk.get(cacheKey);
                if (cache != null) {
                    subscriber.onNext(cache);
                } else {
                    subscriber.onCompleted();
                }
            }
        });


        /**
         * 这里的fromNetwork 不需要指定Schedule,在handleRequest中已经变换了
         */
        fromNetwork = fromNetwork.map(new Func1<T, T>() {
            @Override
            public T call(T result) {
                Hawk.put(cacheKey, result); //保存数据
                return result;
            }
        });
        if (forceRefresh) {
            return fromNetwork;
        } else {
            return Observable.concat(fromCache, fromNetwork).first();
        }

        /*//从本地读取的数据
        final Observable<T> fromCache = Observable.just(true)
                .map(new Func1<Boolean, T>() {
                    @Override
                    public T call(Boolean aBoolean) {
                        return Hawk.get(cacheKey);
                    }
                });

        if (!NetworkUtil.isNetworkAvailable()) {
            return fromCache;
        }

        if (forceRefresh) {
            return fromNetwork;
        }

        //从网络取数据,这里的fromNetwork 不需要指定Schedule,在handleRequest中已经变换了
        return fromNetwork.flatMap(new Func1<T, Observable<T>>() {

            @Override
            public Observable<T> call(T result) {
                Hawk.put(cacheKey, result); //保存数据
                //concat:首先取缓存,如果缓存有数据就使用缓存,如果没有就使用网络(如何使得哪层有数据就用哪层的，之后就不走后面的逻辑了。)
                return Observable.concat(fromCache, fromNetwork)
                        .takeFirst(new Func1<T, Boolean>() {
                            @Override
                            public Boolean call(T t) {
                                return t != null;
                            }
                        })
                        .doOnNext(new Action1<T>() {
                            @Override
                            public void call(T t) {
                                HandleResultFuc.createData(t);
                            }
                        });
            }
        });*/
    }
}