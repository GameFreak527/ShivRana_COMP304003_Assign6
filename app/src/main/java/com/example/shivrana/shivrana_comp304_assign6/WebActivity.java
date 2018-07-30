package com.example.shivrana.shivrana_comp304_assign6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class WebActivity extends AppCompatActivity {
    WebView webView;
    Button  tripBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        //Declaration of the controls
        declaration();

        //Loading image into the web View
        loadImage();

        //Button Click event
        buttonEvent();
    }

    public void declaration(){
        webView = findViewById(R.id.TripWebView);
        tripBtn = findViewById(R.id.tripBtn);
    }

    public void loadImage(){
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl("https://upload.wikimedia.org/wikipedia/commons/e/e8/Montage_of_TTC_2.jpg");
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    public void buttonEvent(){
        tripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),TTCActivity.class));
            }
        });
    }
}
