package com.softtime.miko.BmobData;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/1/14.
 */
public class inviteNotice extends BmobObject {
    public String from;
    public String to;
    public int state;
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


}
