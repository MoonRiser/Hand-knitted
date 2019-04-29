package com.example.hand_knitted.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;

import com.example.hand_knitted.R;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;

public class SplashActivity extends BaseActivity {
    private final int TIME = 3500;
    private Intent intent;
    private SharedPreferences sp;
    private Boolean isRookie;
    @BindView(R.id.IVlogo)
    public ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_splash);

        init();
        // Glide.with(this).load(R.drawable.ic_knit).into(imageView);


        new Handler().postDelayed(() -> {
            if (BmobUser.isLogin()) {

                if (isRookie) {
                    intent = new Intent(SplashActivity.this, RookieActivity.class);
                } else {
                    // intent= new Intent(SplashActivity.this,MainActivity.class);
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }

            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            SplashActivity.this.finish();
        }, TIME);

    }


    private void setFullScreen() {
        //用于隐藏导航栏和状态栏的标识符
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN //hide statusBar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; //hide navigationBar
        decorView.setSystemUiVisibility(option);

    }

    private void init() {
        //   imageView = findViewById(R.id.IVlogo);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        isRookie = sp.getBoolean("isRookie", true);
    }


}
