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

import com.softtime.miko.Fragment.FragmentMainMatched;
import com.softtime.miko.Fragment.FragmentMainTopic;
import com.softtime.miko.Fragment.FragmentMainMe;
import com.softtime.miko.Fragment.FragmentMainFriend;
import com.softtime.miko.Fragment.FragmentMainChat;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
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
        System.out.println(gettedUserInfo.getObjectId());
        //下面这段代码是因为Bmob里面有Bug,总是无法获得getOnDating的值，所以要手动查一下
        BmobQuery<User> query = new BmobQuery<User>();
        query.getObject(this,gettedUserInfo.getObjectId(),new GetListener<User>() {
            @Override
            public void onSuccess(User user) {
                System.out.println(user.getOnDating()+"usergetondating");
                isMatched = user.getOnDating();
                switch (isMatched){
                    case 0:
                        System.out.println("ismatch"+isMatched);
                        setTabSelection(1);
                        break;
                    case 1:
                        setTabSelection(5);
                        break;
                }
            }

            @Override
            public void onFailure(int i, String s) {
                System.out.println(s);
            }
        });



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
                BmobQuery<User> query = new BmobQuery<User>();
                query.getObject(MainActivity.this,gettedUserInfo.getObjectId(),new GetListener<User>() {
                    @Override
                    public void onSuccess(User user) {
                        System.out.println(user.getOnDating()+"usergetondating");
                        isMatched = user.getOnDating();
                        switch (isMatched){
                            case 0:
                                System.out.println("ismatch"+isMatched);
                                setTabSelection(1);
                                break;
                            case 1:
                                setTabSelection(5);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        System.out.println(s);
                    }
                });

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








