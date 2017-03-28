package com.mvvm.lux.framework.manager.hybrid;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.ClientCertRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mvvm.lux.framework.utils.NetworkUtil;


/**
 * Created by jingbin on 2016/11/17.
 * 监听网页链接:
 * - 优酷视频直接跳到自带浏览器
 * - 根据标识:打电话、发短信、发邮件
 * - 进度条的显示
 * - 添加javascript监听
 */
public class MyWebViewClient extends WebViewClient {

    private IWebPageView mIWebPageView;
    private BrowserActivity mActivity;

    public MyWebViewClient(IWebPageView mIWebPageView) {
        this.mIWebPageView = mIWebPageView;
        mActivity = (BrowserActivity) mIWebPageView;
        mIWebPageView.startProgress();  //在shouldOverrideUrlLoading中并不执行,待优化
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        // 优酷视频跳转浏览器播放
        if (url.startsWith("http://v.youku.com/")) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addCategory("android.intent.category.BROWSABLE");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            mActivity.startActivity(intent);
            return true;

            // 电话、短信、邮箱
        } else if (url.startsWith(WebView.SCHEME_TEL) || url.startsWith("sms:") || url.startsWith(WebView.SCHEME_MAILTO)) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mActivity.startActivity(intent);
            } catch (ActivityNotFoundException ignored) {
            }
            return true;
        }
        view.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        if (url.contains("dmzjimage://?src=")) {
            return true;
        }
        if (url.contains("dmzjandroid:")) {
            url = url.replace("dmzjandroid:", "");
        }
//        if (url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".png") || url.endsWith(".JPG") || url.endsWith(".JPEG") || url.endsWith(".PNG")) {
//            ImagePicsListActivity.entryGallery(view.getContext(), url, 0);
//            return true;
//        }
        view.loadUrl(url);
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (mActivity.mProgress90) {
            mIWebPageView.hindProgressBar();
        } else {
            mActivity.mPageFinish = true;
        }
        if (!NetworkUtil.isNetworkAvailable()) {
            mIWebPageView.hindProgressBar();
        }
        view.getSettings().setBlockNetworkImage(false);
        // html加载完成之后，添加监听图片的点击js函数
        mIWebPageView.addImageClickListener();
        super.onPageFinished(view, url);
    }

    // 视频全屏播放按返回页面被放大的问题
    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
        if (newScale - oldScale > 7) {
            view.setInitialScale((int) (oldScale / newScale * 100)); //异常放大，缩回去。
        }
    }

    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        super.onReceivedClientCertRequest(view, request);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
        handler.proceed();  //支持https,还有证书需要验证
    }
}
