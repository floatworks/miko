package com.softtime.miko.BmobData;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by Administrator on 2015/2/2.
 */
public class match extends BmobObject {
    public String uid1;
    public String uid2;
    public BmobDate endAt;

    public String getUid1() {
        return uid1;
    }

    public void setUid1(String uid1) {
        this.uid1 = uid1;
    }

    public String getUid2() {
        return uid2;
    }

    public void setUid2(String uid2) {
        this.uid2 = uid2;
    }

    public BmobDate getEndAt() {
        return endAt;
    }

    public void setEndAt(BmobDate endAt) {
        this.endAt = endAt;
    }
}
