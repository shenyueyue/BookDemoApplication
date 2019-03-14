package com.example.a10132.bookdemoapplication;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.example.a10132.bookdemoapplication.widget.PtrClassicFrameLayout;
import com.example.a10132.bookdemoapplication.widget.PtrDefaultHandler;
import com.example.a10132.bookdemoapplication.widget.PtrFrameLayout;
import com.example.a10132.bookdemoapplication.widget.PtrHandler;

public class MainActivity extends AppCompatActivity {
    private PtrClassicFrameLayout mPtrFrame;
    private WebView mWebView;
    private RelativeLayout toprl;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView)findViewById(R.id.main_webview);
        mPtrFrame= (PtrClassicFrameLayout) findViewById(R.id.main_pcfl);
        toprl = (RelativeLayout)findViewById(R.id.main_top);
        RelativeLayout findrl = (RelativeLayout)findViewById(R.id.main_find_rl);
        RelativeLayout bookrl = (RelativeLayout)findViewById(R.id.main_book_rl);
        RelativeLayout myrl = (RelativeLayout)findViewById(R.id.main_my_rl);
        findrl.setOnClickListener(new MyListener());
        myrl.setOnClickListener(new MyListener());
        WebSettings webSettings =   mWebView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);//支持js脚本
        webSettings.setDomStorageEnabled(true);////设置DOM Storage缓存，不然插件出不来
        mWebView.setWebViewClient(new WebViewClient());//
        mWebView.setWebChromeClient(new WebChromeClient());//用chrome浏览器
        mWebView.loadUrl("file:///android_asset/pages/index.html");
        initView();
        //hidestatusbar();
    }
    /**
     * 定义底栏点击事件
     */
    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.main_find_rl:
                    i = 0;
                    updateData();
                    toprl.setVisibility(View.VISIBLE);
                    break;
                case R.id.main_my_rl:
                    i =1;
                    updateData();
                    toprl.setVisibility(View.GONE);
                    break;
                case R.id.main_book_rl:

                    break;
            }
        }
    }
    private void initView(){
        mWebView.setWebViewClient(new WebViewClient() {

//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if(url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://"))
//                {
//                    return false;
//                }
//                return true;
//            }
            @Override
            public void onPageFinished(WebView view, String url) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                }, 1000);
            }

        });
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mWebView, header);
            }
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                updateData();
            }
        });
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        mPtrFrame.setPullToRefresh(false);
        mPtrFrame.setKeepHeaderWhenRefresh(true);

        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 1000);
    }
    private void updateData() {
        if(i == 0){
            mWebView.loadUrl("file:///android_asset/pages/index.html");
        }else if(i == 1){
            mWebView.loadUrl("file:///android_asset/pages/me.html");
        }

    }
    /**
     * 隐藏状态栏
     */
    private void hidestatusbar(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
}
