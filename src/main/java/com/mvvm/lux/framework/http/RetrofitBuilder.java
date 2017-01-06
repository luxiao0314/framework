package com.mvvm.lux.framework.http;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.mvvm.lux.framework.BaseApplication;
import com.mvvm.lux.framework.http.cookie.CookieManger;
import com.mvvm.lux.framework.http.interceptor.BaseInterceptor;
import com.mvvm.lux.framework.http.interceptor.CacheInterceptor;
import com.mvvm.lux.framework.http.interceptor.HttpLoggingInterceptor;
import com.mvvm.lux.framework.utils.CommonUtils;

import java.io.File;
import java.io.InputStream;
import java.net.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @Description retrofitHelper的builder类
 * @Author luxiao418
 * @Email luxiao418@pingan.com.cn
 * @Date 2016/12/20 18:24
 * @Version 1.0.1
 */
public class RetrofitBuilder {

    private static final int DEFAULT_TIMEOUT = 5;
    private static final int DEFAULT_MAXIDLE_CONNECTIONS = 5;
    private static final long DEFAULT_KEEP_ALIVEDURATION = 8;
    private static final long caheMaxSize = 100 * 1024 * 1024;

    private okhttp3.Call.Factory callFactory;
    private String baseUrl;
    private Boolean isLog = false;
    private Boolean isCookie = false;
    private Boolean isCache = false;
    private List<InputStream> certificateList;
    private HostnameVerifier hostnameVerifier;
    private CertificatePinner certificatePinner;
    private Executor callbackExecutor;
    private boolean validateEagerly;
    private Context context;
    private CookieManger cookieManager;
    private Cache cache = null;
    private Proxy proxy;
    private File httpCacheDirectory;
    private SSLSocketFactory sslSocketFactory;
    private ConnectionPool connectionPool;
    private Converter.Factory converterFactory;
    private CallAdapter.Factory callAdapterFactory;
    private Retrofit.Builder retrofitBuilder;
    private static OkHttpClient.Builder okHttpBuilder;

    public RetrofitBuilder() {
        okHttpBuilder = new OkHttpClient.Builder();
        retrofitBuilder = new Retrofit.Builder();       //retrofitBuilder的配置信息不能作为单例
        this.context = BaseApplication.getAppContext();
    }

    @NonNull
    public RetrofitBuilder client(OkHttpClient client) {
        retrofitBuilder.client(CommonUtils.checkNotNull(client, "client == null"));
        return this;
    }

    public RetrofitBuilder callFactory(okhttp3.Call.Factory factory) {
        this.callFactory = CommonUtils.checkNotNull(factory, "factory == null");
        return this;
    }

    public RetrofitBuilder connectTimeout(int timeout) {
        return connectTimeout(timeout, TimeUnit.SECONDS);
    }

    public RetrofitBuilder writeTimeout(int timeout) {
        return writeTimeout(timeout, TimeUnit.SECONDS);
    }

    public RetrofitBuilder readTimeout(int timeout) {
        return readTimeout(timeout, TimeUnit.SECONDS);
    }

    public RetrofitBuilder addLog(boolean isLog) {
        this.isLog = isLog;
        return this;
    }

    public RetrofitBuilder addCookie(boolean isCookie) {
        this.isCookie = isCookie;
        return this;
    }

    public RetrofitBuilder addCache(boolean isCache) {
        this.isCache = isCache;
        return this;
    }

    public RetrofitBuilder proxy(Proxy proxy) {
        okHttpBuilder.proxy(CommonUtils.checkNotNull(proxy, "proxy == null"));
        return this;
    }

    public RetrofitBuilder writeTimeout(int timeout, TimeUnit unit) {
        if (timeout != -1) {
            okHttpBuilder.writeTimeout(timeout, unit);
        } else {
            okHttpBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        }
        return this;
    }

    public RetrofitBuilder readTimeout(int timeout, TimeUnit unit) {
        if (timeout != -1) {
            okHttpBuilder.readTimeout(timeout, unit);
        } else {
            okHttpBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        }
        return this;
    }

    public RetrofitBuilder connectionPool(ConnectionPool connectionPool) {
        if (connectionPool == null) throw new NullPointerException("connectionPool == null");
        this.connectionPool = connectionPool;
        return this;
    }

    public RetrofitBuilder connectTimeout(int timeout, TimeUnit unit) {
        if (timeout != -1) {
            okHttpBuilder.connectTimeout(timeout, unit);
        } else {
            okHttpBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        }
        return this;
    }

    public RetrofitBuilder baseUrl(String baseUrl) {
        this.baseUrl = CommonUtils.checkNotNull(baseUrl, "init == null");
        return this;
    }

    public RetrofitBuilder addConverterFactory(Converter.Factory factory) {
        this.converterFactory = factory;
        return this;
    }

    public RetrofitBuilder addCallAdapterFactory(CallAdapter.Factory factory) {
        this.callAdapterFactory = factory;
        return this;
    }

    public RetrofitBuilder addHeader(Map<String, String> headers) {
        okHttpBuilder.addInterceptor(new BaseInterceptor((CommonUtils.checkNotNull(headers, "header == null"))));
        return this;
    }

    public RetrofitBuilder addParameters(Map<String, String> parameters) {
        okHttpBuilder.addInterceptor(new BaseInterceptor((CommonUtils.checkNotNull(parameters, "parameters == null"))));
        return this;
    }

    public RetrofitBuilder addInterceptor(Interceptor interceptor) {
        okHttpBuilder.addInterceptor(CommonUtils.checkNotNull(interceptor, "interceptor == null"));
        return this;
    }

    public RetrofitBuilder authenticator(Authenticator authenticator) {
        okHttpBuilder.authenticator(authenticator);
        return this;
    }

    public RetrofitBuilder callbackExecutor(Executor executor) {
        this.callbackExecutor = CommonUtils.checkNotNull(executor, "executor == null");
        return this;
    }

    public RetrofitBuilder validateEagerly(boolean validateEagerly) {
        this.validateEagerly = validateEagerly;
        return this;
    }

    public RetrofitBuilder cookieManager(CookieManger cookie) {
        if (cookie == null) throw new NullPointerException("cookieManager == null");
        this.cookieManager = cookie;
        return this;
    }

    public RetrofitBuilder addSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
        return this;
    }

    public RetrofitBuilder addHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }

    public RetrofitBuilder addCertificatePinner(CertificatePinner certificatePinner) {
        this.certificatePinner = certificatePinner;
        return this;
    }

    public RetrofitBuilder addSSL(String[] hosts, int[] certificates) { //certificates:证书
        if (hosts == null) throw new NullPointerException("hosts == null");
        if (certificates == null) throw new NullPointerException("ids == null");

        addSSLSocketFactory(HttpsFactroy.getSSLSocketFactory(context, certificates));
        addHostnameVerifier(HttpsFactroy.getHostnameVerifier(hosts));
        return this;
    }

    public RetrofitBuilder addNetworkInterceptor(Interceptor interceptor) {
        okHttpBuilder.addNetworkInterceptor(interceptor);
        return this;
    }

    public RetrofitBuilder addCache(Cache cache) {
        int maxStale = 60 * 60 * 24 * 3;
        return addCache(cache, maxStale);
    }

    public RetrofitBuilder addCache(Cache cache, final long cacheTime) {
        addCache(cache, String.valueOf(cacheTime));
        return this;
    }

    private RetrofitBuilder addCache(Cache cache, final String cacheControlValue) {
        CacheInterceptor cacheInterceptor = new CacheInterceptor(context, cacheControlValue);
        addNetworkInterceptor(cacheInterceptor);   //在线缓存:添加网络拦截器
        addInterceptor(cacheInterceptor);  //离线缓存:添加应用拦截器
        this.cache = cache;
        return this;
    }

    public RetrofitBuilder retryOnConnectionFailure(boolean retryOnConnectionFailure) {
        okHttpBuilder.retryOnConnectionFailure(retryOnConnectionFailure);
        return this;
    }

    public Retrofit build() {

        if (baseUrl == null) throw new IllegalStateException("Base URL required.");

        if (okHttpBuilder == null) throw new IllegalStateException("okHttpBuilder required.");

        if (retrofitBuilder == null) throw new IllegalStateException("retrofitBuilder required.");

        if (proxy != null)
            okHttpBuilder.proxy(proxy);

        if (httpCacheDirectory == null)
            httpCacheDirectory = new File(context.getCacheDir(), "http_cache");

        if (isCache)
            addCache(new Cache(httpCacheDirectory, caheMaxSize), caheMaxSize);

        if (cache != null)
            okHttpBuilder.cache(cache);

        if (isLog)
            okHttpBuilder.addInterceptor(new HttpLoggingInterceptor("logger_record"));

        if (isCookie && cookieManager == null)
            okHttpBuilder.cookieJar(new CookieManger(context));

        if (cookieManager != null)
            okHttpBuilder.cookieJar(cookieManager);

        if (callFactory != null)
            retrofitBuilder.callFactory(callFactory);

        if (callAdapterFactory == null)
            callAdapterFactory = RxJavaCallAdapterFactory.create();

        if (sslSocketFactory != null)
            okHttpBuilder.sslSocketFactory(sslSocketFactory);

        if (hostnameVerifier != null)
            okHttpBuilder.hostnameVerifier(hostnameVerifier);

        if (connectionPool == null)
            connectionPool = new ConnectionPool(DEFAULT_MAXIDLE_CONNECTIONS, DEFAULT_KEEP_ALIVEDURATION, TimeUnit.SECONDS);

        okHttpBuilder.connectionPool(connectionPool);

        if (converterFactory == null)
            converterFactory = GsonConverterFactory.create(new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .serializeNulls()
                    .create());

        retrofitBuilder.baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .client(HttpHolder.okHttpClient);

        return retrofitBuilder.build();
    }

    /**
     * OkHttpClient使用单例,程序中会使用OkHttp做一些其他请求操作，比如下载、上传等网络操作，就可用共用一个OkHttpClient
     * <p>
     * 同时缓存也可以只使用一个:解决了log日志拦截器,重复打印很多遍的问题
     */
    private static class HttpHolder {
        static OkHttpClient okHttpClient = okHttpBuilder.build();
    }

}