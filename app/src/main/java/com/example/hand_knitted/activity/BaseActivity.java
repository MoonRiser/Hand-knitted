package com.example.hand_knitted.activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {
    private Toast toast;
    protected Unbinder unbinder;


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
    }


    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    /*自定义Toast*/
    public void  showToast(String content){
        if(toast==null){
            toast=Toast.makeText(this,content,Toast.LENGTH_SHORT);
        }else {
            toast.setText(content);
        }
        toast.show();
    }

    public void  showToastLong(String content){
        if(toast==null){
            toast=Toast.makeText(this,content,Toast.LENGTH_LONG);
        }else {
            toast.setText(content);
        }
        toast.show();
    }

}
