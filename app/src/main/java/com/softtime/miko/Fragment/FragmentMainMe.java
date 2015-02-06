package com.softtime.miko.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bmob.BmobProFile;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.softtime.miko.BmobData.User;
import com.softtime.miko.Config;
import com.softtime.miko.EditProfile;
import com.softtime.miko.NoticeList;
import com.softtime.miko.R;

import cn.bmob.v3.BmobUser;


public class FragmentMainMe extends Fragment {

    User loginedUser;
    ImageView ivUserAvatar;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main_me,container,false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginedUser = BmobUser.getCurrentUser(getActivity(),User.class);
        Button btnEditProfile = (Button) getActivity().findViewById(R.id.btn_main_me_editprofile);
        Button btnCheckInviteNotice = (Button) getActivity().findViewById(R.id.btn_main_me_checkInviteNotice);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGotoEditProfile = new Intent(getActivity(),EditProfile.class);
                startActivity(intentGotoEditProfile);
            }
        });
        btnCheckInviteNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGotoNoticeList = new Intent(getActivity(), NoticeList.class);
                startActivity(intentGotoNoticeList);
            }
        });


        TextView tvMyName = (TextView) getActivity().findViewById(R.id.tv_main_me_username);//获得用户名
        tvMyName.setText(loginedUser.getUsername());//设置用户名
        ivUserAvatar = (ImageView) getActivity().findViewById(R.id.iv_main_me_avatar);//获得图片控件
        String avatarUrl = BmobProFile.getInstance(getActivity()).signURL(loginedUser.getAvatarFileName(),loginedUser.getAvatarUrl(),Config.accessKey,0,null);//URL签名获得真正的地址
        ImageLoaderConfiguration ilConfinguration = ImageLoaderConfiguration.createDefault(getActivity());//创建默认配置文件
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();//设置配置选项
        ImageLoader.getInstance().init(ilConfinguration);//绑定配置文件
        ImageLoader.getInstance().loadImage(avatarUrl,options,new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                System.out.println("ing");
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                    System.out.println("cao");
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    ivUserAvatar.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        System.out.println(avatarUrl);



    }
}
