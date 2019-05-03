
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
    @BindView(R.id.BTstyle1)
    public Button style1;
    @BindView(R.id.BTstyle2)
    public Button style2;
    @BindView(R.id.BTstyle3)
    public Button style3;
    @BindView(R.id.BTstyle4)
    public Button style4;
    @BindView(R.id.BTstyle5)
    public Button style5;

    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rookie);
        init();

        Glide.with(this).load(R.drawable.stick).into(stick);
        Glide.with(this).load(R.drawable.hook).into(hook);



    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.BTskip:
                skipImp();
                break;
            default:Intent intent = new Intent(this,TutorialActivity.class);
            startActivity(intent);break;
        }
    }


    private void init() {
        //   stick = findViewById(R.id.IMGstick);
        //   hook = findViewById(R.id.IMGhook);
        //   skip = findViewById(R.id.BTskip);
        skip.setOnClickListener(this);
        hook.setOnClickListener(this);
        stick.setOnClickListener(this);
        style1.setOnClickListener(this);
        style2.setOnClickListener(this);
        style3.setOnClickListener(this);
        style4.setOnClickListener(this);
        style5.setOnClickListener(this);
        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();


    }


    private void skipImp() {

        editor.putBoolean("isRookie", false);
        editor.apply();
        Intent intent = new Intent(RookieActivity.this, MainActivity.class);
        startActivity(intent);

    }


}
