package org.liumingyi.swiper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 专门用于展示web的Activity<br>
 *
 * Created by liumingyi on 2017/3/31.
 */

public class WebActivity extends AppCompatActivity {

  public static final String INTENT_KEY_URL = "intent_key_url";

  WebView webView;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_web);
    webView = (WebView) findViewById(R.id.web_activity_web_view);
    Intent intent = getIntent();
    String url = intent.getStringExtra(INTENT_KEY_URL);
    initWebView();
    loadUrl(url);
  }

  private void loadUrl(String url) {
    if (TextUtils.isEmpty(url)) {
      //todo show error view
      return;
    }
    webView.loadUrl(url);
  }

  private void initWebView() {
    webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    webView.getSettings().setDefaultTextEncodingName("utf-8");// 避免中文乱码
    webView.setHorizontalScrollBarEnabled(false);// 水平不显示
    webView.setVerticalScrollBarEnabled(false); // 垂直不显示

    WebSettings settings = webView.getSettings();
    //支持获取手势焦点，输入用户名、密码或其他
    //webView.requestFocusFromTouch();
    settings.setJavaScriptEnabled(true);    //支持javascript
    settings.setUseWideViewPort(true);    //设置webview推荐使用的窗口，使html界面自适应屏幕
    //settings.setLoadWithOverviewMode(true);     //缩放至屏幕的大小
    //settings.setAllowFileAccess(true);      //设置可以访问文件
    settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);    //设置中等像素密度，medium=160dpi
    settings.setSupportZoom(true);    //设置支持缩放
    settings.setLoadsImagesAutomatically(true);    //设置自动加载图片
    //settings.setBlockNetworkImage(true);    //设置网页在加载的时候暂时不加载图片
    //settings.setAppCachePath("");   //设置缓存路径
    settings.setCacheMode(WebSettings.LOAD_NO_CACHE);   //设置缓存模式
    webView.setWebViewClient(new ZhWebViewClient());
  }

  private class ZhWebViewClient extends WebViewClient {

    @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
      view.loadUrl(url);
      return true;
    }
  }
}
