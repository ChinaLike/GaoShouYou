package com.touchrom.gaoshouyou.activity.web;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.databinding.ActivityBaseWebBinding;
import com.touchrom.gaoshouyou.entity.WebEntity;
import com.touchrom.gaoshouyou.widget.TempView;

import butterknife.InjectView;

/**
 * Created by lk on 2015/12/3.
 * webView基类
 */
public class BaseWebView extends BaseActivity<ActivityBaseWebBinding> {
    private static final int SHOW = 0;
    private static final int HIDE = 1;
    @InjectView(R.id.webView)
    WebView mWebView;
    private WebEntity mWebViewEntity;
    private WebSettings mWebSetting;
    private int mPackageNum = 0;
    private boolean isBack = false;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_base_web;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mWebViewEntity = getIntent().getParcelableExtra(Constance.KEY.PARCELABLE_ENTITY);
        initWidget();
    }

    private void initWidget() {
        mWebView.setWebViewClient(getWebViewClient());
        mWebSetting = mWebView.getSettings();
        mWebSetting.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new WebAppInterface(this), "GaoShouYou");
        mWebView.setWebChromeClient(getWebChromeClient());
        loadUrl(mWebViewEntity.getContentUrl());
        String title = mWebViewEntity.getTitle();
        mToolbar.setTitle(TextUtils.isEmpty(title) ? "网页浏览" : title);
    }

    /**
     * 给子类提供加载网页的接口
     *
     * @param url
     */
    protected void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    public WebView getmWebView() {
        return mWebView;
    }

    public WebSettings getmWebSetting() {
        return mWebSetting;
    }

    public void setmWebSetting(WebSettings mWebSetting) {
        this.mWebSetting = mWebSetting;
    }

    private WebChromeClient getWebChromeClient() {
        return new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        };
    }

    protected void back() {
//        if (mPackageNum == 0){
//            mToolbar.setName("关闭");
//        }else {
//            mToolbar.setName(mWebViewEntity.getName());
//        }
        if (mWebView.canGoBack() && mPackageNum > 1) {
            mWebView.goBack();
            mPackageNum--;
            isBack = true;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private WebViewClient getWebViewClient() {
        return new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mPackageNum++;
                showWebLoadingDialog();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                isBack = false;
                dismissWebLoadDialog();
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                dismissWebLoadDialog();
            }
        };
    }

    @Override
    public void finish() {
        super.finish();
        dismissWebLoadDialog();
    }

    private void showWebLoadingDialog() {
        if (!isBack) {
            showTempView(TempView.LOADING);
        }
    }

    private void dismissWebLoadDialog() {
        hintTempView();
    }
}
