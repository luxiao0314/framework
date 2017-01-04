package com.mvvm.lux.framework.http;


import com.mvvm.lux.framework.http.interceptor.UserAgentInterceptor;

import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;

/**
 * @Description 网络请求工具类
 * @Author luxiao418
 * @Email luxiao418@pingan.com.cn
 * @Date 2016/12/20 18:36
 * @Version 1.1.0
 */
public class RetrofitExcuter {

    public static Retrofit baseUrl(String baseUrl) {
        return new RetrofitBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true) //重试机制
                .addLog(true) //用于输出网络请求和结果log的拦截器
                .addCache(true)
                .addNetworkInterceptor(new UserAgentInterceptor())
                //.addCookie(true)    //持久化cookie
                //.addInterceptor(new RequestInterceptor())   //拦截所有请求url,添加全局参数和请求头
                //.addSSL(new String[]{}, new int[]{R.raw.geotrust}) //添加SSL证书
                //.addSSLSocketFactory(CusSSLSocketFactory.buildSSLSocketFactory(R.raw.geotrust))
                .baseUrl(baseUrl)
                .build();
    }
}
