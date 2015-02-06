package com.softtime.miko;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.softtime.miko.Adapter.InviteNoticeListAdapter;
import com.softtime.miko.BmobData.User;
import com.softtime.miko.BmobData.inviteNotice;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;


public class NoticeList extends ActionBarActivity {

    ListView lvInviteNotice;
    InviteNoticeListAdapter inviteNoticeListAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);
        lvInviteNotice = (ListView) findViewById(R.id.listView_invite_notice);//消息列表控件
        User loginedUser = BmobUser.getCurrentUser(this,User.class);//获取现在登录用户
        BmobQuery<inviteNotice> query = new BmobQuery<inviteNotice>();
        query.addWhereEqualTo("to",loginedUser.getEmail());//设置检索条件，发送给当前用户的邀请消息
        query.addWhereNotEqualTo("state",1);//设置检索条件，状态不等于1的消息，（状态为1是已经处理的）
        query.findObjects(this,new FindListener<inviteNotice>() {
            @Override
            public void onSuccess(List<inviteNotice> inviteNotices) {
                inviteNoticeListAdapter = new InviteNoticeListAdapter(NoticeList.this,R.layout.listview_item_invite_notice,inviteNotices);
                lvInviteNotice.setAdapter(inviteNoticeListAdapter);
                System.out.println(inviteNotices.size()+"pppppppppppppppppppppppppppppppp");

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(NoticeList.this,s,Toast.LENGTH_LONG).show();
            }
        });




    }



}
