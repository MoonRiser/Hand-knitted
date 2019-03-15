package com.example.hand_knitted.util;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application {

    private  Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        Bmob.initialize(this, "5bb030d2c8976622d951f1fb49e61ffc");
    }



    public  Context getContext(){
        return context;
    }

}
