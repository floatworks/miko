package com.softtime.miko.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softtime.miko.BmobData.User;
import com.softtime.miko.InviteFriend;
import com.softtime.miko.R;

/**
 * 如果已经匹配到好友，就显示这个页面
 */
public class FragmentMainMatched extends Fragment {

    User loginedUser;
    TextView tvTitleBar;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_matched, container, false);
    }







    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvTitleBar = (TextView) getActivity().findViewById(R.id.tv_titlebar);





    }
}
