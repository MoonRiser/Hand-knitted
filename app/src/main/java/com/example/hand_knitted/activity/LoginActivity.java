package com.example.hand_knitted.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hand_knitted.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends BaseActivity {

    private EditText name;
    private EditText pwd;
    private CheckBox remPWD;
    private Button login;
    private Button register;
    private String account;
    private Boolean isRemenber;
    private String password;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPermissionGranted()){
                    loginBmob();
                }else {
                    requestPermiassionDialog();
                }
            }
        });
    }


    private void init(){
        name = findViewById(R.id.ETname);
        pwd = findViewById(R.id.ETpwd);
        remPWD = findViewById(R.id.CBremenber);
        login = findViewById(R.id.BTlogin);
        register = findViewById(R.id.BTregister);

        sp= PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();
        account = name.getText().toString();
        password = pwd.getText().toString();
        isRemenber=sp.getBoolean("remember_password",false);

        /*如果选中记住密码，则*/
        if(isRemenber){
            account = sp.getString("account","");
            password= sp.getString("password","");
            name.setText(account);
            pwd.setText(password);
            remPWD.setChecked(true);
        }
    }


    private Boolean isPermissionGranted(){
        //运行时权限获取
        if(ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_PHONE_STATE)== PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            return false;
        }
    }


    private void requestPermiassionDialog(){
        AlertDialog.Builder  dialog = new AlertDialog.Builder(LoginActivity.this);
        dialog.setTitle("说明");
        dialog.setCancelable(false);
        dialog.setMessage("完成正常功能需要获取 \n读取电话状态·读存储 权限\n不会用于其他用途，请放心 ( •̀ ω •́ )y");
        dialog.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},1);//申请权限
            }
        });
        dialog.show();

    }


    public void onRequestPermissionsResult(int requestCode ,String permissions[],int[] grantResults){

        switch (requestCode){
            case  1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED)
                    loginBmob();
                else
                    showToast("you denied the permission,that sounds terrible");
                  //  Toast.makeText(LoginActivity.this,"you denied the permission",Toast.LENGTH_LONG).show();
                break;
        }
    }


    //负责登陆账号密码的判断
    private void  loginBmob(){

        if(account.length()==0  || password.length()==0){//当账号或密码的输入为空时提示
            showToast("输入为空，请重试");
            //Toast.makeText(this, "请输入账号密码", Toast.LENGTH_SHORT).show();
            return;
        }
        BmobUser bu = new BmobUser();
        bu.setUsername(account);
        bu.setPassword(password);
        bu.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser s, BmobException e) {
                if(e==null){
                    Toast.makeText(LoginActivity.this, "登陆成功" , Toast.LENGTH_LONG).show();
                    if(remPWD.isChecked()){
                        editor.putBoolean("remember_password",true);
                        editor.putString("account",account);
                        editor.putString("password",password);
                    }else {
                        editor.clear();}

                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    if(e.getErrorCode()==9016){
                        Toast.makeText(LoginActivity.this, "网络不可用，请检查你的网络连接", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(LoginActivity.this,"账号或密码错误（password or account is not valid）",Toast.LENGTH_LONG).show();
                    }
                    Log.i("bmob","登陆失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

}
