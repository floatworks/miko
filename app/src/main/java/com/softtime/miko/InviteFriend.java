package com.softtime.miko;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.softtime.miko.BmobData.User;
import com.softtime.miko.BmobData.inviteNotice;

import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class InviteFriend extends ActionBarActivity {

    Button btnInvite;
    EditText etFriendEmail;
    User gettedUserInfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        gettedUserInfo = BmobUser.getCurrentUser(this, User.class);//获取当前用户

        btnInvite = (Button) findViewById(R.id.btn_invite_friend_invite);
        etFriendEmail = (EditText) findViewById(R.id.et_invite_friend_friendEmail);
        //准备查找输入的Email目标用户
        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final BmobQuery<User> bmobQuery = new BmobQuery<User>();
                bmobQuery.addWhereEqualTo("email",etFriendEmail.getText().toString());
                bmobQuery.findObjects(InviteFriend.this,new FindListener<User>() {
                    @Override
                    public void onSuccess(List<User> users) {
                        if(users.size()==0){
                            Toast.makeText(InviteFriend.this,"没有找到用户",Toast.LENGTH_LONG).show();
                        }else {
                            BmobPushManager pushManager = new BmobPushManager(InviteFriend.this);
                            BmobQuery<BmobInstallation> query  = new BmobQuery<BmobInstallation>();
                            query.addWhereEqualTo("installationId",users.get(0).getInstallationId());
                            pushManager.setQuery(query);
                            pushManager.pushMessage("你收到一条新的约会邀请");
                            //然后把邀请信息储存到inviteNotice表里
                            inviteNotice inviteNotice = new inviteNotice();
                            inviteNotice.setState(0);
                            inviteNotice.setTo(users.get(0).getEmail());
                            inviteNotice.setFrom(gettedUserInfo.getEmail());
                            inviteNotice.save(InviteFriend.this,new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(InviteFriend.this,"信息已经发送",Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(InviteFriend.this,"发送过程中有点小问题:"+s,Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(InviteFriend.this,"服务器开小差了……",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });




    }






}
