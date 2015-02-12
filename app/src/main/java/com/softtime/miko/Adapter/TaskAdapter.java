package com.softtime.miko.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bmob.BmobProFile;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.softtime.miko.BmobData.task;
import com.softtime.miko.Config;
import com.softtime.miko.R;

import java.util.List;

/**
 * Created by Administrator on 2015/1/10.
 */
public class TaskAdapter extends ArrayAdapter<task> {
    private int resourceId;

    public TaskAdapter(Context context, int resource, List<task> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        task task = getItem(position);//获得对象
        View view ;
        final ViewHolder viewHolder;
        if(convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView= (ImageView)view.findViewById(R.id.iv_main_task_background);
            viewHolder.title=(TextView) view.findViewById(R.id.textView_main_task_title);
            viewHolder.desc = (TextView) view.findViewById(R.id.textView_main_task_desc);
            view.setTag(viewHolder);
        }else {
          view =convertView;
            viewHolder = (ViewHolder)view.getTag();
        };
        TextView title = (TextView) view.findViewById(R.id.textView_main_task_title);
        TextView desc = (TextView) view.findViewById(R.id.textView_main_task_desc);
        final ImageView img = (ImageView) view.findViewById(R.id.iv_main_task_background);
        //拼凑出图片地址
        String backUrl = BmobProFile.getInstance(view.getContext()).signURL(task.getBackgroundFilename(),task.getBackgroundUrl(), Config.accessKey,0,null);//URL签名获得真正的地址
        //设置显示用UniversualImageLoader
        ImageLoaderConfiguration ilConfinguration = ImageLoaderConfiguration.createDefault(view.getContext());//创建默认配置文件
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();//设置配置选项
        ImageLoader.getInstance().init(ilConfinguration);//绑定配置文件
        ImageLoader.getInstance().loadImage(backUrl,options,new ImageLoadingListener() {
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
                viewHolder.imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

        viewHolder.title.setText(task.getTaskname());
        viewHolder.desc.setText(task.getDesc());
        return view;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView title;
        TextView desc;
    }
}
