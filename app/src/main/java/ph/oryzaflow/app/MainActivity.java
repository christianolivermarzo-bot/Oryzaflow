package ph.oryzaflow.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String DASHBOARD = "http://192.168.4.1";
    private WebView webView;
    private TextView status;

    @SuppressLint("SetJavaScriptEnabled")
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        immersive();

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(Color.rgb(7,83,63));
        webView = new WebView(this);
        status = new TextView(this);
        status.setTextColor(Color.WHITE);
        status.setTextSize(22);
        status.setGravity(android.view.Gravity.CENTER);
        status.setPadding(32,32,32,32);
        status.setText("Connecting to Oryza Flow 2.0…

Connect this phone to:
Oryza-AP
Password: oryzaflow20");

        root.addView(webView, new FrameLayout.LayoutParams(-1,-1));
        root.addView(status, new FrameLayout.LayoutParams(-1,-1));
        setContentView(root);

        WebSettings s=webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setCacheMode(WebSettings.LOAD_NO_CACHE);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);
        s.setAllowFileAccess(false);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            @Override public void onPageFinished(WebView view,String url){ status.setVisibility(View.GONE); }
            @Override public void onReceivedError(WebView view,WebResourceRequest req,WebResourceError error){
                if(req.isForMainFrame()){
                    status.setVisibility(View.VISIBLE);
                    status.setText("Oryza Flow dashboard is unavailable.

1. Connect to Oryza-AP
2. Use password oryzaflow20
3. Confirm the ESP32 is powered
4. Tap anywhere to retry");
                    status.setOnClickListener(v->webView.reload());
                }
            }
            @Override public boolean shouldOverrideUrlLoading(WebView view,WebResourceRequest req){
                Uri u=req.getUrl();
                if("192.168.4.1".equals(u.getHost())) return false;
                return true;
            }
        });
        webView.setDownloadListener((url,agent,disposition,mime,length)->{
            android.app.DownloadManager.Request r=new android.app.DownloadManager.Request(Uri.parse(url));
            r.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            r.setTitle("Oryza Flow cycle log");
            ((android.app.DownloadManager)getSystemService(DOWNLOAD_SERVICE)).enqueue(r);
        });
        webView.loadUrl(DASHBOARD);
    }

    private void immersive(){
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
    @Override public void onWindowFocusChanged(boolean focus){ super.onWindowFocusChanged(focus); if(focus) immersive(); }
    @Override public void onBackPressed(){ if(webView.canGoBack()) webView.goBack(); }
    @Override protected void onResume(){ super.onResume(); immersive(); if(webView!=null) webView.reload(); }
}
