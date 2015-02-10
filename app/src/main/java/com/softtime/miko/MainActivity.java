package com.softtime.miko;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.softtime.miko.BmobData.User;

import com.softtime.miko.BmobData.match;
import com.softtime.miko.Fragment.FragmentMainMatched;
import com.softtime.miko.Fragment.FragmentMainTopic;
import com.softtime.miko.Fragment.FragmentMainMe;
import com.softtime.miko.Fragment.FragmentMainFriend;
import com.softtime.miko.Fragment.FragmentMainChat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

public class MainActivity extends Activity {


    //底部导航按钮
    ImageView ivBottombarPicbox;
    ImageView ivBottombarTopic;
    ImageView ivBottombarChat;
    ImageView ivBottombarMe;

    //5个Fragment
    Fragment fragmentPicbox;
    Fragment fragmentTopic;
    Fragment fragmentChat;
    Fragment fragmentMe;
    Fragment fragmentMatched;
    //FragmentManager
    FragmentManager fragmentManager;
    //获取当前登录用户（用的是Bmob的云服务）
    User gettedUserInfo;
    int isMatched;//查看是否已经匹配了
    match nowMatch ; //现在正在匹配中的match

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, Config.applicationId);
        gettedUserInfo = BmobUser.getCurrentUser(this, User.class);
        fragmentManager = getFragmentManager();
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this, "3c18f2577233b3d021465b2885790e29");
        init();//初始化控件
        /**
         *  现在我想通过查找match表来确定用户是否已经匹配成功
         *  我应该根据用户email和isDead来查出这个用户还没有结束的match
         *  如果能查到，那么就说明已经在匹配中了
         */
        //先查到from或者to里面含有登录用户的所有项
        BmobQuery<match> matchBmobQueryEq1 = new BmobQuery<match>();
        matchBmobQueryEq1.addWhereEqualTo("uid1",gettedUserInfo.getEmail());//获得登录用户发起的匹配
        BmobQuery<match> matchBmobQueryEq2 = new BmobQuery<match>();
        matchBmobQueryEq2.addWhereEqualTo("uid2", gettedUserInfo.getEmail());//获得登录用户收到的匹配
        //将这两个条件封装成一个List 这个list里面装的是两个条件
        List<BmobQuery<match>> queries = new ArrayList<BmobQuery<match>>();
        queries.add(matchBmobQueryEq1);
        queries.add(matchBmobQueryEq2);
        BmobQuery<match> preQuery = new BmobQuery<match>();
        preQuery.or(queries);//将这两个条件设置成了or查询
        preQuery.findObjects(this,new FindListener<match>() {
            @Override
            public void onSuccess(List<match> matches) {
                if(matches.size()==0){
                    isMatched = 0;
                    nowMatch =null;
                }else {
                    isMatched = 1;
                    nowMatch = matches.get(0);

                }
                switch (isMatched){
                    case 0:
                        setTabSelection(1);//如果没有匹配就显示没匹配的首页
                        break;
                    case 1:
                        setTabSelection(5);
                        SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
                        break;//如果匹配了就显示已经匹配的首页
                }

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        });
        //在这里加入一下判断，看看匹配是不是已经过期了，如果已经过期了就删除这个match，然后把这个match移动到一个备份表里


    }


    /**
     * init（）
     * 初始化控件
     */

    private void init() {
        //初始化5个页面
        fragmentTopic = new FragmentMainTopic();
        fragmentChat = new FragmentMainChat();
        fragmentMe = new FragmentMainMe();
        fragmentMatched = new FragmentMainMatched();
        fragmentPicbox = new FragmentMainFriend();


        //初始化四个底部导航按钮
        ivBottombarPicbox = (ImageView) findViewById(R.id.iv_bottom_bar_friend);
        ivBottombarTopic = (ImageView) findViewById(R.id.iv_bottom_bar_topic);
        ivBottombarChat = (ImageView) findViewById(R.id.iv_bottom_bar_chat);
        ivBottombarMe = (ImageView) findViewById(R.id.iv_bottom_bar_me);
        //底部导航按钮的点击事件

        ivBottombarPicbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivBottombarPicbox.setBackgroundColor(getResources().getColor(R.color.yello300));
                //看看是否已经匹配
                switch (isMatched){
                    case 0:

                        setTabSelection(1);//如果没有匹配就显示没匹配的首页
                        break;
                    case 1:
                        setTabSelection(5);

                        break;//如果匹配了就显示已经匹配的首页
                }

            }
        });

        ivBottombarTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivBottombarTopic.setBackgroundColor(getResources().getColor(R.color.yello300));
                setTabSelection(2);
            }
        });

        ivBottombarChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivBottombarChat.setBackgroundColor(getResources().getColor(R.color.yello300));
                setTabSelection(3);
            }
        });

        ivBottombarMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivBottombarMe.setBackgroundColor(getResources().getColor(R.color.yello300));
                setTabSelection(4);
            }
        });

    }


    /**
     * 设置显示哪个fragment
     *
     * @param index 按顺序的1，2，3，4
     */
    private void setTabSelection(int index) {
        //先开启一个事务
        setAllButtonWhite();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        gettedUserInfo = BmobUser.getCurrentUser(this, User.class);
        switch (index) {
            case 1:
                ivBottombarPicbox.setBackgroundColor(getResources().getColor(R.color.yello300));
                transaction.replace(R.id.mainContent, fragmentPicbox);
                System.out.println("picbox" + gettedUserInfo.getOnDating());
                break;
            case 2:
                ivBottombarTopic.setBackgroundColor(getResources().getColor(R.color.yello300));
                transaction.replace(R.id.mainContent, fragmentTopic);
                break;
            case 3:
                ivBottombarChat.setBackgroundColor(getResources().getColor(R.color.yello300));
                transaction.replace(R.id.mainContent, fragmentChat);
                break;
            case 4:
                ivBottombarMe.setBackgroundColor(getResources().getColor(R.color.yello300));
                transaction.replace(R.id.mainContent, fragmentMe);
                break;
            case 5:
                ivBottombarPicbox.setBackgroundColor(getResources().getColor(R.color.yello300));
                transaction.replace(R.id.mainContent, fragmentMatched);
                System.out.println("matchen" + gettedUserInfo.getOnDating());
        }
        transaction.commit();
    }

    /**
     * 隐藏所有的fragment
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (fragmentPicbox != null) {
            transaction.hide(fragmentPicbox);
        }
        if (fragmentTopic != null) {
            transaction.hide(fragmentTopic);
        }
        if (fragmentChat != null) {
            transaction.hide(fragmentChat);
        }
        if (fragmentMe != null) {
            transaction.hide(fragmentMe);
        }
    }

    /**
     * setAllButtonWhite
     * 把所有按钮背景色设置成白色
     */

    private void setAllButtonWhite() {
        ivBottombarPicbox.setBackgroundColor(getResources().getColor(R.color.white));
        ivBottombarTopic.setBackgroundColor(getResources().getColor(R.color.white));
        ivBottombarChat.setBackgroundColor(getResources().getColor(R.color.white));
        ivBottombarMe.setBackgroundColor(getResources().getColor(R.color.white));
    }


}








