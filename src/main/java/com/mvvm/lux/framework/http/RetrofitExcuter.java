package com.mvvm.lux.framework.http;


import com.google.gson.GsonBuilder;
import com.mvvm.lux.framework.BaseApplication;
import com.mvvm.lux.framework.BuildConfig;
import com.mvvm.lux.framework.http.interceptor.CacheInterceptor;
import com.mvvm.lux.framework.http.interceptor.HttpLoggingInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Description 网络请求工具类 : 修改
 * @Author luxiao418
 * @Email luxiao418@pingan.com.cn
 * @Date 2016/12/20 18:36
 * @Version 1.1.0
 */
public class RetrofitExcuter {

    private static OkHttpClient mOkHttpClient;

    /**
     * okhttp全局配置,application初始化一次就可以了
     */
    public synchronized static void init() {

        //设置Http缓存
        BaseApplication context = BaseApplication.getAppContext();
        Cache cache = new Cache(new File(context.getCacheDir(), "http_cache"), 1024 * 1024 * 100);
        CacheInterceptor cacheInterceptor = new CacheInterceptor(context, String.valueOf(60 * 60 * 6)); //缓存 cache-control 值

        mOkHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(cacheInterceptor)
                .addNetworkInterceptor(cacheInterceptor)
                .addInterceptor(new HttpLoggingInterceptor("logger"))
                .retryOnConnectionFailure(true) //重试机制
                .build();
    }

    public synchronized static void initOk() {
        mOkHttpClient = new OkHttpBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true) //重试机制
                .addLog(BuildConfig.DEBUG) //用于输出网络请求和结果log的拦截器
                .addCache(true)
                .build();
        //.addCookie(true)    //持久化cookie
        //.addConverterFactory(MyGsonConverterFactory.create())
        //.addInterceptor(new RequestInterceptor())   //拦截所有请求url,添加全局参数和请求头
        //.addSSL(new String[]{}, new int[]{R.raw.geotrust}) //添加SSL证书
        //.addSSLSocketFactory(CusSSLSocketFactory.buildSSLSocketFactory(R.raw.geotrust))
    }

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static Retrofit.Builder create() {
        return new Retrofit.Builder().addConverterFactory(

                GsonConverterFactory.create(new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .serializeNulls()
                        .create()))  //Converter对Call<T>中的T转换
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())  //CallAdapter对Call转换为Rxjava的Observable
                .client(mOkHttpClient);
    }
}
