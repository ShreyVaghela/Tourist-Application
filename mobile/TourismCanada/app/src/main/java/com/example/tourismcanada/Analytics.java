package com.example.tourismcanada;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tourismcanada.R;

public class Analytics extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analytics);
        getIntent();
        String URL="https://dbe6st6u2k.execute-api.us-east-1.amazonaws.com/dev/analytics";
        WebView browser = (WebView) findViewById(R.id.webviewID);
        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        browser.loadUrl(URL);
    }
}
