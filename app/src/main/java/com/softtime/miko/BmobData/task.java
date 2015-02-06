package com.softtime.miko.BmobData;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/1/8.
 */
public class task extends BmobObject implements Serializable {
    private String taskname;
    private String desc;
    String createUserEmail;
    String backgroundFilename;
    String backgroundUrl;

    public String getBackgroundFilename() {
        return backgroundFilename;
    }

    public void setBackgroundFilename(String backgroundFilename) {
        this.backgroundFilename = backgroundFilename;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getCreateUserObjectId() {
        return createUserObjectId;
    }

    public void setCreateUserObjectId(String createUserObjectId) {
        this.createUserObjectId = createUserObjectId;
    }

    public String getCreateUserEmail() {
        return createUserEmail;
    }

    public void setCreateUserEmail(String createUserEmail) {
        this.createUserEmail = createUserEmail;
    }

    String createUserObjectId;

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
