package com.example.scholateacher

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class AcademicCalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_academic_calendar)

        val webView: WebView = findViewById(R.id.webView)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_NO_CACHE
        }

        webView.webViewClient = WebViewClient()

        webView.loadUrl("https://drive.google.com/drive/folders/1kGH2sSHl4uX-6-wFSYFxWaJNHcMK2yiQ_-5JvWwRw8aLuLM-Z8mW2AYLq-65bgx_9fvpZ9j6")
    }
}
