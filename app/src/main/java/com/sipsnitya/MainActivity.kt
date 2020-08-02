package com.sipsnitya

import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;
class MainActivity:AppCompatActivity() {
    private val web:WebView
    internal var webUrl = "https://google.com"
    private val swipeRefreshLayout:SwipeRefreshLayout
    @SuppressLint("SetJavaScriptEnabled")
    protected fun onCreate(savedInstanceState:Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getSupportActionBar().hide()
        setContentView(R.layout.activity_main)
        web = findViewById(R.id.webView) as WebView
        web.loadUrl(webUrl)
        val mywebsettings = web.getSettings()
        mywebsettings.setJavaScriptEnabled(true)
        ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        web.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 9; Pixel3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Mobile Safari/537.36")
        web.setWebViewClient(object:WebViewClient() {
            fun shouldOverrideUrlLoading(view:WebView, url:String):Boolean {
                if (url == null || url.startsWith("http://") || url.startsWith("https://")) return false
                try
                {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    view.getContext().startActivity(intent)
                    return true
                }
                catch (e:Exception) {
                    return true
                }
            }
        })
        web.setWebChromeClient(object:WebChromeClient() {
            fun onGeolocationPermissionsShowPrompt(origin:String, callback:GeolocationPermissions.Callback) {
                callback.invoke(origin, true, false)
            }
            fun onPermissionRequest(request:PermissionRequest) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    request.grant(request.getResources())
                }
            }
        })
        //improve webview performance
        web.getSettings().setLoadsImagesAutomatically(true)
        web.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH)
        web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)
        web.getSettings().setAppCacheEnabled(true)
        web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY)
        mywebsettings.setDomStorageEnabled(true)
        mywebsettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS)
        mywebsettings.setUseWideViewPort(true)
        mywebsettings.setSavePassword(true)
        mywebsettings.setSaveFormData(true)
        mywebsettings.setEnableSmoothTransition(true)
        //pull to refresh
        swipeRefreshLayout = findViewById(R.id.swipe) as SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(object:SwipeRefreshLayout.OnRefreshListener() {
            fun onRefresh() {
                swipeRefreshLayout.setRefreshing(true)
                Handler().postDelayed(object:Runnable {
                    public override fun run() {
                        swipeRefreshLayout.setRefreshing(false)
                        web.reload()
                    }
                }, 2000)
            }
        })
        swipeRefreshLayout.setColorSchemeColors(
            getResources().getColor(android.R.color.holo_blue_dark),
            getResources().getColor(android.R.color.holo_orange_dark),
            getResources().getColor(android.R.color.holo_green_dark),
            getResources().getColor(android.R.color.holo_red_dark)
        )
    }
    fun onBackPressed() {
        if (web.canGoBack())
        {
            web.goBack()
        }
        else
        {
            super.onBackPressed()
        }
    }
}