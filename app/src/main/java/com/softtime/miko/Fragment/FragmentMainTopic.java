package com.softtime.miko.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.softtime.miko.Adapter.TaskAdapter;
import com.softtime.miko.AddNewTopic;
import com.softtime.miko.BmobData.task;
import com.softtime.miko.Config;
import com.softtime.miko.R;
import com.softtime.miko.TaskDetail;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class FragmentMainTopic extends Fragment {

    @Nullable

    Activity activity;
    ListView listView;
    List<task> mtasks;
    Button btnAddNewTopic;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_main_topic,container,false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        listView = (ListView) getActivity().findViewById(R.id.listView_main_pic_task);//获得ListView
        BmobQuery<task> taskBmobQuery = new BmobQuery<task>();//准备查询task表
        taskBmobQuery.findObjects(getActivity(),new FindListener<task>() {
            @Override
            public void onSuccess(List<task> tasks) {
                mtasks = tasks;
                TaskAdapter taskAdapter = new TaskAdapter(activity,R.layout.listview_item_main_task,tasks);
                listView.setAdapter(taskAdapter);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(activity, "读取数据失败" + s, Toast.LENGTH_SHORT).show();
            }
        });


       //下面设置列表点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(activity,"this is"+position,Toast.LENGTH_SHORT).show();//调试用，显示这是第几个任务
                Intent intentToTaskDetail = new Intent(activity,TaskDetail.class);
                //把背景图直接算出来
                String backgroundImgUrl = BmobProFile.getInstance(getActivity()).signURL(mtasks.get(position).getBackgroundFilename(), mtasks.get(position).getBackgroundUrl(), Config.accessKey,0,null);
                intentToTaskDetail.putExtra("title",mtasks.get(position).getTaskname());
                intentToTaskDetail.putExtra("desc",mtasks.get(position).getDesc());
                intentToTaskDetail.putExtra("background",backgroundImgUrl);
                intentToTaskDetail.putExtra("objectId",mtasks.get(position).getObjectId());
                startActivity(intentToTaskDetail);
            }
        });


       //添加新主题按钮相关代码
        btnAddNewTopic = (Button) activity.findViewById(R.id.btn_add_task);
        btnAddNewTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGotoAddTopic = new Intent(getActivity(), AddNewTopic.class);
                startActivity(intentGotoAddTopic);
            }
        });
    }


}
