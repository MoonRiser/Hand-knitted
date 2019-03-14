package com.example.hand_knitted.util;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        Bmob.initialize(this, "Your Application ID","bmob");
    }



    public Context getContext(){
        return context;
    }

}
