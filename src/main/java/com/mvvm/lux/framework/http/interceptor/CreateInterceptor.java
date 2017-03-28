package com.mvvm.lux.framework.http.interceptor;


import com.mvvm.lux.framework.http.exception.ServerException;
import com.mvvm.lux.framework.utils.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class CreateInterceptor implements Interceptor {
    public static final int HTTP_CODE_ACCEPT = 202;                     //请求成功，但没有处理

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());  //如果401了，会先执行TokenAuthenticator
        Logger.e("CreateInterceptor request url " + response.request().url());
        Logger.e("CreateInterceptor  response code " + response.code());
        switch (response.code()) {
            case HTTP_CODE_ACCEPT:
                throw new ServerException(HTTP_CODE_ACCEPT, response.header("Retry-After"));
            case 401:   //返回为token失效
                throw new ServerException(401, response.message());
            case 403:   //绑卡失败
                throw new ServerException(403, response.message());
        }
        return response;
    }
}