package com.example.hand_knitted.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.example.hand_knitted.R;

public class BaseActivity extends AppCompatActivity {
    private Toast toast;
    protected Unbinder unbinder;
    private IntentFilter intentFilter;
    private NetWorkChangeRecevier netWorkChangeRecevier;
    private AlertDialog dialog;

    private SoundPool soundPool;
    private int soundID;


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netWorkChangeRecevier = new NetWorkChangeRecevier();
        initSound();
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(netWorkChangeRecevier, intentFilter);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (netWorkChangeRecevier != null) {
            unregisterReceiver(netWorkChangeRecevier);
            netWorkChangeRecevier = null;
        }

    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    /*自定义Toast*/
    public void showToast(String content) {
        if (toast == null) {
            toast = Toast.makeText(this, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    public void showToastLong(String content) {
        if (toast == null) {
            toast = Toast.makeText(this, content, Toast.LENGTH_LONG);
        } else {
            toast.setText(content);
        }
        toast.show();
    }


    class NetWorkChangeRecevier extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager)
                        context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo == null || !mNetworkInfo.isConnected()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("WARNING");
                    builder.setMessage("网络无法连接\nunable to connect network");
                    builder.setView(R.layout.dialog_warn);
                    builder.setCancelable(false);
                    dialog = builder.show();
                } else {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            }
        }
    }


    @SuppressLint("NewApi")
    private void initSound(){
        soundPool = new SoundPool.Builder().build();
        soundID = soundPool.load(this,R.raw.open_ended,1);
    }

    protected void playSound() {
        soundPool.play(
                soundID,
                0.5f,      //左耳道音量【0~1】
                0.5f,      //右耳道音量【0~1】
                0,         //播放优先级【0表示最低优先级】
                0,         //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                1          //播放速度【1是正常，范围从0~2】
        );
    }


}
