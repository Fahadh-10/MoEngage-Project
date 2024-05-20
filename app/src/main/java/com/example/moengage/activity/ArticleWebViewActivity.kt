package com.example.moengage.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.moengage.helpers.NavigationKey
import com.example.moengage.databinding.ActivityArticleWebViewBinding

class ArticleWebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        readBundles()
    }

    /**
     * Reads the intent extras to get the web URL and loads the web page if available.
     */
    private fun readBundles(){
        if (intent.hasExtra(NavigationKey.WEB_URL)){
            val webUrl = intent.getStringExtra(NavigationKey.WEB_URL)
            if (webUrl != null) {
                loadWebView(webUrl)
            }
        }
    }

    /**
     * Loads the web URL into the WebView and configures WebView settings
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView(webUrl: String) {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = WebViewClient()
        binding.progressBar.visibility = VISIBLE
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.progressBar.visibility = GONE
                }, 300)
            }
        }
        binding.webView.loadUrl(webUrl)
    }
}