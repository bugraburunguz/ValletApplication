package com.bugraburunguz.android.valetapp.activity

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.bugraburunguz.android.valetapp.R

class ManualActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual)
        val view = WebView(this)
        view.loadUrl("file:///android_asset/manual.html")
        setContentView(view)
    }
}