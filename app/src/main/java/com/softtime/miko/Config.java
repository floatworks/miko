package com.softtime.miko;

/**
 * Created by Administrator on 2015/1/8.
 */
public class Config {

    public  static  String applicationId = "3c18f2577233b3d021465b2885790e29";
    public static  String accessKey="84582b44e8db97a86f30b8c28411c4b4";
    public  static  int NOTICE_ID_INVITE = 66666;
    public static String temp ="";

    public static String getTemp() {
        return temp;
    }

    public static void setTemp(String temp) {
        Config.temp = temp;
    }

    public static String getapplicationId() {
        return applicationId;
    }

    public static void setapplicationId(String applicationId) {
        Config.applicationId = applicationId;
    }
}
