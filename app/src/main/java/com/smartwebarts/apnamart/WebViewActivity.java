package com.smartwebarts.apnamart;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.utils.Toolbar_Set;

public class WebViewActivity extends AppCompatActivity {

    public static final String DATA = "data";
    public static final String TITLE = "title";
    private String url, title;
    private WebView webview;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        url = getIntent().getExtras().getString(DATA, "");
        title = getIntent().getExtras().getString(TITLE, "");
        Toolbar_Set.INSTANCE.setToolbar(this, title);
        Toolbar_Set.INSTANCE.setBottomNav(this);
        webview = findViewById(R.id.webview);
        progressBar = (ProgressBar)findViewById(R.id.progress);

        Sprite doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {
            webview.setWebViewClient(new MyBrowser());
            webview.getSettings().setLoadsImagesAutomatically(true);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setAllowFileAccess(true);
            webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webview.clearCache(true);
            webview.loadUrl(url);
        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(this);
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()){
            webview.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toolbar_Set.INSTANCE.getCartList(this);
    }
}