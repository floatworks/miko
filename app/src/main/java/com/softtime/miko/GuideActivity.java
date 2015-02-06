package com.softtime.miko;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.softtime.miko.Adapter.ViewPagerAdapter;
import com.softtime.miko.BmobData.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;


public class GuideActivity extends Activity {

    private ViewPager guideViewPage ;
    private ViewPagerAdapter guideViewPageAdapter;
    private List<View> views;
    private ImageView[] dots;

    Button btnLogin;
    Button btnReg;
    Intent intentToLogin;
    Intent intentToReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bmob.initialize(this, "3c18f2577233b3d021465b2885790e29");
        User userinfo = BmobUser.getCurrentUser(this,User.class);
        if(userinfo==null){
            setContentView(R.layout.guide);
            init();


            btnLogin = (Button) findViewById(R.id.btn_login);
            btnReg = (Button) findViewById(R.id.btn_reg);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    intentToLogin = new Intent(GuideActivity.this,LoginActivity.class);
                    startActivity(intentToLogin);

                }
            });

            btnReg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentToReg = new Intent(GuideActivity.this,RegActivity.class);
                    startActivity(intentToReg);


                }
            });
        }else {
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }


    }


    private  void init(){
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.guide_page_1,null));
        views.add(inflater.inflate(R.layout.guide_page_2,null));
        views.add(inflater.inflate(R.layout.guide_page_3,null));
        views.add(inflater.inflate(R.layout.guide_page_4,null));
        views.add(inflater.inflate(R.layout.guide_page_5,null));

        guideViewPageAdapter = new ViewPagerAdapter(views,this);

        guideViewPage = (ViewPager) findViewById(R.id.guideViewPager);
        guideViewPage.setAdapter(guideViewPageAdapter);

  }



}
