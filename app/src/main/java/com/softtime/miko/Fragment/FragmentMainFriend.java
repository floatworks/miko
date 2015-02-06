package com.softtime.miko.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.softtime.miko.BmobData.User;
import com.softtime.miko.InviteFriend;
import com.softtime.miko.R;


public class FragmentMainFriend extends Fragment {

    User loginedUser;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_main_picbox, container, false);
    }







    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //如果显示的是还没有匹配到好友的页面，那么就要获得那个邀请好友的按钮。

            Button btnInviteFriend = (Button) getActivity().findViewById(R.id.btn_main_inviteFriend);
            btnInviteFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentInviteFriend = new Intent(getActivity(), InviteFriend.class);
                    startActivity(intentInviteFriend);
                }
            });



    }
}
