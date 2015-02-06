package com.softtime.miko.BmobData;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2015/1/7.
 */
public class User extends BmobUser {

    private String avatarUrl;
    private String avatarFileName;
    private String installationId;
    private int onDating;

    public int getOnDating() {
        return onDating;
    }

    public void setOnDating(int onDating) {
        this.onDating = onDating;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarFileName() {
        return avatarFileName;
    }

    public void setAvatarFileName(String avatarFileName) {
        this.avatarFileName = avatarFileName;
    }





    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }


}
