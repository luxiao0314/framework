package com.mvvm.lux.framework.http;


import java.util.concurrent.TimeUnit;

/**
 * @Description 网络请求工具类
 * @Author luxiao418
 * @Email luxiao418@pingan.com.cn
 * @Date 2016/12/20 18:36
 * @Version 1.0.0
 */
public class RetrofitExcuter {

    /**
     * 初始化 Okhttp配置
     * @return
     */
    public static RetrofitBuilder init() {
        return new RetrofitBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true) //重试机制
                .addLog(true) //用于输出网络请求和结果log的拦截器
                .addCache(true);
                //.addCookie(true)    //持久化cookie
                //.addConverterFactory(MyGsonConverterFactory.create())
                //.addInterceptor(new RequestInterceptor())   //拦截所有请求url,添加全局参数和请求头
                //.addSSL(new String[]{}, new int[]{R.raw.geotrust}) //添加SSL证书
                //.addSSLSocketFactory(CusSSLSocketFactory.buildSSLSocketFactory(R.raw.geotrust))
    }
}
