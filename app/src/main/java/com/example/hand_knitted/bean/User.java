package com.example.hand_knitted.bean;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {


    private String sex;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
