package com.example.hand_knitted.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.hand_knitted.R;
import com.example.hand_knitted.bean.User;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends BaseActivity {


    @BindView(R.id.ETname)
    public EditText name;
    @BindView(R.id.CBremenber)
    public CheckBox remPWD;
    @BindView(R.id.BTlogin)
    public Button login;
    @BindView(R.id.BTregister)
    public Button register;
    @BindView(R.id.ETpwd)
    public EditText pwd;

    private EditText nameStr;//注册的对话框组件
    private EditText pwdStr;//同上
    private RadioGroup radioGroup;

    private String account;
    private String password;
    private Boolean isRemenber;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private View dialogRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        clickEvent();

    }


    private void init() {
      //  name = findViewById(R.id.ETname);
      //   pwd = findViewById(R.id.ETpwd);
      //  remPWD = findViewById(R.id.CBremenber);
      //  login = findViewById(R.id.BTlogin);
      //  register = findViewById(R.id.BTregister);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();
        isRemenber = sp.getBoolean("remember_password", false);

        /*如果选中记住密码，则*/
        if (isRemenber) {
            account = sp.getString("account", "");
            password = sp.getString("password", "");
            name.setText(account);
            pwd.setText(password);
            remPWD.setChecked(true);
        }

        /*注册用的对话框*/
        LayoutInflater factory = LayoutInflater.from(LoginActivity.this);
        dialogRegister = factory.inflate(R.layout.dialog_register, null, false);

    }


    private Boolean isPermissionGranted() {
        //运行时权限获取
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    private void requestPermiassionDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
        dialog.setTitle("说明");
        dialog.setCancelable(false);
        dialog.setMessage("完成正常功能需要获取 \n读取电话状态·读存储 权限\n不会用于其他用途，请放心 ( •̀ ω •́ )y");
        dialog.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);//申请权限
            }
        });
        dialog.show();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String permissions[], @NotNull int[] grantResults) {


        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    loginBmob();
                else
                    showToast("you denied the permission,that sounds terrible");
                //  Toast.makeText(LoginActivity.this,"you denied the permission",Toast.LENGTH_LONG).show();
                break;
        }
    }

    //点击事件
    private void clickEvent() {
        /*登陆按钮监听事件*/
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionGranted()) {
                    loginBmob();
                } else {
                    requestPermiassionDialog();
                }
            }
        });


        //注册点击事件使用对话框进行注册
        register.setOnClickListener(v -> {

            removeParentsView(dialogRegister);

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
            alertDialog.setTitle("用户注册");
            alertDialog.setView(dialogRegister);
            alertDialog.setPositiveButton("注册", (dialog, which) -> {
                registerBmob();//在后台注册账户信息

            });
            alertDialog.show();
        });
    }


    //负责登陆账号密码的判断
    private void loginBmob() {

        account = name.getText().toString();
        password = pwd.getText().toString();
        if (account.length() == 0 || password.length() == 0) {//当账号或密码的输入为空时提示
            showToast("输入为空，请重试");
            //Toast.makeText(this, "请输入账号密码", Toast.LENGTH_SHORT).show();
            return;
        }
        User bu = new User();
        bu.setUsername(account);
        bu.setPassword(password);
        bu.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser s, BmobException e) {
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                    if (remPWD.isChecked()) {
                        editor.putBoolean("remember_password", true);
                        editor.putString("account", account);
                        editor.putString("password", password);
                    } else {
                        editor.clear();
                    }

                    editor.apply();
                  //  Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Intent intent = new Intent(LoginActivity.this, RookieActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (e.getErrorCode() == 9016) {
                        Toast.makeText(LoginActivity.this, "网络不可用，请检查你的网络连接", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "账号或密码错误（password or account is not valid）", Toast.LENGTH_LONG).show();
                    }
                    Log.i("bmob", "登陆失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void registerBmob() {

        nameStr = dialogRegister.findViewById(R.id.nameStr);
        pwdStr = dialogRegister.findViewById(R.id.pwdStr);
        radioGroup = dialogRegister.findViewById(R.id.radioGroup);
        String userStr = nameStr.getText().toString();
        String passwdStr = pwdStr.getText().toString();
        String sex;

        int id = radioGroup.getCheckedRadioButtonId();
        if (R.id.male == id) {
            sex = "male";
        } else {
            sex = "female";
        }
        if (passwdStr.length() < 8) {
            Toast.makeText(LoginActivity.this, "密码至少8位", Toast.LENGTH_LONG).show();
            //      removeParentsView(dialogSignIn);
            return;
        }

        final User bu = new User();
        bu.setUsername(userStr);
        bu.setPassword(passwdStr);
        bu.setSex(sex);
        //Log.i("性别是否空指针",bu.getSex().toString());
        //Log.i("用户名是否空指针",bu.getUsername().toString());

        bu.signUp(new SaveListener<User>() {
            @Override
            public void done(User s, BmobException e) {
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "注册成功:"+bu.getUsername()+"你好" , Toast.LENGTH_LONG).show();
                } else {
                    Log.i("bmob", "注册失败：" + e.getMessage() + "," + e.getErrorCode());
                    if (e.getErrorCode() == 202) {
                        showToastLong("用户名已被注册，请更换");
                    }
                }
            }
        });
    }

    //去除视图的所有父视图
    public void removeParentsView(View view) {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeAllViews();
        }
    }

}
