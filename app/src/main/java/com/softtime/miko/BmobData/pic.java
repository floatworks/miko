package com.softtime.miko.BmobData;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/2/20.
 */
public class pic extends BmobObject {
    String picDesc;
    String user;
    String picUrl;
    String picFilename;
    String topicObjectId;

    public String getPicDesc() {
        return picDesc;
    }

    public void setPicDesc(String picDesc) {
        this.picDesc = picDesc;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicFilename() {
        return picFilename;
    }

    public void setPicFilename(String picFilename) {
        this.picFilename = picFilename;
    }

    public String getTopicObjectId() {
        return topicObjectId;
    }

    public void setTopicObjectId(String topicObjectId) {
        this.topicObjectId = topicObjectId;
    }
}
