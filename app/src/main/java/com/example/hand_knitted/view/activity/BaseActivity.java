package com.example.hand_knitted.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {
    private Toast toast;






    /*自定义Toast*/
    public void  showToast(String content){
        if(toast==null){
            toast=Toast.makeText(this,content,Toast.LENGTH_SHORT);
        }else {
            toast.setText(content);
        }
        toast.show();
    }
}
