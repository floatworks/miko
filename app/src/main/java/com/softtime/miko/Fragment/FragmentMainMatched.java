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
import com.softtime.miko.BmobData.match;
import com.softtime.miko.InviteFriend;
import com.softtime.miko.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

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
        //查询当前用户的匹配信息
        loginedUser = BmobUser.getCurrentUser(getActivity(), User.class);
        BmobQuery<match> matchBmobQueryEq1 = new BmobQuery<match>();
        matchBmobQueryEq1.addWhereEqualTo("uid1",loginedUser.getEmail());//获得登录用户发起的匹配
        BmobQuery<match> matchBmobQueryEq2 = new BmobQuery<match>();
        matchBmobQueryEq2.addWhereEqualTo("uid2", loginedUser.getEmail());//获得登录用户收到的匹配
        //将这两个条件封装成一个List 这个list里面装的是两个条件
        List<BmobQuery<match>> queries = new ArrayList<BmobQuery<match>>();
        queries.add(matchBmobQueryEq1);
        queries.add(matchBmobQueryEq2);
        BmobQuery<match> preQuery = new BmobQuery<match>();
        preQuery.or(queries);//将这两个条件设置成了or查询
        preQuery.findObjects(getActivity(),new FindListener<match>() {
            @Override
            public void onSuccess(List<match> matches) {
                //将获得的结束时间转成Date类型
                String dateString = matches.get(0).getEndAt().getDate();
                Date endDate;
                SimpleDateFormat simpleDateFormatEndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    endDate = simpleDateFormatEndDate.parse(dateString);
                    //现在时间和到期时间相减，得到一个毫秒数
                    long duringTimeSS = endDate.getTime()-System.currentTimeMillis();
                    long hours = duringTimeSS/(1000*60*60);
                    tvTitleBar.setText("剩余"+hours+"小时");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });



    }
}
