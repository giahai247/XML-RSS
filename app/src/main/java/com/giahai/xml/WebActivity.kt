package com.giahai.xml

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web.*


class WebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val intent = intent
        val link = intent.getStringExtra("link")

        webView.loadUrl(link)

        webView.webViewClient = WebViewClient()
    }
}
