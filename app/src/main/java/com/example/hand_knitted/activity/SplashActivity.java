package com.example.hand_knitted.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.hand_knitted.R;

import cn.bmob.v3.BmobUser;

public class SplashActivity extends BaseActivity {
    private final int TIME =3500;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.IVlogo);
        Glide.with(this).load(R.drawable.ic_knit).into(imageView);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (BmobUser.isLogin()){
                    intent= new Intent(SplashActivity.this,MainActivity.class);
                }else{
                    intent= new Intent(SplashActivity.this,LoginActivity.class);
                }
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, TIME);
    }



    private void setFullScreen(){
        //用于隐藏导航栏和状态栏的标识符
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(option);

    }



}
