package com.example.hand_knitted.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.hand_knitted.R;
import com.example.hand_knitted.util.MyUtils;

import butterknife.BindView;

public class TutorialActivity extends BaseActivity {

    @BindView(R.id.WB)
    public WebView webView;
    @BindView(R.id.TVinTB)
    public TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        Intent intent =  getIntent();
        int num = intent.getIntExtra("num",0);
        textView.setText(MyUtils.option[num]);
        String option = intent.getStringExtra("option");
        String url = "http://www.bianzhirensheng.com/jiaocheng.html?cid="+option;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);




    }
}
