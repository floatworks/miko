package com.softtime.miko.BmobData;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/1/6.
 */
public class userdate extends BmobObject {
    String email;
    String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
