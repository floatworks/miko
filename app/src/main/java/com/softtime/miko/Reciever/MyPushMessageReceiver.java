package com.softtime.miko.Reciever;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import android.widget.Toast;

import com.softtime.miko.Config;
import com.softtime.miko.R;

import cn.bmob.push.PushConstants;

/**
 * Created by Administrator on 2015/1/15.
 */
public class MyPushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            Toast.makeText(context, "客户端收到推送内容：" + intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING), Toast.LENGTH_LONG).show();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.drawable.logo);
            builder.setContentTitle("柔软时光");
            builder.setContentText("您有一条新的消息");
            Notification notice = builder.build();
            notificationManager.notify(Config.NOTICE_ID_INVITE,notice);


        }
    }

}
