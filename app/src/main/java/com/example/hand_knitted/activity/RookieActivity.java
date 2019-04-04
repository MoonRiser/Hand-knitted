
package com.example.hand_knitted.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.hand_knitted.R;

import butterknife.BindView;

public class RookieActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.IMGstick)
    public ImageView stick;
    @BindView(R.id.IMGhook)
    public ImageView hook;
    @BindView(R.id.BTskip)
    public Button skip;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rookie);
        init();


        Glide.with(this).load(R.drawable.stick).into(stick);
        Glide.with(this).load(R.drawable.hook).into(hook);
        skip.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.BTskip : skipImp();break;
        }
    }



    private void init(){
     //   stick = findViewById(R.id.IMGstick);
     //   hook = findViewById(R.id.IMGhook);
     //   skip = findViewById(R.id.BTskip);
        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();


    }



    private void skipImp(){

        editor.putBoolean("isRookie",false);
        editor.apply();
        Intent intent = new Intent(RookieActivity.this,MainActivity.class);
        startActivity(intent);

    }



}
